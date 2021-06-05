package xyz.radiish.zephyr.command.argument.rules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import xyz.radiish.zephyr.command.argument.SelectionRule;
import xyz.radiish.zephyr.command.argument.SelectionRuleArgumentType;

import java.util.concurrent.CompletableFuture;

public class SnowflakeRule<T extends ISnowflake> extends SelectionRule<T> {
  public static class ArgumentType extends SelectionRuleArgumentType<ISnowflake> {
    public ArgumentType() {
      super("snowflake");
    }

    @Override
    public SelectionRule<ISnowflake> parse(StringReader reader) throws CommandSyntaxException {
      long uid = reader.readLong();
      return SelectionRule.ofPredicate(flake -> flake.getIdLong() == uid);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      builder.suggest("843475499896012830");
      return builder.buildFuture();
    }

    public static ArgumentType snowflake() {
      return new ArgumentType();
    }
  }


  public SnowflakeRule(long uid) {
    super(SelectionRule.unary(flake -> flake.getIdLong() == uid));
  }
}