/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


/**
 *
 * @author juno
 */
public class LongArrayTransform implements BitTransform<long[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == long.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(long[] ls, BitBuffer buf) {
    buf.putInt(ls.length);
    LongStream.of(ls).forEach(buf::putLong);
    return Integer.BYTES + Long.BYTES * ls.length;
  }
  
  @Override
  public long[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    long[] ls = new long[len];
    IntStream.range(0, len).forEach(i -> ls[i] = buf.getLong());
    return ls;
  }
  
}
