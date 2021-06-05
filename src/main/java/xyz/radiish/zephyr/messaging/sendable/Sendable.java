package xyz.radiish.zephyr.messaging.sendable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public abstract class Sendable {
  public abstract MessageAction send(Message message);
  public abstract MessageAction send(MessageEmbed embed);

  public static Sendable of(Message message) {
    return new EditSendable(message);
  }

  public static Sendable of(MessageChannel channel) {
    return new ChannelSendable(channel);
  }
}
