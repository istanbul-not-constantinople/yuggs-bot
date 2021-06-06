package xyz.radiish.zephyr.storage;

import xyz.radiish.zephyr.cereal.TypedObject;

import java.util.ArrayList;
import java.util.List;

public class RecordProvider {
  private List<TypedObject<?>> records;

  public RecordProvider() {
    records = new ArrayList<>();
  }
}
