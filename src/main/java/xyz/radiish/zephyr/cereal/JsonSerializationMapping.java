package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class JsonSerializationMapping<T> extends JsonObjectSerializer<T> {
  private final List<Pair<AnnotationInfo, Field>> fields;
  private final List<Pair<AnnotationInfo, Method>> getters;
  private final List<Pair<AnnotationInfo, Method>> setters;

  public JsonSerializationMapping(Class<T> clazz, List<Pair<AnnotationInfo, Field>> fields, List<Pair<AnnotationInfo, Method>> getters, List<Pair<AnnotationInfo, Method>> setters) {
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
        Object b = JsonSerializing.deserialize(entry.getRight().getParameterTypes()[0], obj.get(entry.getLeft().getPropertyName()), entry.getRight().getGenericParameterTypes()[0]);
        entry.getRight().invoke(instance.get(), b);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    return instance.get();
  }

  public List<Pair<AnnotationInfo, Field>> getFields() {
    return fields;
  }

  public List<Pair<AnnotationInfo, Method>> getGetters() {
    return getters;
  }

  public List<Pair<AnnotationInfo, Method>> getSetters() {
    return setters;
  }
}
