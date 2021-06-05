package xyz.radiish.zephyr.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;

public abstract class SelectionRuleArgumentType<T> implements ArgumentType<SelectionRule<T>> {
  private final String key;

  public SelectionRuleArgumentType(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
