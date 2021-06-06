package xyz.radiish.yuggsbot;

import com.mongodb.DB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import xyz.radiish.yuggsbot.command.BigBlueButtonCommand;
import xyz.radiish.yuggsbot.command.HelpCommand;
import xyz.radiish.yuggsbot.command.UwuifyCommand;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.storage.ProviderKey;
import xyz.radiish.zephyr.storage.ProviderKeyRegistry;

public class Yuggs extends Zephyr {

  public static final ProviderKey<YuggsUserRecord> USER_RECORD = ProviderKeyRegistry.USERS.register("yuggs", YuggsUserRecord::new);

  public Yuggs(JDA jda, DB database) {
    super(jda, database);

    jda.getPresence().setActivity(Activity.listening("more reasons to hate italy"));

    HelpCommand.register(getDispatcher());
    UwuifyCommand.register(getDispatcher());
    BigBlueButtonCommand.register(getDispatcher());

    jda.addEventListener(new YuggsListenerAdapter(this));
  }
}
