package xyz.radiish.zephyr.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProviderKeyRegistry {
  public static final ProviderKeyRegistry USERS = new ProviderKeyRegistry("users");
  public static final ProviderKeyRegistry GUILDS = new ProviderKeyRegistry("guilds");

  public static final Map<String, ProviderKeyRegistry> REGISTRIES = new HashMap<>();

  static {
    REGISTRIES.put("users", USERS);
    REGISTRIES.put("guilds", GUILDS);
  }

  private final Map<String, ProviderKey<?>> stringsToKeys;
  private final String key;

  public ProviderKeyRegistry(String key) {
    stringsToKeys = new HashMap<>();
    this.key = key;
  }

  public <T extends ProvidedRecord> ProviderKey<T> register(String key, Supplier<T> supplier) {
    ProviderKey<T> pKey = new ProviderKey<>(key, supplier);
    getStringsToKeys().put(key, pKey);
    return pKey;
  }

  public Map<String, ProviderKey<?>> getStringsToKeys() {
    return stringsToKeys;
  }

  public String getKey() {
    return key;
  }
}
