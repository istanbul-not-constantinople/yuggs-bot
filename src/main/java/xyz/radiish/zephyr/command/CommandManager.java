package xyz.radiish.zephyr.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xyz.radiish.zephyr.command.registry.ArgumentTypeEntry;
import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandManager {
  public static final Map<String, ArgumentTypeEntry<?>> ARGUMENT_TYPES = new HashMap<>();

  public static <S> CompletableFuture<Suggestions> mergeSuggestions(CompletableFuture<Suggestions> a, CompletableFuture<Suggestions> b) {
    return a.thenCompose(a1 -> b.thenApply(b1 -> new Suggestions(a1.getRange(), Stream.of(a1, b1).flatMap(suggestions -> suggestions.getList().stream()).collect(Collectors.toList()))));
  }

  public static <T extends ArgumentTypeEntry<?>> T register(T entry) {
    ARGUMENT_TYPES.put(entry.getName(), entry);
    return entry;
  }

  public static LiteralArgumentBuilder<CommandSource> literal(String literal) {
    return LiteralArgumentBuilder.literal(literal);
  }
  public static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
    return RequiredArgumentBuilder.argument(name, type);
  }

}
