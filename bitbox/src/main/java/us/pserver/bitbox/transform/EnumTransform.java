/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class EnumTransform<T extends Enum> implements BitTransform<T>{

  @Override
  public Predicate<Class> matching() {
    return c -> c.isEnum();
  }


  @Override
  public BiConsumer<T, BitBuffer> boxing() {
    return (e,b) -> {
      Class<T> c = (Class<T>) e.getClass();
      BitTransform<String> stran = BoxRegistry.INSTANCE.getAnyTransform(String.class);
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      stran.boxing().accept(e.name(), b);
      ctran.boxing().accept(c, b);
    };
  }


  @Override
  public Function<BitBuffer, T> unboxing() {
    return b -> {
      BitTransform<String> stran = BoxRegistry.INSTANCE.getAnyTransform(String.class);
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      String name = stran.unboxing().apply(b);
      Class<T> c = (Class<T>) ctran.unboxing().apply(b);
      return (T) Enum.valueOf(c, name);
    };
  }
  
}
