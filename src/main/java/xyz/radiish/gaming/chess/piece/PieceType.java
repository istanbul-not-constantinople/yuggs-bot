package xyz.radiish.gaming.chess.piece;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;
import xyz.radiish.gaming.chess.moves.Move;
import xyz.radiish.gaming.chess.util.BitMath;

import java.util.Collection;

public abstract class PieceType extends BitMath {
  public static final PieceType EMPTY = new PieceBuilder().setKey("--").setPlayer(Player.INVALID).build();

  public static PieceBuilder builder() {
    return new PieceBuilder();
  }

  public PieceType(byte bits) {
    super(bits);
  }

  public abstract Collection<Move> moves(Vec2i source, Board board);

  public abstract Player getPlayer();

  public abstract String getName();
}
