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
public class ShortArrayTransform implements BitTransform<short[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == short.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(short[] ss, BitBuffer buf) {
    buf.putInt(ss.length);
    IntStream.range(0, ss.length).forEach(i -> buf.putShort(ss[i]));
    return Integer.BYTES + ss.length * Short.BYTES;
  }
  
  @Override
  public short[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    short[] ss = new short[len];
    IntStream.range(0, len).forEach(i -> ss[i] = buf.getShort());
    return ss;
  }
  
}
