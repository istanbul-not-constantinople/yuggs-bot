package xyz.radiish.yuggsbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.messaging.InformationBuilder;

import static xyz.radiish.zephyr.command.CommandManager.literal;


public class BigBlueButtonCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    LiteralCommandNode<CommandSource> node = dispatcher.register(literal("bigbluebutton").executes(context -> execute(context.getSource())));
    dispatcher.register(literal("bbb").redirect(node));
  }

  public static int execute(CommandSource source) {
    source.getMessage().getChannel().sendMessage(new MessageBuilder(new InformationBuilder()).setActionRows(
      ActionRow.of(Button.primary("big_blue", "boop me!"))
    ).build()).queue();
    return 1;
  }
}
