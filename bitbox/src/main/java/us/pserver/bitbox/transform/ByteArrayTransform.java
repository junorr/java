/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.IndexedInt;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class ByteArrayTransform implements BitTransform<byte[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == int.class;
  }


  @Override
  public BiConsumer<byte[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      b.put(a);
    };
  }
  
  @Override
  public Function<BitBuffer, byte[]> unboxing() {
    return b -> {
      int size = b.getInt();
      byte[] a = new byte[size];
      b.get(a);
      return a;
    };
  }
  
}
