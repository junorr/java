/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.IndexedLong;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.stream.LongStream;


/**
 *
 * @author juno
 */
public class LongArrayTransform implements BitTransform<long[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == long.class;
  }


  @Override
  public BiConsumer<long[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      LongStream.of(a).forEach(b::putLong);
    };
  }
  
  @Override
  public Function<BitBuffer, long[]> unboxing() {
    return b -> {
      int size = b.getInt();
      long[] a = new long[size];
      LongFunction<IndexedLong> fid = IndexedLong.builder();
      LongStream.generate(b::getLong)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> a[x.index()] = x.value());
      return a;
    };
  }
  
}
