package xyz.radiish.zephyr.permissions;

import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.source.CommandSource;

public class KeyPermission extends Permission {
  private final String key;

  public KeyPermission(String key) {
    this.key = key;
  }

  @Override
  public boolean resolve(User user, CommandSource source) {
    return source.fetchUserRecord().getPermissionKeys().contains(key);
  }
}
