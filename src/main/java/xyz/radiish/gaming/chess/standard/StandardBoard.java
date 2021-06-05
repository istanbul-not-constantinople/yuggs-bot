package xyz.radiish.gaming.chess.standard;

import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.moves.Move;
import xyz.radiish.gaming.chess.piece.Player;
import xyz.radiish.gaming.chess.state.GameState;

import java.util.Arrays;
import java.util.List;

public class StandardBoard extends Board {
  public StandardBoard() {
    super(8, 8,
      () -> Arrays.asList(Player.WHITE, Player.BLACK).iterator(),
      KingType.BLACK,
      KingType.WHITE
    );
    set(0, 0, KingType.BLACK);
    set(7, 7, KingType.WHITE);
  }

  @Override
  public boolean isThreatened(int x, int y) {
    return false;
  }

  @Override
  public GameState moves() {
    setPlayer(next());
    List<Move> moves = allMoves();
    if(moves.isEmpty()) {
      return GameState.stalemate();
    } else {
      return GameState.move(moves);
    }
  }
}
