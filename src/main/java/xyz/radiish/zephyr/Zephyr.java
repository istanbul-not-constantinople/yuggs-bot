package xyz.radiish.zephyr;

import com.mojang.brigadier.CommandDispatcher;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.cereal.JsonSerializing;
import xyz.radiish.zephyr.command.ZephyrListenerAdapter;
import xyz.radiish.zephyr.storage.MongoHelper;
import xyz.radiish.zephyr.storage.UserRecord;
import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.Map;
import java.util.regex.Pattern;

public class Zephyr {

  private final CommandDispatcher<CommandSource> dispatcher;
  private Pattern prefix;
  private final JDA jda;
  private final DB database;
  private final DBCollection users;
  private final DBCollection guilds;

  public Zephyr(JDA jda, DB database) {
    dispatcher = new CommandDispatcher<>();
    setPrefix(";");
    this.jda = jda;
    this.database = database;
    users = this.database.getCollection("users");
    guilds = this.database.getCollection("guilds");

    getJda().addEventListener(new ZephyrListenerAdapter(this));
  }

  public UserRecord fetchUserRecord(User user) {
    return MongoHelper.fetch(users, user.getIdLong(), UserRecord.class, () -> new UserRecord(user));
  }

  public void updateUserRecord(UserRecord record) {
    MongoHelper.update(users, record, record.getId());
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

  public DB getDatabase() {
    return database;
  }

  public DBCollection getGuilds() {
    return guilds;
  }

  public DBCollection getUsers() {
    return users;
  }
}
