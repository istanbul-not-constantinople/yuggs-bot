package xyz.radiish.zephyr;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mongodb.MongoClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import xyz.radiish.zephyr.command.ZephyrListenerAdapter;
import xyz.radiish.zephyr.cereal.JsonObjectBuilder;
import xyz.radiish.zephyr.cereal.JsonSerializing;
import xyz.radiish.zephyr.storage.UserRecord;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Zephyr {

  private final CommandDispatcher<CommandSource> dispatcher;
  private Pattern prefix;
  private JDA jda;
  private MongoClient mongo;
  private Map<User, UserRecord> userRecords;

  public Zephyr(JDA jda, MongoClient mongo) {
    dispatcher = new CommandDispatcher<>();
    setPrefix(";");
    setJda(jda);
    setMongo(mongo);
    setUserRecords(new HashMap<>());

    getJda().addEventListener(new ZephyrListenerAdapter(this));

    mongo.getUsedDatabases();
  }

  public CommandDispatcher<CommandSource> getDispatcher() {
    return dispatcher;
  }

  public Pattern getPrefix() {
    return prefix;
  }

  public void setPrefix(Pattern prefix) {
    this.prefix = prefix;
  }

  public void setPrefix(String prefix) {
    setPrefix(Pattern.compile("(" + prefix + ")(.*)"));
  }

  public JDA getJda() {
    return jda;
  }

  public void setJda(JDA jda) {
    this.jda = jda;
  }

  public Map<User, UserRecord> getUserRecords() {
    return userRecords;
  }

  public void setUserRecords(Map<User, UserRecord> userRecords) {
    this.userRecords = userRecords;
  }

  public MongoClient getMongo() {
    return mongo;
  }

  public void setMongo(MongoClient mongo) {
    this.mongo = mongo;
  }
}
