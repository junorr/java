/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 *
 * @author juno
 */
public class GlobalObjectTransform implements BitTransform<Object> {

  @Override
  public Predicate<Class> matching() {
    return c -> !BoxRegistry.INSTANCE.containsTransform(c);
  }
  
  @Override
  public BiConsumer<Object, BitBuffer> boxing() {
    return  null;
  }
  
  @Override
  public Function<BitBuffer, Object> unboxing() {
    return null;
  }
  
}
