package xyz.radiish.zephyr.command;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.command.source.CommandSource;
import xyz.radiish.zephyr.messaging.ErrorBuilder;

import java.util.regex.Matcher;

public class ZephyrListenerAdapter extends ListenerAdapter {
  private final Zephyr client;

  public ZephyrListenerAdapter(Zephyr client) {
    this.client = client;
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    Matcher matcher = client.getPrefix().matcher(event.getMessage().getContentRaw());
    if(matcher.find()) {
      CommandSource source = new CommandSource(event.getGuild(), event.getMessage(), client);
      ParseResults<CommandSource> parsed = client.getDispatcher().parse(matcher.group(2), source);

      if(parsed.getExceptions().isEmpty()) {
        try {
          client.getDispatcher().execute(parsed);
        } catch (CommandSyntaxException e) {
          event.getChannel().sendMessage(new ErrorBuilder()
            .appendDescription("```md\n")
            //.appendDescription(parsed.getExceptions().values().stream().map(CommandSyntaxException::getMessage).reduce("", (total, ex) -> total + ex + "\n"))
            .appendDescription(e.getMessage())
            .appendDescription("```")
            .build()).queue();
        }
      } else {
        event.getChannel().sendMessage(new ErrorBuilder()
          .appendDescription("```md\n")
          .appendDescription(parsed.getExceptions().values().stream().map(CommandSyntaxException::getMessage).reduce("", (total, ex) -> total + ex + "\n"))
          //.appendDescription(String.format("use ```\n;help argument %s```", parsed.getContext().getArguments().get(parsed.getContext().getArguments().size() - 1).getResult()))
          .appendDescription("```")
          .build()).queue();
      }
    }
  }


}
