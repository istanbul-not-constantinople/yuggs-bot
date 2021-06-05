package xyz.radiish.yuggsbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.radiish.yuggsbot.YuggsBot;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.messaging.InformationBuilder;
import xyz.radiish.zephyr.storage.UserRecord;

import static xyz.radiish.zephyr.command.CommandManager.literal;


public class BigBlueButtonCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    LiteralCommandNode<CommandSource> node = dispatcher.register(literal("bigbluebutton").executes(context -> execute(context.getSource())));
    dispatcher.register(literal("bbb").redirect(node));
  }

  public static Message messageFor(UserRecord record) {
    return new MessageBuilder(new InformationBuilder().appendDescription(String.format("you have clicked the button %s time(s)", record.getButtonClicks()))).setActionRows(
      ActionRow.of(Button.primary("big_blue", "boop me!"))
    ).build();
  }

  public static int execute(CommandSource source) {
    source.getMessage().reply(messageFor(source.fetchUserRecord())).mentionRepliedUser(false).queue();
    return 1;
  }
}
