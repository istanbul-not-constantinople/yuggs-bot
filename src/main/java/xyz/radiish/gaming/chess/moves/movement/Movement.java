package xyz.radiish.gaming.chess.moves.movement;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;
import xyz.radiish.gaming.chess.moves.Move;

import java.util.Collection;
import java.util.function.BiFunction;

public abstract class Movement {
  public abstract Collection<Move> moves(Vec2i source, Board board);
}
