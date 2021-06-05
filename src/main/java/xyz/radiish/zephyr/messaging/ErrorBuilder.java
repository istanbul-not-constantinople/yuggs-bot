package xyz.radiish.zephyr.messaging;

public class ErrorBuilder extends AbstractMessageEmbedBuilder<ErrorBuilder> {
  public ErrorBuilder() {
    super();
    setColor(0xD34B4B);
    setThumbnail("https://i.imgur.com/4ndZiuW.png");
  }
}
