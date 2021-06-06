package xyz.radiish.zephyr.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.*;
import org.bson.BSONObject;
import xyz.radiish.zephyr.cereal.JsonArrayBuilder;
import xyz.radiish.zephyr.cereal.JsonObjectBuilder;
import xyz.radiish.zephyr.cereal.JsonSerializing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MongoHelper {
  private static Object jsonElementToObject(JsonElement element) {
    if(element.isJsonObject()) {
      return MongoHelper.jsonToObject(element.getAsJsonObject());
    } else if(element.isJsonArray()) {
      return StreamSupport.stream(element.getAsJsonArray().spliterator(), false).map(MongoHelper::jsonElementToObject).collect(Collectors.toList());
    } else if(element.isJsonNull()) {
      return JsonNull.INSTANCE;
    } else if(element.getAsJsonPrimitive().isBoolean()) {
      return element.getAsBoolean();
    } else if(element.getAsJsonPrimitive().isString()) {
      return element.getAsString();
    } else {
      return element.getAsNumber();
    }
  }

  public static DBObject jsonToObject(JsonObject json) {
    BasicDBObject mongo = new BasicDBObject();
    json.entrySet().forEach(entry -> mongo.append(entry.getKey(), jsonElementToObject(entry.getValue())));
    return mongo;
  }

  private static JsonElement objectToJsonElement(Object object) {
    if (object == null) {
      return JsonNull.INSTANCE;
    } else if(object instanceof LazyDBObject || object instanceof BasicDBObject) {
      return objectToJson((DBObject) object);
    } else  if(object instanceof List<?>) {
      return new JsonArrayBuilder().addAll(((List<?>) object).stream().map(MongoHelper::objectToJsonElement).collect(Collectors.toList())).build();
    } else if(object instanceof Number) {
      return new JsonPrimitive((Number) object);
    } else if(object instanceof Boolean) {
      return new JsonPrimitive((Boolean) object);
    } else {
      return new JsonPrimitive((String) object);
    }
  }

  public static JsonObject objectToJson(DBObject object) {
    JsonObjectBuilder builder = new JsonObjectBuilder();
    object.toMap().forEach((key, value) -> builder.put((String) key, objectToJsonElement(value)));
    return builder.build();
  }

  public static <T> T fetch(DBCollection collection, Object id, Class<?> clazz, Supplier<T> supplier) {
    DBObject object = collection.find(new BasicDBObject("_id", id)).one();
    if(object != null) {
      return JsonSerializing.deserialize(clazz, MongoHelper.objectToJson(object));
    } else {
      return supplier.get();
    }
  }

  public static void update(DBCollection collection, Object record, Object id) {
    collection.update(new BasicDBObject("_id", id), MongoHelper.jsonToObject(JsonSerializing.serialize(record).getAsJsonObject()), true, false);
  }
}
