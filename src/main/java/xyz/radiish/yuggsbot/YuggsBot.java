package xyz.radiish.yuggsbot;

import com.mongodb.MongoClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import xyz.radiish.yuggsbot.command.BigBlueButtonCommand;
import xyz.radiish.yuggsbot.command.HelpCommand;
import xyz.radiish.yuggsbot.command.UwuifyCommand;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.command.argument.user.UserSelection;

import static xyz.radiish.zephyr.command.CommandManager.argument;
import static xyz.radiish.zephyr.command.CommandManager.literal;
import static xyz.radiish.zephyr.command.argument.LiteralCollectionArgumentType.literals;
import static xyz.radiish.zephyr.command.argument.user.UserArgumentType.user;

public class YuggsBot extends Zephyr {

  public YuggsBot(JDA jda, MongoClient mongo) {
    super(jda, mongo);

    jda.getPresence().setActivity(Activity.listening("more reasons to hate italy"));

    HelpCommand.register(getDispatcher());
    UwuifyCommand.register(getDispatcher());
    BigBlueButtonCommand.register(getDispatcher());
  }
}
