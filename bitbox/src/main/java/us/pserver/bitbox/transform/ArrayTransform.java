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
public class ArrayTransform<T> implements BitTransform<T[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && !c.getComponentType().isPrimitive();
  }


  @Override
  public BiConsumer<T[], BitBuffer> boxing() {
    return (a,b) -> {
      int startPos = b.position();
      Class<T> cls = (Class<T>) a[0].getClass();
      Function<T,Indexed<T>> fid = Indexed.builder();
      BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
      BitTransform<Class> ct = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      b.position(startPos + (a.length + 2) * Integer.BYTES);
      int[] idxs = new int[a.length];
      Arrays.asList(a).stream()
          .map(fid::apply)
          .peek(x -> idxs[x.index()] = b.position())
          .forEach(x -> trans.boxing().accept(x.value(), b));
      System.out.println(Arrays.toString(idxs));
      int cpos = b.position();
      ct.boxing().accept(cls, b);
      b.position(startPos).putInt(a.length).putInt(cpos);
      IntStream.of(idxs).forEach(b::putInt);
    };
  }
  
  @Override
  public Function<BitBuffer, T[]> unboxing() {
    return b -> {
      int startPos = b.position();
      int size = b.getInt();
      int cpos = b.getInt();
      int ipos = b.position();
      BitTransform<Class> ct = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      Class<T> cls = ct.unboxing().apply(b.position(cpos));
      IntFunction<IndexedInt> fid = IndexedInt.builder();
      b.position(ipos);
      int[] idxs = new int[size];
      IntStream.generate(b::getInt)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> idxs[x.index()] = x.value());
      Object array = Array.newInstance(cls, size);
      BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
      fid = IndexedInt.builder();
      IntStream.of(idxs)
          .mapToObj(fid::apply)
          .forEach(x -> Array.set(array, x.index(),
              trans.unboxing().apply(
                  b.position(x.value())))
          );
      return (T[]) array;
    };
  }
  
}
