package xyz.radiish.zephyr.command.argument;

import xyz.radiish.zephyr.command.source.CommandSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Selection<T> {
  private List<SelectionRule<? super T>> rules;

  protected Selection(List<SelectionRule<? super T>> rules) {
    setRules(rules);
  }

  public Optional<T> first(CommandSource source) {
    return filteredStream(source).findFirst();
  }

  public abstract Stream<T> stream(CommandSource source);

  public Stream<T> filteredStream(CommandSource source) {
    AtomicReference<Stream<T>> stream = new AtomicReference<>(stream(source));
    getRules().forEach(rule -> stream.set(rule.apply(stream.get())));
    return stream.get();
  }

  public List<T> all(CommandSource source) {
    return filteredStream(source).collect(Collectors.toList());
  }


  public List<SelectionRule<? super T>> getRules() {
    return rules;
  }

  public void setRules(List<SelectionRule<? super T>> rules) {
    this.rules = rules;
  }

  public static Selection<?> empty() {
    return new Selection<Object>(new ArrayList<>()) {
      @Override
      public Stream<Object> stream(CommandSource source) {
        return Stream.empty();
      }
    };
  }
}
