package xyz.radiish.zephyr.util;

import net.dv8tion.jda.api.requests.RestAction;
import xyz.radiish.zephyr.messaging.sendable.Sendable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RestActionQueue<T, U> {
  private List<T> queue;
  private boolean emptying;
  private Function<T, RestAction<U>> clear;
  private Consumer<U> queueConsumer;

  public RestActionQueue(Function<T, RestAction<U>> clear, Consumer<U> queueConsumer) {
    setClear(clear);
    setQueueConsumer(queueConsumer);
    setQueue(new ArrayList<>());
  }

  public void flush() {
    if(!isEmptying()) {
      setEmptying(true);
      emptyQueue();
    }
  }

  private void emptyQueue() {
    if(getQueue().size() > 0) {
      getClear().apply(getQueue().get(0)).queue(result -> {
        getQueueConsumer().accept(result);
        emptyQueue();
      });
      getQueue().remove(0);
    } else {
      setEmptying(false);
    }
  }

  public List<T> getQueue() {
    return queue;
  }

  public void setQueue(List<T> queue) {
    this.queue = queue;
  }

  public boolean isEmptying() {
    return emptying;
  }

  public void setEmptying(boolean emptying) {
    this.emptying = emptying;
  }

  public Function<T, RestAction<U>> getClear() {
    return clear;
  }

  public void setClear(Function<T, RestAction<U>> clear) {
    this.clear = clear;
  }

  public Consumer<U> getQueueConsumer() {
    return queueConsumer;
  }

  public void setQueueConsumer(Consumer<U> queueConsumer) {
    this.queueConsumer = queueConsumer;
  }
}
