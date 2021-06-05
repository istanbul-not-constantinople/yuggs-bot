package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import java.lang.reflect.Type;
import java.util.Optional;

public class JsonNullSerializer<T> extends JsonSerializer<T> {
  public JsonNullSerializer() {
    super(null);
  }

  @Override
  public JsonElement serialize(T instance) {
    return JsonNull.INSTANCE;
  }

  @Override
  public T deserialize(Optional<T> instance, JsonElement element, Type type) {
    return null;
  }
}
