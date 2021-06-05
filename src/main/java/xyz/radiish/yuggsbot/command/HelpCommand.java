package xyz.radiish.yuggsbot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xyz.radiish.zephyr.command.CommandManager;
import xyz.radiish.zephyr.command.registry.ArgumentTypeEntry;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.messaging.InformationBuilder;

import static xyz.radiish.zephyr.command.CommandManager.argument;
import static xyz.radiish.zephyr.command.CommandManager.literal;
import static xyz.radiish.zephyr.command.argument.LiteralCollectionArgumentType.literals;

public class HelpCommand {
  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(literal("help")
      .then(literal("argument")
        .then(argument("key", literals(CommandManager.ARGUMENT_TYPES.keySet().toArray(new String[0])))
          .executes(context -> helpWithArgument(context, CommandManager.ARGUMENT_TYPES.get(context.getArgument("key", String.class)))))
        ));
  }

  public static int helpWithArgument(CommandContext<CommandSource> context, ArgumentTypeEntry<?> entry) {
    entry.getFactory().get().listSuggestions(context, new SuggestionsBuilder(context.getInput(), 0)).thenAccept(suggestions -> {
      InformationBuilder builder = new InformationBuilder()
        .appendDescription("```md\n");
      suggestions.getList().forEach(suggestion -> builder.appendDescription(suggestion.getText() + "\n"));
      context.getSource().getMessage().reply(builder
        .appendDescription("```")
        .build()).mentionRepliedUser(false).queue();
    });
    return 1;
  }

  public static void helpWithCommand() {

  }
}
