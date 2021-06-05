package xyz.radiish.zephyr.command.source;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.storage.UserRecord;

public class CommandSource {
  private final Message message;
  private final Zephyr client;
  private final Guild guild;

  public CommandSource(Guild guild, Message message, Zephyr client) {
    this.guild = guild;
    this.message = message;
    this.client = client;
  }

  public boolean isPrivate() {
    return getGuild() == null;
  }

  public Guild getGuild() {
    return guild;
  }

  public Message getMessage() {
    return message;
  }

  public Zephyr getClient() {
    return client;
  }

  public UserRecord fetchUserRecord() {
    return client.fetchUserRecord(message.getAuthor());
  }

  public void updateUserRecord(UserRecord record) {
    client.updateUserRecord(record);
  }
}
