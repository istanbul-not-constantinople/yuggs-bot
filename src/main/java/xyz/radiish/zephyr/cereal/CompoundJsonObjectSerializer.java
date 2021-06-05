package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CompoundJsonObjectSerializer<T> extends JsonObjectSerializer<T> {

  private final Set<JsonObjectSerializer<T>> serializers;

  public CompoundJsonObjectSerializer(Class<T> clazz, Set<JsonObjectSerializer<?>> serializers) {
    super(clazz);
    this.serializers = serializers.stream().map(b -> (JsonObjectSerializer<T>) b).collect(Collectors.toSet());

  }

  @Override
  public JsonObject serialize(T instance) {
    JsonObjectBuilder builder = new JsonObjectBuilder();
    serializers.forEach(serializer -> {
      JsonObject jso = serializer.serialize(instance);
      builder.merge(jso);
    });
    return builder.build();
  }

  @Override
  public T deserialize(Optional<T> instance, JsonElement element, Type type) {
    T newInstance = instance.orElse(newInstance());
    serializers.forEach(cereal -> cereal.deserialize(Optional.ofNullable(newInstance), element, type));
    return newInstance;
  }
}
