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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class CollectionTransform<T> implements BitTransform<Collection<T>> {
  
  @Override
  public Predicate<Class> matching() {
    return Collection.class::isAssignableFrom;
  }


  @Override
  public BiConsumer<Collection<T>, BitBuffer> boxing() {
    return (c,b) -> {
      int startPos = b.position();
      Class<T> cls = (Class<T>) c.stream()
          .map(Object::getClass)
          .findAny()
          .orElseThrow(IllegalStateException::new);
      Function<T,Indexed<T>> fid = Indexed.builder();
      BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
      BitTransform<Class> ct = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      b.position(startPos + (c.size() + 2) * Integer.BYTES);
      int[] idxs = new int[c.size()];
      c.stream()
          .map(fid::apply)
          .peek(x -> idxs[x.index()] = b.position())
          .forEach(x -> trans.boxing().accept(x.value(), b));
      int cpos = b.position();
      ct.boxing().accept(cls, b);
      int end = b.position();
      b.position(startPos).putInt(c.size()).putInt(cpos);
      IntStream.of(idxs).forEach(b::putInt);
      b.position(end);
    };
  }
  
  public void debugBoxingOutput(BitBuffer out) {
    int size = out.getInt();
    int classPos = out.getInt();
    System.out.printf("* size=%d%n", size);
    System.out.printf("* classPos=%d%n", classPos);
    int[] idxs = new int[size];
    IntFunction<IndexedInt> fid = IndexedInt.builder();
    IntStream.generate(out::getInt)
        .mapToObj(fid::apply)
        .takeWhile(x -> x.index() < size)
        .forEach(x -> idxs[x.index()] = x.value());
    System.out.printf("* value indexes: %s%n", Arrays.toString(idxs));
  }
  
  @Override
  public Function<BitBuffer, Collection<T>> unboxing() {
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
      List<T> ls = new ArrayList<>(size);
      BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
      fid = IndexedInt.builder();
      IntStream.of(idxs)
          .mapToObj(i -> trans.unboxing().apply(b.position(i)))
          .forEach(ls::add);
      return ls;
    };
  }
  
}
