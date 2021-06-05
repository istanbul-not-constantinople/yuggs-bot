package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;

public class JsonSerializationMapping<T> extends JsonObjectSerializer<T> {
  private final Set<Pair<AnnotationInfo, Field>> fields;
  private final Set<Pair<AnnotationInfo, Method>> getters;
  private final Set<Pair<AnnotationInfo, Method>> setters;

  public JsonSerializationMapping(Class<T> clazz, Set<Pair<AnnotationInfo, Field>> fields, Set<Pair<AnnotationInfo, Method>> getters, Set<Pair<AnnotationInfo, Method>> setters) {
    super(clazz);
    this.fields = fields;
    this.getters = getters;
    this.setters = setters;
  }

  @Override
  public JsonObject serialize(T instance) {
    JsonObjectBuilder builder = new JsonObjectBuilder();
    getFields().forEach((entry) -> {
      try {
        builder.put(entry.getLeft().getPropertyName(), JsonSerializing.serialize(entry.getRight().get(instance)));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    });
    getGetters().forEach((entry) -> {
      try {
        builder.put(entry.getLeft().getPropertyName(), JsonSerializing.serialize(entry.getRight().invoke(instance)));
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    return builder.build();
  }

  @Override
  public T deserialize(Optional<T> instance, JsonElement element, Type type) {
    JsonObject obj = element.getAsJsonObject();
    getFields().forEach((entry) -> {
      try {
        Object b = JsonSerializing.deserialize(entry.getRight().getType(), obj.get(entry.getLeft().getPropertyName()), entry.getRight().getGenericType());
        entry.getRight().set(instance.get(), b);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    });
    getSetters().forEach((entry) -> {
      try {
        Object b = JsonSerializing.deserialize(entry.getRight().getReturnType(), obj.get(entry.getLeft().getPropertyName()), entry.getRight().getGenericReturnType());
        entry.getRight().invoke(instance.get(), b);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    return instance.get();
  }

  public Set<Pair<AnnotationInfo, Field>> getFields() {
    return fields;
  }

  public Set<Pair<AnnotationInfo, Method>> getGetters() {
    return getters;
  }

  public Set<Pair<AnnotationInfo, Method>> getSetters() {
    return setters;
  }

  @Override
  public String toString() {
    return "JsonSerializationMapping{" +
      "class=" + getClazz().getSimpleName() +
      '}';
  }
}
