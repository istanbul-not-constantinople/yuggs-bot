package xyz.radiish.gaming.chess.moves;

import com.google.common.base.MoreObjects;
import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;

import java.util.function.Consumer;

public abstract class Move implements Consumer<Board> {
  private final Vec2i source;
  private final Vec2i target;

  public Move(Vec2i source, Vec2i target) {
    this.source = source;
    this.target = target;
  }

  public Vec2i getSource() {
    return source;
  }

  public Vec2i getTarget() {
    return target;
  }

  public abstract Vec2i[] takes(Board board);

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("source", source)
      .add("target", target)
      .toString();
  }
}
