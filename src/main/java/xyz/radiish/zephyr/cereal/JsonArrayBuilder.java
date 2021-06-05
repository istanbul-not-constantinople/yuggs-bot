package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonArrayBuilder extends JsonBuilder<JsonArray> {
  private final List<JsonElement> elements;

  public JsonArrayBuilder() {
    elements = new ArrayList<>();
  }

  public JsonArrayBuilder add(JsonElement value) {
    getElements().add(value);
    return this;
  }

  public JsonArrayBuilder add(int value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(float value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(double value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(short value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(byte value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(char value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(boolean value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(String value) {
    return add(new JsonPrimitive(value));
  }

  public JsonArrayBuilder add(JsonBuilder<?> value) {
    return add(value.build());
  }

  public JsonArrayBuilder addAll(List<JsonElement> value) {
    getElements().addAll(value);
    return this;
  }

  public JsonArrayBuilder addAllInts(List<Integer> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllFloats(List<Float> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllDoubles(List<Double> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllShorts(List<Short> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllBytes(List<Byte> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllCharacters(List<Character> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllBooleans(List<Boolean> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllStrings(List<String> value) {
    return addAll(value.stream().map(JsonPrimitive::new).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addAllBuilders(List<JsonBuilder<?>> value) {
    return addAll(value.stream().map(JsonBuilder::build).collect(Collectors.toList()));
  }

  public JsonArrayBuilder addJsonArray(JsonArray array) {
    List<JsonElement> list = new ArrayList<>();
    for(int i = 0; i < array.size(); i++) {
      list.add(array.get(i));
    }
    return addAll(list);
  }

  @Override
  public JsonArray build() {
    JsonArray array = new JsonArray();
    getElements().forEach(array::add);
    return array;
  }

  public List<JsonElement> getElements() {
    return elements;
  }
}
