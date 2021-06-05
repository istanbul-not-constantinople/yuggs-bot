package xyz.radiish.zephyr.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.List;

public class LiteralCollectionArgumentType implements ArgumentType<String> {
  private final List<String> literals;


  public LiteralCollectionArgumentType(List<String> literals) {
    this.literals = literals;
  }

  public static LiteralCollectionArgumentType literals(String ... literals) {
    return literals(Arrays.asList(literals));
  }

  public static LiteralCollectionArgumentType literals(List<String> literals) {
    return new LiteralCollectionArgumentType(literals);
  }

  @Override
  public String parse(StringReader reader) throws CommandSyntaxException {
    for(String literal : getLiterals()) {
      int start = reader.getCursor();
      if(reader.canRead(literal.length())) {
        int end = start + literal.length();
        String substring = reader.getString().substring(start, end);
        if(substring.equals(literal)) {
          reader.setCursor(end);
          if(!reader.canRead() || reader.peek() == ' ') {
            return substring;
          } else {
            reader.setCursor(start);
          }
        }
      }
    }
    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, String.join(",", getLiterals()));
  }

  public List<String> getLiterals() {
    return literals;
  }
}
