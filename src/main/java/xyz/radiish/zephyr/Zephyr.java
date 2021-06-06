package xyz.radiish.zephyr;

import com.mojang.brigadier.CommandDispatcher;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.ZephyrListenerAdapter;
import xyz.radiish.zephyr.storage.*;
import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Zephyr {
  public static final ProviderKey<ZephyrUserRecord> USER_RECORD = ProviderKeyRegistry.USERS.register("zephyr", ZephyrUserRecord::new);

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

  public RecordProvider fetchUserRecord(User user) {
    RecordProvider provider = MongoHelper.fetch(users, user.getIdLong(), RecordProvider.class, () -> new RecordProvider(ProviderKeyRegistry.USERS, user.getIdLong()));
    provider.setClient(this);
    return provider;
  }

  public void updateUserRecord(RecordProvider record) {
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
