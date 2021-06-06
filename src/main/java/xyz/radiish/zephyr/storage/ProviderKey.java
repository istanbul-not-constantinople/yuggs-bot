package xyz.radiish.zephyr.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProviderKey<T extends ProvidedRecord> implements Supplier<T> {
  private final String key;
  private final Supplier<T> supplier;

  public ProviderKey(String key, Supplier<T> supplier) {
    this.key = key;
    this.supplier = supplier;
  }

  public String getKey() {
    return key;
  }

  @Override
  public T get() {
    return supplier.get();
  }
}
