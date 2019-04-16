/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Indexed;
import us.pserver.tools.IndexedInt;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class IntArrayTransform implements BitTransform<int[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == int.class;
  }


  @Override
  public BiConsumer<int[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      IntStream.of(a).forEach(b::putInt);
    };
  }
  
  @Override
  public Function<BitBuffer, int[]> unboxing() {
    return b -> {
      int startPos = b.position();
      int size = b.getInt();
      int[] a = new int[size];
      IntFunction<IndexedInt> fid = IndexedInt.builder();
      IntStream.generate(b::getInt)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> a[x.index()] = x.value());
      return a;
    };
  }
  
}
