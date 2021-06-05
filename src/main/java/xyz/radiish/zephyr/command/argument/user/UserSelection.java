package xyz.radiish.zephyr.command.argument.user;

import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.command.argument.Selection;
import xyz.radiish.zephyr.command.argument.SelectionRule;
import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.List;
import java.util.stream.Stream;

public class UserSelection extends Selection<User> {

  protected UserSelection(List<SelectionRule<? super User>> selectionRules) {
    super(selectionRules);
  }

  @Override
  public Stream<User> stream(CommandSource source) {
    return source.getClient().getJda().getUsers().stream();
  }
}
