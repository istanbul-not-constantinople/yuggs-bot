package xyz.radiish.gaming.chess.standard;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;
import xyz.radiish.gaming.chess.moves.DirectMove;
import xyz.radiish.gaming.chess.moves.Move;
import xyz.radiish.gaming.chess.piece.PieceBuilder;
import xyz.radiish.gaming.chess.piece.PieceType;
import xyz.radiish.gaming.chess.piece.Player;
import xyz.radiish.gaming.chess.util.BitMath;

import java.util.Arrays;
import java.util.Collection;

public class KingType {
  private static Collection<Move> moves(Vec2i source, Board board) {
   return Arrays.asList(
     new DirectMove(source, source.plus(-1, -1)),
     new DirectMove(source, source.plus( 0, -1)),
     new DirectMove(source, source.plus( 1, -1)),
     new DirectMove(source, source.plus(-1,  0)),
     new DirectMove(source, source.plus( 1,  0)),
     new DirectMove(source, source.plus(-1,  1)),
     new DirectMove(source, source.plus( 0,  1)),
     new DirectMove(source, source.plus( 1,  1))
   );
  }

  public static final PieceType BLACK;
  public static final PieceType WHITE;
  static {
    PieceBuilder king = new PieceBuilder().setBits(BitMath.signed(1)).setFunction(KingType::moves).setKey("kg");
    BLACK = king.copy().setPlayer(Player.BLACK).build();
    WHITE = king.copy().setPlayer(Player.WHITE).build();
  }
}
