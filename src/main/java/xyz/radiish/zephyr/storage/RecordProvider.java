package xyz.radiish.zephyr.storage;

import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.cereal.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@JsonSerializable
public class RecordProvider {
  @JsonField("_id")
  private long id;

  private Map<ProviderKey<?>, ProvidedRecord> records;
  private Zephyr client;
  private ProviderKeyRegistry registry;

  public RecordProvider() {
    id = 0;
    records = new HashMap<>();
  }

  public RecordProvider(ProviderKeyRegistry registry, long id) {
    this();
    this.registry = registry;
    this.id = id;
  }

  public <T extends ProvidedRecord> T get(ProviderKey<T> key) {
    if(!records.containsKey(key)) {
      T record = key.get();
      record.setProvider(this);
      records.put(key, record);
    }
    return (T) records.get(key);
  }

  @JsonGetter("records")
  public Map<String, TypedObject<? extends ProvidedRecord>> getTypedRecords() {
    return records.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getKey(), entry -> TypedObject.of(entry.getValue())));
  }

  @JsonSetter("records")
  public void setTypedRecords(Map<String, TypedObject<? extends ProvidedRecord>> records) {
    this.records = records.entrySet().stream().collect(Collectors.toMap(entry -> registry.getStringsToKeys().get(entry.getKey()), entry -> {
      ProvidedRecord record = entry.getValue().getValue();
      record.setProvider(this);
      return record;
    }));
  }

  @JsonGetter(value = "registry", priority = 0)
  public String getRegistryKey() {
    return registry.getKey();
  }

  @JsonSetter(value = "registry", priority = 0)
  public void setRegistryByKey(String key) {
    this.registry = ProviderKeyRegistry.REGISTRIES.get(key);
  }

  public long getId() {
    return id;
  }

  public void setClient(Zephyr client) {
    this.client = client;
  }

  public Zephyr getClient() {
    return client;
  }

  public void update() {
    getClient().updateUserRecord(this);
  }

  public Map<ProviderKey<?>, ProvidedRecord> getRecords() {
    return records;
  }
}
