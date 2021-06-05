package xyz.radiish.zephyr.permissions;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.Optional;

public class RolePermission extends Permission {
  private final Role role;

  public RolePermission(Role role) {
    this.role = role;
  }

  @Override
  public boolean resolve(User user, CommandSource source) {
    return Optional.ofNullable(role.getGuild().getMember(user)).map(member -> member.getRoles().contains(role)).orElse(false);
  }
}
