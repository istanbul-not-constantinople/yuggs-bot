package xyz.radiish.zephyr.command.registry;

import com.mojang.brigadier.arguments.ArgumentType;

import java.util.function.Supplier;

public class ArgumentTypeEntry<T extends ArgumentType<?>> {
  private Supplier<T> factory;
  private String name;

  public ArgumentTypeEntry(Supplier<T> factory, String name) {
    this.factory = factory;
    this.name = name;
  }

  public ArgumentTypeEntry() {
    this(() -> null, "");
  }

  public ArgumentTypeEntry<T> setName(String name) {
    this.name = name;
    return this;
  }

  public <U extends ArgumentType<?>> ArgumentTypeEntry<U> setFactory(Supplier<U> factory) {
    this.factory = (Supplier<T>) factory;
    return (ArgumentTypeEntry<U>) this;
  }

  public Supplier<T> getFactory() {
    return factory;
  }

  public String getName() {
    return name;
  }
}
