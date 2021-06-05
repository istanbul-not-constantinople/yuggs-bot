package xyz.radiish.gaming.chess.piece;

import static com.diogonunes.jcolor.Attribute.*;

import com.diogonunes.jcolor.Ansi;
import xyz.radiish.gaming.chess.util.BitMath;

import java.util.function.UnaryOperator;

public class Player extends BitMath {
  public static final Player INVALID = new Player(0b00000000, str -> Ansi.colorize(str,               BRIGHT_BLACK_TEXT()));
  public static final Player BLACK =   new Player(0b00000000, str -> Ansi.colorize(str,               RED_TEXT()));
  public static final Player WHITE =   new Player(0b10000000, str -> Ansi.colorize(str.toUpperCase(), BRIGHT_WHITE_TEXT()));

  private final UnaryOperator<String> operator;

  public Player(int bits, UnaryOperator<String> operator) {
    super(bits);
    this.operator = operator;
  }

  public UnaryOperator<String> getOperator() {
    return operator;
  }
}
