package xyz.radiish.zephyr.messaging;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public abstract class AbstractMessageEmbedBuilder<T extends AbstractMessageEmbedBuilder<?>> extends EmbedBuilder {

  public AbstractMessageEmbedBuilder() {
    setFooter("made by radiish | beep boop");
  }

  @NotNull
  @Override
  public T clear() {
    return (T) super.clear();
  }

  @NotNull
  @Override
  public T setTitle(@Nullable String title) {
    return (T) super.setTitle(title);
  }

  @NotNull
  @Override
  public T setTitle(@Nullable String title, @Nullable String url) {
    return (T) super.setTitle(title, url);
  }

  @NotNull
  @Override
  public T appendDescription(@NotNull CharSequence description) {
    return (T) super.appendDescription(description);
  }

  @NotNull
  @Override
  public T setTimestamp(@Nullable TemporalAccessor temporal) {
    return (T) super.setTimestamp(temporal);
  }

  @NotNull
  @Override
  public T setColor(@Nullable Color color) {
    return (T) super.setColor(color);
  }

  @NotNull
  @Override
  public T setColor(int color) {
    return (T) super.setColor(color);
  }

  @NotNull
  @Override
  public T setThumbnail(@Nullable String url) {
    return (T) super.setThumbnail(url);
  }

  @NotNull
  @Override
  public T setImage(@Nullable String url) {
    return (T) super.setImage(url);
  }

  @NotNull
  @Override
  public T setAuthor(@Nullable String name) {
    return (T) super.setAuthor(name);
  }

  @NotNull
  @Override
  public T setAuthor(@Nullable String name, @Nullable String url) {
    return (T) super.setAuthor(name, url);
  }

  @NotNull
  @Override
  public T setAuthor(@Nullable String name, @Nullable String url, @Nullable String iconUrl) {
    return (T) super.setAuthor(name, url, iconUrl);
  }

  @NotNull
  @Override
  public T setFooter(@Nullable String text) {
    return (T) super.setFooter(text);
  }

  @NotNull
  @Override
  public T setFooter(@Nullable String text, @Nullable String iconUrl) {
    return (T) super.setFooter(text, iconUrl);
  }

  @NotNull
  @Override
  public T addField(@Nullable MessageEmbed.Field field) {
    return (T) super.addField(field);
  }

  @NotNull
  @Override
  public T addField(@Nullable String name, @Nullable String value, boolean inline) {
    return (T) super.addField(name, value, inline);
  }

  @NotNull
  @Override
  public T addBlankField(boolean inline) {
    return (T) super.addBlankField(inline);
  }

  @NotNull
  @Override
  public T clearFields() {
    return (T) super.clearFields();
  }
}
