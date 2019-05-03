/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Indexed;


/**
 *
 * @author juno
 */
public class PolymorphCollectionTransform implements BitTransform<Collection> {
  
  private final PolymorphNodeTransform ptran = new PolymorphNodeTransform();
  
  @Override
  public boolean match(Class c) {
    return Collection.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.of(Collection.class);
  }
  
  @Override
  public int box(Collection c, BitBuffer buf) {
    if(c == null || c.isEmpty()) {
      buf.putInt(0);
      return Integer.BYTES;
    }
    int pos = buf.position();
    int len = Integer.BYTES * (c.size() + 1);
    buf.putInt(c.size());
    int[] idx = new int[c.size()];
    buf.position(pos + len);
    Function<Object,Indexed<Object>> ibd = Indexed.builder();
    Stream<Indexed<Object>> stream = c.stream().map(ibd);
    len = stream.peek(i -> idx[i.index()] = buf.position())
        .mapToInt(i -> ptran.box(i.value(), buf))
        .reduce(len, Integer::sum);
    buf.position(pos + Integer.BYTES);
    IntStream.of(idx).forEach(buf::putInt);
    buf.position(pos + len);
    return len;
  }
  
  @Override
  public Collection unbox(BitBuffer buf) {
    int pos = buf.position();
    int size = buf.getInt();
    if(size == 0) {
      return Collections.EMPTY_LIST;
    }
    int[] idx = new int[size];
    IntStream.range(0, size)
        .forEach(i -> idx[i] = buf.getInt());
    return IntStream.of(idx)
        .peek(buf::position)
        .mapToObj(i -> ptran.unbox(buf))
        .collect(Collectors.toList());
  }
  
}
