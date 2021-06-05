package xyz.radiish.zephyr.cereal;

import xyz.radiish.zephyr.util.TextUtils;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum CaseType {
  SNAKE_CASE(TextUtils.CharacterMorph.of(letter -> Character.isUpperCase(letter) ? "_" + Character.toLowerCase(letter) : letter.toString()));

  private final UnaryOperator<String> encasing;

  public UnaryOperator<String> getEncasing() {
    return encasing;
  }

  public String encase(String input) {
    return getEncasing().apply(input);
  }

  CaseType(UnaryOperator<String> encasing) {
    this.encasing = encasing;
  }
}
