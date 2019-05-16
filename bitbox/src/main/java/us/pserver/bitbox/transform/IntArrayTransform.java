/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import java.util.stream.IntStream;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;


/**
 *
 * @author juno
 */
public class IntArrayTransform implements BitTransform<int[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == int.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(int[] is, BitBuffer buf) {
    buf.putInt(is.length);
    IntStream.of(is).forEach(buf::putInt);
    return Integer.BYTES + Integer.BYTES * is.length;
  }
  
  @Override
  public int[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    int[] is = new int[len];
    IntStream.range(0, len).forEach(i -> is[i] = buf.getInt());
    return is;
  }
  
}
