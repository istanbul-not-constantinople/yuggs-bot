package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Optional;

@JsonSerializable
public class TypedObject<T> implements JsonElementSerializer {
  private Object value;
  private Type type;

  public TypedObject(T value, Type type) {
    this.value  = value;
    this.type = type;
  }

  public TypedObject() {
    this.value = null;
    this.type = null;
  }

  public static <T> TypedObject<T> of(T value) {
    return new TypedObject<>(value, value.getClass());
  }

  @Override
  public void deserialize(JsonElement element, Type type) {
    this.type = DefaultSerializers.TYPE_SERIALIZER.deserialize(Optional.empty(), element.getAsJsonObject().get("type"), Type.class);
    value = JsonSerializing.deserialize(getType(), element.getAsJsonObject().get("value"));
  }

  @Override
  public JsonElement serialize() {
    return new JsonObjectBuilder().put("type", DefaultSerializers.TYPE_SERIALIZER.serialize(getType())).put("value", JsonSerializing.serialize(getValue())).build();
  }

  public T getValue() {
    return (T) value;
  }

  public Type getType() {
    return type;
  }
}
