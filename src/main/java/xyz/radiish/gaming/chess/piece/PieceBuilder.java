package xyz.radiish.gaming.chess.piece;

public class PieceBuilder extends AbstractPieceBuilder<PieceBuilder> {
  @Override
  public PieceBuilder copy() {
    return new PieceBuilder()
      .setBits(getBits())
      .setKey(getKey())
      .setFunction(getFunction());
  }
}
