/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;


/**
 *
 * @author juno
 */
public class ArrayTransform<T> implements BitTransform<T[]> {
  
  private final BitBoxConfiguration cfg;
  
  public ArrayTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
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
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    BitTransform<T> trans = cfg.getTransform(cls);
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
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    Class<T> cls = ctran.unbox(buf);
    BitTransform<T> trans = cfg.getTransform(cls);
    T[] ts = (T[]) Array.newInstance(cls, size);
    IntStream.range(0, size)
        .forEach(i -> ts[i] = trans.unbox(buf));
    return ts;
  }
  
}
