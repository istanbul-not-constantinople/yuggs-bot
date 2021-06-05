package xyz.radiish.zephyr.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoopingIterator<E> implements Iterator<E> {
  private Supplier<Iterator<E>> looper;
  private Iterator<E> it;

  public LoopingIterator(Supplier<Iterator<E>> looper) {
    setLooper(looper);
    it = looper.get();
  }

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public E next() {
    if(it.hasNext()) {
      return it.next();
    } else {
      it = getLooper().get();
      return next();
    }
  }

  @Override
  public void forEachRemaining(Consumer<? super E> action) {
    Objects.requireNonNull(action);
    while (it.hasNext())
      action.accept(next());
  }

  @Override
  public void remove() {
    it.remove();
  }

  public Supplier<Iterator<E>> getLooper() {
    return looper;
  }

  public void setLooper(Supplier<Iterator<E>> looper) {
    this.looper = looper;
  }
}
