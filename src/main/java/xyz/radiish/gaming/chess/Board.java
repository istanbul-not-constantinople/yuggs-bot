package xyz.radiish.gaming.chess;

import xyz.radiish.gaming.chess.moves.Move;
import xyz.radiish.gaming.chess.piece.PieceType;
import xyz.radiish.gaming.chess.piece.Player;
import xyz.radiish.gaming.chess.state.GameState;
import xyz.radiish.gaming.chess.util.BitMath;
import xyz.radiish.zephyr.util.LoopingIterator;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Board extends LoopingIterator<Player> {
  private List<Move> history;
  private final PieceType[] pieces;
  private final byte[][] matrix;
  private final int width;
  private final int height;
  private Player player;

  public Board(int width, int height, Supplier<Iterator<Player>> loop, PieceType ... pieces) {
    super(loop);
    this.width = width;
    this.height = height;
    matrix = new byte[width][height];
    history = new ArrayList<>();
    this.pieces = new PieceType[256];
    for(PieceType piece : pieces) {
      this.pieces[BitMath.unsigned(piece.getBits())] = piece;
    }
  }

  public void empty(int x, int y) {
    set(x, y, PieceType.EMPTY.getBits());
  }

  public void empty(Vec2i tile) {
    empty(tile.getX(), tile.getY());
  }

  public void set(int x, int y, byte piece) {
    matrix[y][x] = piece;
  }

  public void set(Vec2i tile, byte piece) {
    set(tile.getX(), tile.getY(), piece);
  }

  public void set(int x, int y, PieceType piece) {
    set(x, y, piece.getBits());
  }

  public void set(Vec2i tile, PieceType piece) {
    set(tile, piece.getBits());
  }

  public byte get(int x, int y) {
    return matrix[y][x];
  }

  public byte get(Vec2i tile) {
    return get(tile.getX(), tile.getY());
  }

  public byte[][] getMatrix() {
    return matrix;
  }

  public PieceType of(byte bits) {
    PieceType piece = pieces[BitMath.unsigned(bits)];
    return piece != null ? piece : PieceType.EMPTY;
  }

  public List<Move> movesFor(int x, int y) {
    return movesFor(new Vec2i(x, y));
  }

  public List<Move> movesFor(Vec2i tile) {
    PieceType type = of(get(tile));
    if(type.getPlayer().equals(player)) {
      List<Move> moves = new ArrayList<>(type.moves(tile, this));
      moves.removeIf(this::isIllegal);
      return moves;
    } else {
      return Collections.emptyList();
    }
  }

  public abstract boolean isThreatened(int x, int y);

  protected List<Move> allMoves() {
    List<Move> list = new ArrayList<>();
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        list.addAll(movesFor(x, y));
      }
    }
    return list;
  }

  public abstract GameState moves();

  public boolean isInBounds(Vec2i tile) {
    return isInBounds(tile.getX(), tile.getY());
  }

  public boolean isInBounds(int x, int y) {
    return (height > y  && y >= 0 && width > x && x >= 0);
  }

  public boolean isIllegal(Move move) {
    return !isInBounds(move.getTarget());
  }

  @Override
  public String toString() {
    return Arrays.stream(matrix).map(row -> BitMath.stream(row).map(piece -> of(piece).getName()).collect(Collectors.joining(" "))).collect(Collectors.joining("\n"));
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
