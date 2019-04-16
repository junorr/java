/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.function.BiConsumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import us.pserver.tools.IndexedDouble;


/**
 *
 * @author juno
 */
public class FloatArrayTransform implements BitTransform<float[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == long.class;
  }


  @Override
  public BiConsumer<float[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      for(int i = 0; i < a.length; i++) {
        b.putFloat(a[i]);
      }
    };
  }
  
  @Override
  public Function<BitBuffer, float[]> unboxing() {
    return b -> {
      int size = b.getInt();
      float[] a = new float[size];
      DoubleFunction<IndexedDouble> fid = IndexedDouble.builder();
      DoubleStream.generate(b::getFloat)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> a[x.index()] = (float) x.value());
      return a;
    };
  }
  
}
