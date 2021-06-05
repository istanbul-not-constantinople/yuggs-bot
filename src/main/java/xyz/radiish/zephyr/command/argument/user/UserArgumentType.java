package xyz.radiish.zephyr.command.argument.user;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.CommandManager;
import xyz.radiish.zephyr.command.argument.Selection;
import xyz.radiish.zephyr.command.argument.SelectionArgumentType;
import xyz.radiish.zephyr.command.argument.SelectionRule;
import xyz.radiish.zephyr.command.argument.SelectionRuleArgumentType;
import xyz.radiish.zephyr.command.argument.rules.LimitRule;
import xyz.radiish.zephyr.command.argument.rules.SnowflakeRule;
import xyz.radiish.zephyr.command.registry.ArgumentTypeEntry;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

import static xyz.radiish.zephyr.command.argument.rules.LimitRule.ArgumentType.limit;
import static xyz.radiish.zephyr.command.argument.rules.SnowflakeRule.ArgumentType.snowflake;

public class UserArgumentType extends SelectionArgumentType<User> {
  public static final ArgumentTypeEntry<UserArgumentType> USER = CommandManager.register(new ArgumentTypeEntry<>(
    UserArgumentType::user,
    "user"
  ));

  public static final ArgumentTypeEntry<UserArgumentType> USERS = CommandManager.register(new ArgumentTypeEntry<>(
    UserArgumentType::users,
    "users"
  ));

  public UserArgumentType(boolean allowMultiple, List<SelectionRuleArgumentType<? super User>> rules, List<SelectionRule<? super User>> defaultRules) {
    super(allowMultiple, rules, defaultRules);
  }

  @Override
  public Selection<User> select(List<SelectionRule<? super User>> selectionRules) {
    return new UserSelection(selectionRules);
  }

  @Override
  public Selection<User> parse(StringReader reader) throws CommandSyntaxException {
    try {
      return super.parse(reader);
    } catch(CommandSyntaxException exception) {
      if(reader.canRead() && reader.peek() == '<') {
        String unquoted = reader.readStringUntil('>') + '>';
        Matcher user = Message.MentionType.USER.getPattern().matcher(unquoted);
        if(user.matches()) {
          long uid = Long.parseLong(user.group(1));
          return selectFromDefaults(Collections.singletonList(new SnowflakeRule<>(uid)));
        } else if(Message.MentionType.EVERYONE.getPattern().matcher(unquoted).matches() || Message.MentionType.HERE.getPattern().matcher(unquoted).matches()) {
          return new UserSelection(new ArrayList<>());
        }
      }
    }
    throw SelectionArgumentType.INVALID_FORMAT.create();
  }

  public static UserArgumentType users() {
    return new UserArgumentType(true, Arrays.asList(snowflake(), limit()), new ArrayList<>());
  }

  public static UserArgumentType user() {
    return new UserArgumentType(false, Collections.singletonList(snowflake()), Collections.singletonList(new LimitRule(1)));
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    super.listSuggestions(context, builder);
    if(isAllowedMultiple()) {
      builder.suggest("@everyone").suggest("@here");
    }
    builder.suggest("@member");
    return builder.buildFuture();
  }
}
