package xyz.radiish.zephyr;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.radiish.zephyr.command.CommandDispatcherListenerAdapter;

import java.util.regex.Pattern;

public class Zephyr {
  private ListenerAdapter adapter;
  private CommandDispatcher<ZephyrCommandSource> dispatcher;
  private Pattern prefix;
  private JDA jda;

  protected Zephyr(JDA jda) {
    setAdapter(new ListenerAdapter() {});
    setDispatcher(new CommandDispatcher<>());
    setPrefix(";");
    setJda(jda);

    getJda().addEventListener(new CommandDispatcherListenerAdapter());
  }

  public ListenerAdapter getAdapter() {
    return adapter;
  }

  public void setAdapter(ListenerAdapter adapter) {
    this.adapter = adapter;
  }

  public CommandDispatcher<ZephyrCommandSource> getDispatcher() {
    return dispatcher;
  }

  public void setDispatcher(CommandDispatcher<ZephyrCommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public Pattern getPrefix() {
    return prefix;
  }

  public void setPrefix(Pattern prefix) {
    this.prefix = prefix;
  }

  public void setPrefix(String prefix) {
    setPrefix(Pattern.compile(prefix));
  }

  public JDA getJda() {
    return jda;
  }

  public void setJda(JDA jda) {
    this.jda = jda;
  }
}
