package xyz.radiish.zephyr.permissions;

import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.storage.ProviderKey;

public class KeyPermission extends Permission {
  private final String key;

  public KeyPermission(String key) {
    this.key = key;
  }

  @Override
  public boolean resolve(User user, CommandSource source) {
    return source.fetchUserRecord().get(Zephyr.USER_RECORD).getPermissionKeys().contains(key);
  }
}
