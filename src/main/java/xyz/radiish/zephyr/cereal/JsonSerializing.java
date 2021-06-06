package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import xyz.radiish.zephyr.util.TypeUtils;

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

  public static void initialize() {
    setSerializers(new ArrayList<>());

    DefaultSerializers.generateDefaultSerializers().forEach(getSerializers()::add);

    REFLECTIONS = new Reflections("", new SubTypesScanner(), new TypeAnnotationsScanner());
    REFLECTIONS.getTypesAnnotatedWith(JsonSerializable.class).forEach(clazz -> {
      if(TypeUtils.highestCommonFactor(Set.of(clazz, JsonElementSerializer.class)) == JsonElementSerializer.class) {
        getSerializers().add(new JsonClassSerializer<>(clazz));
      } else {
        Set<Field> myFields = Arrays.stream(clazz.getFields()).collect(Collectors.toSet());
        myFields.addAll(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toSet()));
        myFields.forEach(field -> field.setAccessible(true));
        List<Pair<AnnotationInfo, Field>> fieldMappings = new ArrayList<>(myFields.stream().filter(field -> field.isAnnotationPresent(JsonField.class)).map(field -> {
          JsonField annotation = field.getAnnotation(JsonField.class);
          return Pair.of(AnnotationInfo.of(annotation.value(), field.getName(), field.getDeclaringClass().getAnnotation(JsonSerializable.class).caseType(), annotation.priority()), field);
        }).collect(Collectors.toSet()));
        fieldMappings.sort(Comparator.comparingInt(pair -> pair.getLeft().getPriority()));

        Set<Method> myMethods = Arrays.stream(clazz.getMethods()).collect(Collectors.toSet());
        myMethods.addAll(Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toSet()));

        List<Pair<AnnotationInfo, Method>> getters = new ArrayList<>(myMethods.stream().filter(method -> method.isAnnotationPresent(JsonGetter.class)).map(method -> {
          JsonGetter annotation = method.getAnnotation(JsonGetter.class);
          return Pair.of(AnnotationInfo.of(annotation.value(), method.getName(), method.getDeclaringClass().getAnnotation(JsonSerializable.class).caseType(), annotation.priority()), method);
        }).collect(Collectors.toSet()));
        getters.sort(Comparator.comparingInt(pair -> pair.getLeft().getPriority()));
        List<Pair<AnnotationInfo, Method>> setters = new ArrayList<>(myMethods.stream().filter(method -> method.isAnnotationPresent(JsonSetter.class)).map(method -> {
          JsonSetter annotation = method.getAnnotation(JsonSetter.class);
          return Pair.of(AnnotationInfo.of(annotation.value(), method.getName(), method.getDeclaringClass().getAnnotation(JsonSerializable.class).caseType(), annotation.priority()), method);
        }).collect(Collectors.toSet()));
        setters.sort(Comparator.comparingInt(pair -> pair.getLeft().getPriority()));

        getSerializers().add(new JsonSerializationMapping<>(clazz, fieldMappings, getters, setters));
      }
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
        if(serial.get() instanceof JsonObjectSerializer<?>) {
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
      return new CompoundJsonObjectSerializer<>(clazz, serializers.stream().map(serializer -> (JsonObjectSerializer<? super T>) serializer).collect(Collectors.toSet()));
    }
  }

  public static <T> T deserialize(Type type, JsonElement element) {
    return (T) deserialize(TypeUtils.ofType(type), element, type);
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
