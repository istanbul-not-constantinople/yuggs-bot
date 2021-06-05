package xyz.radiish.zephyr.messaging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import xyz.radiish.zephyr.messaging.sendable.Sendable;
import xyz.radiish.zephyr.util.RestActionQueue;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class InformationBuilder extends AbstractMessageEmbedBuilder<InformationBuilder> {
  public static final int COLOR = 7506394;

  public static class LoadingInfo {
    private int progress;
    private final int max;
    private final RestActionQueue<MessageEmbed, Message> queue;
    private Sendable sendable;
    private EmbedBuilder builder;

    public LoadingInfo(int max, Sendable sendable) {
      this.max = max;
      setSendable(sendable);
      setProgress(0);
      setBuilder(new EmbedBuilder());
      queue = new RestActionQueue<>(message -> getSendable().send(message), message -> setSendable(Sendable.of(message)));
    }


    public int getProgress() {
      return progress;
    }

    public void setProgress(int progress) {
      this.progress = progress;
    }

    public void updateProgress(int progress) {
      setProgress(progress);
      getQueue().getQueue().add(new EmbedBuilder(getBuilder()).setColor(COLOR).setDescription(getBuilder().getDescriptionBuilder().toString() + "\n" + toString()).build());
      getQueue().flush();
    }

    public int getMax() {
      return max;
    }

    public Sendable getSendable() {
      return sendable;
    }

    public void setSendable(Sendable sendable) {
      this.sendable = sendable;
    }

    @Override
    public String toString() {
      int notches = Math.round(getProgress() * 10f / getMax());
      StringBuilder builder = new StringBuilder();
      for(int i = 1; i <= 10; i++) {
        builder.append(i <= notches ? "▰" : "▱");
      }
      return String.format("`%s %d/%d (%d%%)`", builder.toString(), getProgress(), getMax(), Math.round(getProgress() * 100f / getMax()));
    }

    public RestActionQueue<MessageEmbed, Message> getQueue() {
      return queue;
    }

    public EmbedBuilder getBuilder() {
      return builder;
    }

    public void setBuilder(EmbedBuilder builder) {
      this.builder = builder;
    }
  }
  public InformationBuilder() {
    setColor(COLOR);
  }

  public CompletableFuture<InformationBuilder> promise(BiConsumer<LoadingInfo, InformationBuilder> promise, int max, Sendable target) {
    return CompletableFuture.supplyAsync(() -> {
      LoadingInfo info = new LoadingInfo(max, target);
      promise.accept(info, this);
      info.getQueue().getQueue().add(build());
      info.getQueue().flush();
      return this;
    });
  }
}
