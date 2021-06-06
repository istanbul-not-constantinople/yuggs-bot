package xyz.radiish.yuggsbot;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.radiish.yuggsbot.command.BigBlueButtonCommand;

public class YuggsListenerAdapter extends ListenerAdapter {
  private final Yuggs client;

  public YuggsListenerAdapter(Yuggs client) {
    this.client = client;
  }


  @Override
  public void onButtonClick(@NotNull ButtonClickEvent event) {
    if(event.getComponent() != null && event.getComponent().getId() != null) {
      switch(event.getComponent().getId()) {
        case "big_blue":
          YuggsUserRecord record = client.fetchUserRecord(event.getUser()).get(Yuggs.USER_RECORD);
          record.setButtonClicks(record.getButtonClicks() + 1);
          event.reply(BigBlueButtonCommand.messageFor(record.getButtonClicks())).queue();
          record.update();
          break;
      }
    }
  }
}
