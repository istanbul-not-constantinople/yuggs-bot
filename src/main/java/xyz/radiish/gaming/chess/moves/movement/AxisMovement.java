package xyz.radiish.gaming.chess.moves.movement;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;
import xyz.radiish.gaming.chess.moves.Move;

import java.util.Collection;
import java.util.function.BiPredicate;

public class AxisMovement extends Movement {
  public enum Axis {
    COLUMN,
    ROW,
    SLASH,
    BACKSLASH;

    public static final Axis[] ORTHOGONAL = new Axis[] { COLUMN, ROW };
    public static final Axis[] DIAGONAL   = new Axis[] { SLASH, BACKSLASH };
    public static final Axis[] ALL        = new Axis[] { COLUMN, ROW, SLASH, BACKSLASH };
  }

  private static boolean yuggs(Vec2i source, Board board) {
    return true;
  }

  private byte axes;
  private BiPredicate<Vec2i, Board> blocker;

  public AxisMovement() {
    blocker = AxisMovement::yuggs;
  }

  public AxisMovement addAxis(Axis ... axes) {
    for (Axis axis : axes) {
      this.axes |= 1 << axis.ordinal();
    }
    return this;
  }

  public AxisMovement setBlocker(BiPredicate<Vec2i, Board> blocker) {
    this.blocker = blocker;
    return this;
  }

  @Override
  public Collection<Move> moves(Vec2i source, Board board) {
    return null;
  }
}
