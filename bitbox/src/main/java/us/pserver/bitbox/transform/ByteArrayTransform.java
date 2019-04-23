/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Optional;
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
public class ByteArrayTransform implements BitTransform<byte[]> {
  
  @Override
  public boolean match(Class c) {
    return c.isArray() && c.getComponentType() == byte.class;
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(byte[] bs, BitBuffer buf) {
    buf.putInt(bs.length);
    buf.put(bs);
    return bs.length + Integer.BYTES;
  }
  
  @Override
  public byte[] unbox(BitBuffer buf) {
    int len = buf.getInt();
    byte[] bs = new byte[len];
    buf.get(bs);
    return bs;
  }
  
}
