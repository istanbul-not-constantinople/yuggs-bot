package xyz.radiish.gaming.chess.moves;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;

public class DirectMove extends Move {
  public DirectMove(Vec2i source, Vec2i target) {
    super(source, target);
  }

  @Override
  public Vec2i[] takes(Board board) {
    return new Vec2i[] { getTarget() };
  }

  @Override
  public void accept(Board board) {
    byte piece = board.get(getSource());
    board.empty(getSource());
    board.set(getTarget(), piece);
  }
}
