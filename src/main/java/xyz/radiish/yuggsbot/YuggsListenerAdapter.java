package xyz.radiish.yuggsbot;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.radiish.yuggsbot.command.BigBlueButtonCommand;
import xyz.radiish.zephyr.storage.UserRecord;

public class YuggsListenerAdapter extends ListenerAdapter {
  private final YuggsBot client;

  public YuggsListenerAdapter(YuggsBot client) {
    this.client = client;
  }


  @Override
  public void onButtonClick(@NotNull ButtonClickEvent event) {
    if(event.getComponent() != null && event.getComponent().getId() != null) {
      switch(event.getComponent().getId()) {
        case "big_blue":
          UserRecord record = client.fetchUserRecord(event.getUser());
          record.setButtonClicks(record.getButtonClicks() + 1);
          event.reply(BigBlueButtonCommand.messageFor(record)).queue();
          client.updateUserRecord(record);
          break;
      }
    }
  }
}
