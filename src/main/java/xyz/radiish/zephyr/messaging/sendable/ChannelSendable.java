package xyz.radiish.zephyr.messaging.sendable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class ChannelSendable extends Sendable {
  private MessageChannel channel;

  public ChannelSendable(MessageChannel channel) {
    setChannel(channel);
  }

  @Override
  public MessageAction send(Message message) {
    return getChannel().sendMessage(message);
  }

  @Override
  public MessageAction send(MessageEmbed embed) {
    return getChannel().sendMessage(embed);
  }

  public MessageChannel getChannel() {
    return channel;
  }

  public void setChannel(MessageChannel channel) {
    this.channel = channel;
  }
}
