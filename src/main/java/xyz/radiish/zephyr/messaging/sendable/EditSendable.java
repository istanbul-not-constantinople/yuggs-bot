package xyz.radiish.zephyr.messaging.sendable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class EditSendable extends Sendable {
  private Message message;

  public EditSendable(Message message) {
    setMessage(message);
  }

  @Override
  public MessageAction send(Message message) {
    return getMessage().editMessage(message);
  }

  @Override
  public MessageAction send(MessageEmbed embed) {
    return getMessage().editMessage(embed);
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }
}
