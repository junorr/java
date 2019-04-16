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
public class ShortArrayTransform implements BitTransform<short[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == short.class;
  }


  @Override
  public BiConsumer<short[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      for(int i = 0; i < a.length; i++) {
        b.putShort(a[i]);
      }
    };
  }
  
  @Override
  public Function<BitBuffer, short[]> unboxing() {
    return b -> {
      int size = b.getInt();
      short[] a = new short[size];
      IntFunction<IndexedInt> fid = IndexedInt.builder();
      IntStream.generate(b::getShort)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> a[x.index()] = (short) x.value());
      return a;
    };
  }
  
}
