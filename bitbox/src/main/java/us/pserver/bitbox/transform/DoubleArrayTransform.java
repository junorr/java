/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class DoubleArrayTransform implements BitTransform<double[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == double.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(double[] ds, BitBuffer buf) {
    buf.putInt(ds.length);
    DoubleStream.of(ds).forEach(buf::putDouble);
    return Integer.BYTES + ds.length * Double.BYTES;
  }
  
  @Override
  public double[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    double[] ds = new double[len];
    IntStream.range(0, len).forEach(i -> ds[i] = buf.getDouble());
    return ds;
  }
  
}
