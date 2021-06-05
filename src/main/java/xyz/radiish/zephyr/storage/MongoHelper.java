package xyz.radiish.zephyr.storage;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.jetbrains.annotations.Nullable;

public class MongoHelper {
  public DBObject jsonToObject(JsonObject json, @Nullable String id) {
    BasicDBObject mongo = new BasicDBObject();
    if(json.has("_id")) {
      mongo.append("_id", json.get("_id").toString());
    } else if(id != null) {
      mongo.append("_id", id);
    } else {
      throw new IllegalArgumentException("the JsonObject has no \"_id\" property and the id argument was null.");
    }
    json.entrySet().forEach(entry -> mongo.append(entry.getKey(), entry.getValue()));
    return mongo;
  }
}
