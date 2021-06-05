package xyz.radiish.gaming.chess.piece;


import xyz.radiish.gaming.chess.Board;
import xyz.radiish.gaming.chess.Vec2i;
import xyz.radiish.gaming.chess.moves.Move;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;

public abstract class AbstractPieceBuilder<T extends AbstractPieceBuilder<?>> {
  private static class BuiltPieceType extends PieceType {
    private final BiFunction<Vec2i, Board, Collection<Move>> function;
    private final String key;
    private final Player player;

    public BuiltPieceType(byte bits, String key, BiFunction<Vec2i, Board, Collection<Move>> function, Player player) {
      super(bits);
      this.key = key;
      this.function = function;
      this.player = player;
    }

    @Override
    public Collection<Move> moves(Vec2i source, Board board) {
      return function.apply(source, board);
    }

    @Override
    public Player getPlayer() {
      return player;
    }

    @Override
    public String getName() {
      return key;
    }
  }

  private static Collection<Move> noMoves(Vec2i source, Board board) {
    return Collections.emptyList();
  }

  private BiFunction<Vec2i, Board, Collection<Move>> function;
  private String key;
  private byte bits;
  private Player player;

  public AbstractPieceBuilder() {
    function = AbstractPieceBuilder::noMoves;
    key = "  ";
  }

  public T setKey(String key) {
    this.key = key;
    return (T) this;
  }

  public T setBits(byte bits) {
    this.bits = bits;
    return (T) this;
  }

  public T setFunction(BiFunction<Vec2i, Board, Collection<Move>> function) {
    this.function = function;
    return (T) this;
  }

  public abstract T copy();

  public T setPlayer(Player player) {
    this.player = player;
    bits = player.or(bits);
    key = player.getOperator().apply(key);
    return (T) this;
  }

  public PieceType build() {
    return new BuiltPieceType(bits, key, function, player);
  }
  public BiFunction<Vec2i, Board, Collection<Move>> getFunction() {
    return function;
  }

  public String getKey() {
    return key;
  }

  public byte getBits() {
    return bits;
  }

  public Player getPlayer() {
    return player;
  }
}