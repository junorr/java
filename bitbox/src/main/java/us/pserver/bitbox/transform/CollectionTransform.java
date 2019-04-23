/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author juno
 */
public class CollectionTransform<T> implements BitTransform<Collection<T>> {
  
  private final ClassTransform ctran = new ClassTransform();
  
  @Override
  public boolean match(Class c) {
    return Collection.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.of(Collection.class);
  }
  
  @Override
  public int box(Collection<T> c, BitBuffer buf) {
    if(c == null || c.isEmpty()) {
      buf.putInt(0);
      return Integer.BYTES;
    }
    Class<T> cls = (Class<T>) c.stream()
        .map(Object::getClass)
        .findAny().get();
    BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
    int pos = buf.position();
    int len = Integer.BYTES * (c.size() + 1);
    buf.position(pos + len);
    len += ctran.box(trans.serialType().orElse(cls), buf);
    int[] idx = new int[c.size()];
    int i = 0;
    for(T o : c) {
      idx[i++] = buf.position();
      len += trans.box(o, buf);
    }
    buf.position(pos).putInt(c.size());
    IntStream.of(idx).forEach(buf::putInt);
    buf.position(pos + len);
    return len;
  }
  
  @Override
  public Collection<T> unbox(BitBuffer buf) {
    int pos = buf.position();
    int size = buf.getInt();
    if(size == 0) {
      return Collections.EMPTY_LIST;
    }
    buf.position(pos + Integer.BYTES * (size + 1));
    Class<T> cls = ctran.unbox(buf);
    BitTransform<T> trans = BoxRegistry.INSTANCE.getAnyTransform(cls);
    return IntStream.range(0, size)
        .mapToObj(i -> trans.unbox(buf))
        .collect(Collectors.toList());
  }
  
}
