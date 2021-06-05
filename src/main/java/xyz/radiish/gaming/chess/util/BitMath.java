package xyz.radiish.gaming.chess.util;

import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;

import java.util.stream.Stream;

public class BitMath {
  public static byte signed(int unsigned) {
    return (byte) (unsigned > 127 ? unsigned - 256 : unsigned);
  }
  public static int unsigned(byte signed) {
    return signed & 255;
  }

  public static Stream<Byte> stream(byte[] bytes) {
    Stream.Builder<Byte> builder = Stream.builder();
    for(byte twoNybbles : bytes) {
      builder.add(twoNybbles);
    }
    return builder.build();
  }

  private byte bits;

  public BitMath() {
    this(0);
  }

  public BitMath(int bits) {
    this(BitMath.signed(bits));
  }

  public BitMath(byte bits) {
    setBits(bits);
  }

  public byte or(byte bits) {
    return (byte) (this.bits | bits);
  }

  public byte and(byte bits) {
    return (byte) (this.bits & bits);
  }

  public byte getBits() {
    return bits;
  }

  public byte bits() {
    return getBits();
  }

  public void setBits(byte bits) {
    this.bits = bits;
  }
}
