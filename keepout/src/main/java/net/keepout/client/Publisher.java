/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.UnaryOperator;


/**
 *
 * @author juno
 */
public class Publisher<T> implements Flow.Publisher<T> {
  
  private final List<T> items;
  
  private final UnaryOperator<T> oper;
  
  public Publisher(Collection<T> items, UnaryOperator<T> op) {
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
    this.oper = op != null ? op : UnaryOperator.identity();
  }

  public Publisher(T item, UnaryOperator<T> op) {
    this(Arrays.asList(item), op);
  }

  public Publisher(T item) {
    this(Arrays.asList(item), null);
  }

  @Override
  public void subscribe(Flow.Subscriber<? super T> subscriber) {
    Flow.Subscription subs = new Flow.Subscription() {
      private final Iterator<T> it = items.stream().iterator();
      private volatile boolean canceled = false;
      @Override
      public void request(long n) {
        for(int i = 0; i < n && it.hasNext() && !canceled; i++) {
          subscriber.onNext(oper.apply(it.next()));
        }
        if(!it.hasNext()) subscriber.onComplete();
      }
      @Override
      public void cancel() {
        canceled = true;
      }
    };
    subscriber.onSubscribe(subs);
  }

}