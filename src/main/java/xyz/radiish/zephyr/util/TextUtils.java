package xyz.radiish.zephyr.util;

import org.intellij.lang.annotations.Language;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextUtils {

  public static class PatternMorph implements UnaryOperator<String> {
    private final Pattern pattern;
    private final String replacement;

    public PatternMorph(Pattern pattern, String replacement) {
      this.pattern = pattern;
      this.replacement = replacement;
    }

    public static PatternMorph of(@Language("RegExp") String pattern, String replacement) {
      return new PatternMorph(Pattern.compile(pattern, Pattern.MULTILINE), replacement);
    }

    @Override
    public String apply(String s) {
      Matcher matcher = pattern.matcher(s);
      return matcher.replaceAll(replacement);
    }
  }

  public static class WordMorph implements UnaryOperator<String> {
    private final UnaryOperator<String> morph;

    public WordMorph(UnaryOperator<String> morph) {
      this.morph = morph;
    }

    public static WordMorph of(UnaryOperator<String> morph) {
      return new WordMorph(morph);
    }

    @Override
    public String apply(String s) {
      return Arrays.stream(s.split(" ")).map(morph).collect(Collectors.joining(" "));
    }
  }

  public static class CharacterMorph implements UnaryOperator<String> {
    private final Function<Character, String> morph;

    public CharacterMorph(Function<Character, String> morph) {
      this.morph = morph;
    }

    public static CharacterMorph of(Function<Character, String> morph) {
      return new CharacterMorph(morph);
    }

    @Override
    public String apply(String s) {
      StringBuilder builder = new StringBuilder();
      char[] chars = s.toCharArray();
      for(int i = 0; i < chars.length; i++) {
        builder.append(morph.apply(chars[i]));
      }
      return builder.toString();
    }
  }
}
