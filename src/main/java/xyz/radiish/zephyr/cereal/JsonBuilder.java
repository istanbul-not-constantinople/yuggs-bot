package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;

public abstract class JsonBuilder<T extends JsonElement> {
  public abstract T build();
}
