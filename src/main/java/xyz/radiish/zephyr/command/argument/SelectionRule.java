package xyz.radiish.zephyr.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class SelectionRule<T> {
  private final UnaryOperator<Stream<T>> operator;

  public SelectionRule(UnaryOperator<Stream<T>> operator) {
    this.operator = operator;
  }

  public static <T> SelectionRule<T> ofPredicate(Predicate<T> predicate) {
    return new SelectionRule<>(unary(predicate));
  }

  public static <T> UnaryOperator<Stream<T>> unary(Predicate<T> predicate) {
    return stream -> stream.filter(predicate);
  }

  public <U extends T> Stream<U> apply(Stream<U> stream) {
    return (Stream<U>) operator.apply((Stream<T>) stream);
  }
}
