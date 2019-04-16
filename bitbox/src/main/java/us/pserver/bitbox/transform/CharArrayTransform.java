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
public class CharArrayTransform implements BitTransform<char[]> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c.isArray() && c.getComponentType() == char.class;
  }


  @Override
  public BiConsumer<char[], BitBuffer> boxing() {
    return (a,b) -> {
      b.putInt(a.length);
      for(int i = 0; i < a.length; i++) {
        b.putChar(a[i]);
      }
    };
  }
  
  @Override
  public Function<BitBuffer, char[]> unboxing() {
    return b -> {
      int startPos = b.position();
      int size = b.getInt();
      char[] a = new char[size];
      IntFunction<IndexedInt> fid = IndexedInt.builder();
      IntStream.generate(b::getChar)
          .mapToObj(fid::apply)
          .takeWhile(x -> x.index() < size)
          .forEach(x -> a[x.index()] = (char) x.value());
      return a;
    };
  }
  
}
