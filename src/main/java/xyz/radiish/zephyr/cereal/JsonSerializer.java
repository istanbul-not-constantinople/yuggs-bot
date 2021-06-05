package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonSerializer<T> {
  private final Class<? super T> clazz;

  public JsonSerializer(Class<? super T> clazz) {
    this.clazz = clazz;
  }

  public abstract JsonElement serialize(T instance);

  public abstract T deserialize(Optional<T> instance, JsonElement element, Type type);

  public Class<? super T> getClazz() {
    return clazz;
  }

  public T newInstance() {
    try {
      return (T) getClazz().getDeclaredConstructor().newInstance();
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
    return null;
  }
}
