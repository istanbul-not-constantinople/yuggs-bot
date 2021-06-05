package xyz.radiish.zephyr.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import xyz.radiish.zephyr.command.argument.rules.LimitRule;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.radiish.zephyr.command.argument.rules.LimitRule.ArgumentType.limit;

public abstract class SelectionArgumentType<T> implements ArgumentType<Selection<T>> {
  public static final SimpleCommandExceptionType INVALID_FORMAT = new SimpleCommandExceptionType(new LiteralMessage("invalid filter format"));
  public static final SimpleCommandExceptionType UNKNOWN_FILTER = new SimpleCommandExceptionType(new LiteralMessage("unknown selection filter"));
  public static final SimpleCommandExceptionType MISSING_SEPARATOR = new SimpleCommandExceptionType(new LiteralMessage("missing separator"));

  private final List<SelectionRuleArgumentType<? super T>> rules;
  private final List<SelectionRule<? super T>> defaultRules;
  private final boolean allowMultiple;

  public SelectionArgumentType(boolean allowMultiple, List<SelectionRuleArgumentType<? super T>> rules, List<SelectionRule<? super T>> defaultRules) {
    this.allowMultiple = allowMultiple;
    this.rules = rules;
    this.defaultRules = defaultRules;
    if(allowMultiple) {
      this.defaultRules.add(new LimitRule(1));
    } else {
      this.rules.add(limit());
    }
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    List<CompletableFuture<Pair<? extends SelectionRuleArgumentType<? super T>, Suggestions>>> futures = new ArrayList<>();
    for (SelectionRuleArgumentType<? super T> rule : getRules()) {
      CompletableFuture<Pair<? extends SelectionRuleArgumentType<? super T>, Suggestions>> thenApply = rule.listSuggestions(context, new SuggestionsBuilder(builder.getInput(), builder.getStart())).thenApply(suggestions -> Pair.of(rule, suggestions));
      futures.add(thenApply);
    }
    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply((unused) -> {
      futures.stream().map(CompletableFuture::join).collect(Collectors.toList()).forEach(pair -> pair.getRight().getList().forEach(suggestion -> builder.suggest(String.format("{%s=%s}", pair.getLeft().getKey(), suggestion.getText()))));
      return builder.build();
    });
  }

  @Override
  public Selection<T> parse(StringReader reader) throws CommandSyntaxException {
    if(!reader.canRead()) {
      return selectFromDefaults(new ArrayList<>());
    }
    if(reader.peek() == '@') {
      reader.skip();
      if(reader.peek() == '{') {
        reader.skip();
        List<SelectionRule<? super T>> rules = new ArrayList<>();
        for(int i = 0; reader.canRead() && reader.peek() != '}'; i++) {
          rules.add(parseRule(reader, i >= 1));
        }
        if(reader.peek() == '}') {
          return selectFromDefaults(rules);
        }
      }
    }
    throw INVALID_FORMAT.create();
  }

  public Selection<T> selectFromDefaults(List<SelectionRule<? super T>> rules) {
    return select(Stream.of(rules, getDefaultRules()).flatMap(Collection::stream).collect(Collectors.toList()));
  }

  public abstract Selection<T> select(List<SelectionRule<? super T>> rules);

  private SelectionRule<? super T> parseRule(StringReader reader, boolean separator) throws CommandSyntaxException {
    if(!reader.canRead()) {
      throw INVALID_FORMAT.create();
    }
    if(separator) {
      if(reader.peek() == ',') {
        reader.skip();
      } else {
        throw MISSING_SEPARATOR.create();
      }
    }
    String key = reader.readStringUntil('=');
    reader.skip();
    Optional<SelectionRuleArgumentType<? super T>> optional = getRules().stream().filter(rule -> rule.getKey().equals(key)).findFirst();
    if(optional.isPresent()) {
      return optional.get().parse(reader);
    } else {
      throw UNKNOWN_FILTER.create();
    }
  }

  public List<SelectionRuleArgumentType<? super T>> getRules() {
    return rules;
  }

  public List<SelectionRule<? super T>> getDefaultRules() {
    return defaultRules;
  }

  public boolean isAllowedMultiple() {
    return allowMultiple;
  }
}
