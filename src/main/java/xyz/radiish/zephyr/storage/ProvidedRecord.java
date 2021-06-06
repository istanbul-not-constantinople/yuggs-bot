package xyz.radiish.zephyr.storage;

import xyz.radiish.zephyr.Zephyr;

public class ProvidedRecord {
  private RecordProvider provider;

  public void setProvider(RecordProvider provider) {
    this.provider = provider;
  }

  public RecordProvider getProvider() {
    return provider;
  }

  public void update() {
    getProvider().update();
  }
}
