package xyz.radiish.gaming.chess.state;

import xyz.radiish.gaming.chess.moves.Move;
import xyz.radiish.gaming.chess.piece.Player;

import java.util.Collections;
import java.util.List;

public class GameState {
  public enum State {
    PLAYING,
    CHECKMATE,
    STALEMATE
  }

  private final List<Move> moves;
  private final State state;
  private final Player winner;

  public static GameState move(List<Move> moves) {
    return new GameState(moves, State.PLAYING, null);
  }

  public static GameState checkmate(Player player) {
    return new GameState(Collections.emptyList(), State.CHECKMATE, player);
  }

  public static GameState stalemate() {
    return new GameState(Collections.emptyList(), State.STALEMATE, null);
  }

  private GameState(List<Move> moves, State state, Player winner) {
    this.moves = moves;
    this.state = state;
    this.winner = winner;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public boolean isOver() {
    return state == State.CHECKMATE || state == State.STALEMATE;
  }

  public Player getWinner() {
    return winner;
  }

  public State getState() {
    return state;
  }
}