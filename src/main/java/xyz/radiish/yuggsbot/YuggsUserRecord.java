package xyz.radiish.yuggsbot;

import xyz.radiish.zephyr.cereal.JsonField;
import xyz.radiish.zephyr.cereal.JsonSerializable;
import xyz.radiish.zephyr.storage.ProvidedRecord;

@JsonSerializable
public class YuggsUserRecord extends ProvidedRecord {
  @JsonField
  private int buttonClicks;

  public YuggsUserRecord() {
    buttonClicks = 0;
  }

  public int getButtonClicks() {
    return buttonClicks;
  }

  public void setButtonClicks(int buttonClicks) {
    this.buttonClicks = buttonClicks;
  }
}
