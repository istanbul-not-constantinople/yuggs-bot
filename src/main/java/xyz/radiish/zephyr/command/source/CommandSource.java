package xyz.radiish.zephyr.command.source;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.messaging.sendable.Sendable;

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
}
