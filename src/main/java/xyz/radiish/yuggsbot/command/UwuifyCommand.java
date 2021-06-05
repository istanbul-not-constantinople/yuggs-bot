package xyz.radiish.yuggsbot.command;

import com.mojang.brigadier.CommandDispatcher;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.messaging.InformationBuilder;
import xyz.radiish.zephyr.util.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static xyz.radiish.zephyr.command.CommandManager.argument;
import static xyz.radiish.zephyr.command.CommandManager.literal;

public class UwuifyCommand {
  public static final List<String> VERBS = Arrays.asList("nuzzles", "rubs", "pounces on");
  public static final List<String> NOUNS = Arrays.asList("your bulgy wulgy", "my baby tail", "your warm belly", "daddy waddy");

  public static List<UnaryOperator<String>> generateMorphs(int severity) {
    return Arrays.asList(
      TextUtils.WordMorph.of(word -> {
        Random random = new Random();
        double chance = random.nextDouble();
        if(chance < 0.02 * severity) {
          return String.format("uwu %s", word);
        } else if(chance < 0.025 * severity) {
          return String.format("%s %s %s", word, VERBS.get(random.nextInt(VERBS.size())), NOUNS.get(random.nextInt(NOUNS.size())));
        } else if(chance < 0.0275 * severity) {
          return String.format("%s (punish me please)", word);
        }
        return word;
      }),
      TextUtils.PatternMorph.of("[lr]",       "w"),
      TextUtils.PatternMorph.of("t(['])?s",   "sh"),
      TextUtils.PatternMorph.of("([aeiou])u", "$1u"),
      TextUtils.PatternMorph.of("([f])u",     "$1wu"),
      TextUtils.PatternMorph.of("[:;][)\\]}]","uwu")
    );
  }


  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(literal("uwuify")
      .then(argument("text", string())
        .executes(context -> execute(context.getSource(), context.getArgument("text", String.class), 5))
        .then(argument("strength", integer(1, 25))
          .executes(context -> execute(context.getSource(), context.getArgument("text", String.class), context.getArgument("strength", int.class))))));
  }

  public static int execute(CommandSource source, String text, int severity) {
    List<UnaryOperator<String>> textMorphs = generateMorphs(severity);
    for(UnaryOperator<String> morph : textMorphs) {
      text = morph.apply(text);
    }
    source.getMessage().getChannel().sendMessage(new InformationBuilder().setDescription(text).setTitle("merry christmas ya filthy animal").build()).queue();
    return severity;
  }
}
