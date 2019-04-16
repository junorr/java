/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 *
 * @author juno
 */
public class Lambdas {
  
  public static interface Consumer1<A> extends Consumer<A> {
    public void accept(A a);
  }
  
  public static interface Consumer2<A,B> extends BiConsumer<A,B> {
    public void accept(A a, B b);
  }
  
  public static interface Consumer3<A,B,C> {
    public void accept(A a, B b, C c);
  }
  
  public static interface Consumer4<A,B,C,D> {
    public void accept(A a, B b, C c, D d);
  }
  
  public static interface Consumer5<A,B,C,D,E> {
    public void accept(A a, B b, C c, D d, E e);
  }
  
  public static interface Consumer6<A,B,C,D,E,F> {
    public void accept(A a, B b, C c, D d, E e, F f);
  }
  
  public static interface Consumer7<A,B,C,D,E,F,G> {
    public void accept(A a, B b, C c, D d, E e, F f, G g);
  }
  
  public static interface Consumer8<A,B,C,D,E,F,H> {
    public void accept(A a, B b, C c, D d, E e, F f, H h);
  }
  
  
  public static interface Function1<A,R> extends Function<A,R> {
    public R apply(A a);
  }
  
  public static interface Function2<A,B,R> extends BiFunction<A,B,R> {
    public R apply(A a, B b);
  }
  
  public static interface Function3<A,B,C,R> {
    public R apply(A a, B b, C c);
  }
  
  public static interface Function4<A,B,C,D,R> {
    public R accept(A a, B b, C c, D d);
  }
  
  public static interface Function5<A,B,C,D,E,R> {
    public R accept(A a, B b, C c, D d, E e);
  }
  
  public static interface Function6<A,B,C,D,E,F,R> {
    public R accept(A a, B b, C c, D d, E e, F f);
  }
  
  public static interface Function7<A,B,C,D,E,F,G,R> {
    public R accept(A a, B b, C c, D d, E e, F f, G g);
  }
  
  public static interface Function8<A,B,C,D,E,F,G,H,R> {
    public R accept(A a, B b, C c, D d, E e, F f, G g, H h);
  }
  
}
