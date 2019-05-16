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
public class FloatArrayTransform implements BitTransform<float[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == float.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(float[] ds, BitBuffer buf) {
    buf.putInt(ds.length);
    IntStream.range(0, ds.length).forEach(i -> buf.putFloat(ds[i]));
    return Integer.BYTES + ds.length * Float.BYTES;
  }
  
  @Override
  public float[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    float[] ds = new float[len];
    IntStream.range(0, len).forEach(i -> ds[i] = buf.getFloat());
    return ds;
  }
  
}
