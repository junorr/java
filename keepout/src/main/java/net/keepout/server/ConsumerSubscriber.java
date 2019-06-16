/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import net.keepout.client.Unchecked;


/**
 *
 * @author juno
 */
public class ConsumerSubscriber<T> implements Flow.Subscriber<T> {
    
  private final Consumer<T> consumer;
  
  private final Consumer<Throwable> error;
  
  private final Runnable complete;

  private Flow.Subscription subs;
  
  
  public ConsumerSubscriber(Consumer<T> cs, Consumer<Throwable> error, Runnable complete) {
    this.consumer = Objects.requireNonNull(cs);
    this.error = error;
    this.complete = complete;
    subs = null;
  }

  public ConsumerSubscriber(Consumer<T> cs, Consumer<Throwable> error) {
    this(cs, error, null);
  }

  public ConsumerSubscriber(Consumer<T> cs) {
    this(cs, null, null);
  }
  
  
  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subs = subscription;
    subs.request(1);
  }

  @Override
  public void onNext(T item) {
    System.out.printf("[%s] onNext: %s%n", getClass().getSimpleName(), item);
    consumer.accept(item);
    subs.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    if(error != null) {
      error.accept(throwable);
    }
    else {
      throw Unchecked.unchecked(throwable);
    }
  }

  @Override
  public void onComplete() {
    if(complete != null) complete.run();
  }

}