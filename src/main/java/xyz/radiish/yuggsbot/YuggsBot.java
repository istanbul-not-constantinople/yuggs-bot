package xyz.radiish.yuggsbot;

import com.mongodb.DB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import xyz.radiish.yuggsbot.command.BigBlueButtonCommand;
import xyz.radiish.yuggsbot.command.HelpCommand;
import xyz.radiish.yuggsbot.command.UwuifyCommand;
import xyz.radiish.zephyr.Zephyr;

import static xyz.radiish.zephyr.command.CommandManager.literal;
import static xyz.radiish.zephyr.command.argument.LiteralCollectionArgumentType.literals;

public class YuggsBot extends Zephyr {

  public YuggsBot(JDA jda, DB database) {
    super(jda, database);

    jda.getPresence().setActivity(Activity.listening("more reasons to hate italy"));

    HelpCommand.register(getDispatcher());
    UwuifyCommand.register(getDispatcher());
    BigBlueButtonCommand.register(getDispatcher());

    jda.addEventListener(new YuggsListenerAdapter(this));
  }
}
