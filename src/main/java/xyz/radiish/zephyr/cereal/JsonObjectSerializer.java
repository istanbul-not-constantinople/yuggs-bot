package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonObject;

public abstract class JsonObjectSerializer<T> extends JsonSerializer<T> {
  public JsonObjectSerializer(Class<T> clazz) {
    super(clazz);
  }

  @Override
  public abstract JsonObject serialize(T instance);
}
