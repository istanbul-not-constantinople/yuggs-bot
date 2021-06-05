package xyz.radiish.zephyr.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public abstract class MutateArgumentType<T, U> implements ArgumentType<T> {
  private final ArgumentType<U> base;

  protected MutateArgumentType(ArgumentType<U> base) {
    this.base = base;
  }

  @Override
  public T parse(StringReader reader) throws CommandSyntaxException {
    return mutate(getBase().parse(reader));
  }

  public abstract T mutate(U parsed) throws CommandSyntaxException;

  public ArgumentType<U> getBase() {
    return base;
  }
}
