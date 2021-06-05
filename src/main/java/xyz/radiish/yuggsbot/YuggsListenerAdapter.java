package xyz.radiish.yuggsbot;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class YuggsListenerAdapter extends ListenerAdapter {
  @Override
  public void onButtonClick(@NotNull ButtonClickEvent event) {
    if(event.getComponent().getLabel().equals("big_blue")) {

    }
  }
}
