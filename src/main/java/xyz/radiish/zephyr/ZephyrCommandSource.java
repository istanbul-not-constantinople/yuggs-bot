package xyz.radiish.zephyr;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class ZephyrCommandSource {
  private final boolean privacy;
  private final Message message;
  private final Guild guild;

  public ZephyrCommandSource(boolean privacy, Message message, Guild guild) {
    this.privacy = privacy;
    this.message = message;
    this.guild = guild;
  }


  public boolean isPrivate() {
    return privacy;
  }

  public Message getMessage() {
    return message;
  }

  public Guild getGuild() {
    return guild;
  }
}
