package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Optional;

public class JsonClassSerializer<T extends JsonElementSerializer> extends JsonSerializer<T> {

  public JsonClassSerializer(Class<? super T> clazz) {
    super(clazz);
  }

  @Override
  public JsonElement serialize(T instance) {
    return instance.serialize();
  }

  @Override
  public T deserialize(Optional<T> instance, JsonElement element, Type type) {
    T newInstance = instance.orElse(newInstance());
    newInstance.deserialize(element, type);
    return newInstance;
  }
}
