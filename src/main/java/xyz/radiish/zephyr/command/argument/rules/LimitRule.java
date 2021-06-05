package xyz.radiish.zephyr.command.argument.rules;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xyz.radiish.zephyr.command.argument.SelectionRule;
import xyz.radiish.zephyr.command.argument.SelectionRuleArgumentType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitRule extends SelectionRule<Object> {
  public static class ArgumentType extends SelectionRuleArgumentType<Object> {

    public ArgumentType() {
      super("limit");
    }

    public static ArgumentType limit() {
      return new ArgumentType();
    }

    @Override
    public SelectionRule<Object> parse(StringReader reader) throws CommandSyntaxException {
      AtomicInteger threshold = new AtomicInteger(reader.readInt());
      return SelectionRule.ofPredicate(element -> threshold.getAndDecrement() >= 1);
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext context, SuggestionsBuilder builder) {
      builder.suggest("1");
      return builder.buildFuture();
    }
  }


  public LimitRule(int integer) {
    this(new AtomicInteger(integer));
  }

  public LimitRule(AtomicInteger threshold) {
    super(SelectionRule.unary(element -> threshold.getAndDecrement() >= 1));
  }
}
