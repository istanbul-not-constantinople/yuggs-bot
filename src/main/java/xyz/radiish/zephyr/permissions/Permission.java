package xyz.radiish.zephyr.permissions;

import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.source.CommandSource;

public abstract class Permission {
  public abstract boolean resolve(User user, CommandSource source);
}
