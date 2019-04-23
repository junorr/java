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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class ArrayTransform<T> implements BitTransform<T[]> {
  
  private final ClassTransform ctran = new ClassTransform();
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && !c.getComponentType().isPrimitive();
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(T[] ts, BitBuffer buf) {
    Class<T> cls = (Class<T>) ts.getClass().getComponentType();
    BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
    int pos = buf.position();
    int len = Integer.BYTES * (ts.length + 1);
    buf.position(pos + len);
    len += ctran.box(trans.serialType().orElse(cls), buf);
    int[] idx = new int[ts.length];
    int i = 0;
    for(T o : ts) {
      idx[i++] = buf.position();
      len += trans.box(o, buf);
    }
    buf.position(pos).putInt(ts.length);
    IntStream.of(idx).forEach(buf::putInt);
    buf.position(pos + len);
    return len;
  }
  
  @Override
  public T[] unbox(BitBuffer buf) {
    int pos = buf.position();
    int size = buf.getInt();
    buf.position(pos + Integer.BYTES * (size + 1));
    Class<T> cls = ctran.unbox(buf);
    BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
    T[] ts = (T[]) Array.newInstance(cls, size);
    IntStream.range(0, size)
        .forEach(i -> ts[i] = trans.unbox(buf));
    return ts;
  }
  
}
