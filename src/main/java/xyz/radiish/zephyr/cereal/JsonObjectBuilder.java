package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObjectBuilder {
  private final Map<String, JsonElement> entries;

  public JsonObjectBuilder() {
    entries = new HashMap<>();
  }

  public JsonObjectBuilder put(String key, JsonElement value) {
    entries.put(key, value);
    return this;
  }

  public JsonObjectBuilder put(String key, int value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, float value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, double value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, short value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, byte value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, char value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, boolean value) {
    return put(key, new JsonPrimitive(value));
  }

  public JsonObjectBuilder put(String key, String value) {
    return put(key, new JsonPrimitive(value));
  }
  
  public JsonObjectBuilder put(String key, JsonBuilder<?> value) {
    return put(key, value.build());
  }

  public JsonObjectBuilder put(String key, List<JsonElement> value) {
    JsonArray array = new JsonArray();
    value.forEach(array::add);
    return put(key, array);
  }

  public JsonObjectBuilder merge(JsonObject object) {
    object.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
    return this;
  }

  public JsonObject build() {
    JsonObject object = new JsonObject();
    entries.forEach(object::add);
    return object;
  }
}
