package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import xyz.radiish.zephyr.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class JsonSerializing {
  private static List<JsonSerializer<?>> SERIALIZERS;
  private static Reflections REFLECTIONS;

  private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<Class<?>, Class<?>>() {{
      put(boolean.class, Boolean.class);
      put(byte.class, Byte.class);
      put(char.class, Character.class);
      put(double.class, Double.class);
      put(float.class, Float.class);
      put(int.class, Integer.class);
      put(long.class, Long.class);
      put(short.class, Short.class);
      put(void.class, Void.class);
  }};

  public static AnnotationInfo generateAnnotationInfo(Field field, Class<?> clazz, Class<? extends Annotation> annotation) {
    String defaultName = field.getName();
    if(field.getName().startsWith("get") || field.getName().startsWith("set")) {
      defaultName = field.getName().substring(3);
    } else if(field.getName().startsWith("if")) {
      defaultName = field.getName().substring(2);
    }
    return generateAnnotationInfo(field.getAnnotation(annotation), defaultName, clazz, annotation);
  }

  public static AnnotationInfo generateAnnotationInfo(Method method, Class<?> clazz, Class<? extends Annotation> annotation) {
    return generateAnnotationInfo(method.getAnnotation(annotation), method.getName(), clazz, annotation);
  }

  public static AnnotationInfo generateAnnotationInfo(Annotation anno, String defaultName, Class<?> clazz, Class<? extends Annotation> annotation) {
    try {
      String value = (String) annotation.getMethod("value").invoke(anno);
      if(value.equals("*")) {
        return new AnnotationInfo(clazz.getAnnotation(JsonSerializable.class).caseType().encase(defaultName));
      } else {
        return new AnnotationInfo(clazz.getAnnotation(JsonSerializable.class).caseType().encase(value));
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return new AnnotationInfo("");
  }

  public static void initialize() {
    setSerializers(new ArrayList<>());

    DefaultSerializers.generateDefaultSerializers().forEach(getSerializers()::add);

    REFLECTIONS = new Reflections("", new SubTypesScanner(), new TypeAnnotationsScanner());
    REFLECTIONS.getTypesAnnotatedWith(JsonSerializable.class).forEach(clazz -> {
      Set<Field> myFields = Arrays.stream(clazz.getFields()).collect(Collectors.toSet());
      myFields.addAll(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toSet()));
      myFields.forEach(field -> field.setAccessible(true));
      Set<Pair<AnnotationInfo, Field>> myFieldMappings = myFields.stream().filter(field -> field.isAnnotationPresent(JsonField.class)).map(field -> Pair.of(generateAnnotationInfo(field, clazz, JsonField.class), field)).collect(Collectors.toSet());

      Set<Method> myMethods = Arrays.stream(clazz.getMethods()).collect(Collectors.toSet());
       myMethods.addAll(Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toSet()));
      Set<Pair<AnnotationInfo, Method>> getters = myMethods.stream().filter(method -> method.isAnnotationPresent(JsonGetter.class)).map(method -> Pair.of(generateAnnotationInfo(method, clazz, JsonField.class), method)).collect(Collectors.toSet());
      Set<Pair<AnnotationInfo, Method>> setters = myMethods.stream().filter(method -> method.isAnnotationPresent(JsonSetter.class)).map(method -> Pair.of(generateAnnotationInfo(method, clazz, JsonField.class), method)).collect(Collectors.toSet());
      getSerializers().add(new JsonSerializationMapping<>(clazz, myFieldMappings, getters, setters));
    });
  }

  public static <T> JsonSerializer<T> findSerializer(Class<T> clazz) {

    if(clazz == null) {
      return new JsonNullSerializer<>();
    }
    if(clazz.isPrimitive()) {
      return findSerializer((Class<T>) PRIMITIVES_TO_WRAPPERS.get(clazz));
    } else {
      Set<JsonObjectSerializer<?>> serializers = new HashSet<>();
      Optional<JsonSerializer<?>> serial = getSerializers().stream().filter(serializer -> serializer.getClazz() == clazz).findFirst();
      if(serial.isPresent()) {
        if(serial.get() instanceof JsonObjectSerializer) {
          serializers.add((JsonObjectSerializer<?>) serial.get());
        } else {
          return (JsonSerializer<T>) serial.get();
        }
      }
      Class<?> superclass = clazz.getSuperclass();
      if(superclass != null && !superclass.getCanonicalName().equals(Object.class.getCanonicalName())) {
        JsonSerializer<?> superserial = findSerializer(superclass);
        if(superserial instanceof JsonObjectSerializer) {
          serializers.add((JsonObjectSerializer<?>) superserial);
        } else {
          return (JsonSerializer<T>) superserial;
        }
      }
      Class<?>[] interfaces = clazz.getInterfaces();
      for(Class<?> inherited : interfaces) {
        JsonSerializer<?> interserial = findSerializer(inherited);
        if(interserial instanceof JsonObjectSerializer) {
          serializers.add((JsonObjectSerializer<?>) interserial);
        } else {
          return (JsonSerializer<T>) interserial;
        }
      }
      return new CompoundJsonObjectSerializer<>(clazz, serializers);
    }
  }

  public static <T> T deserialize(Type type, JsonElement element) {
    return (T) deserialize(ClassUtils.ofType(type), element, type);
  }
  public static <T> T deserialize(Class<T> clazz, JsonElement element, Type type) {
    if(element == JsonNull.INSTANCE) {
      return null;
    }
    return findSerializer(clazz).deserialize(Optional.empty(), element, type);
  }

  public static <T> JsonElement serialize(T instance) {
    if(instance == null) {
      return JsonNull.INSTANCE;
    }
    return findSerializer((Class<T>) instance.getClass()).serialize(instance);
  }


  public static List<JsonSerializer<?>> getSerializers() {
    return SERIALIZERS;
  }

  public static void setSerializers(List<JsonSerializer<?>> serializers) {
    JsonSerializing.SERIALIZERS = serializers;
  }
}
