package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public interface JsonElementSerializer {
  void deserialize(JsonElement element, Type type);
  JsonElement serialize();
}
