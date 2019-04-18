/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class DynamicEntryTransform implements BitTransform<Map.Entry>{

  @Override
  public Predicate<Class> matching() {
    return Map.Entry.class::isAssignableFrom;
  }


  @Override
  public BiConsumer<Map.Entry, BitBuffer> boxing() {
    return (e,b) -> {
      int startPos = b.position();
      Class kclass = e.getKey().getClass();
      Class vclass = e.getValue().getClass();
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      BitTransform ktran = BoxRegistry.INSTANCE.getAnyTransform(kclass);
      BitTransform vtran = BoxRegistry.INSTANCE.getAnyTransform(vclass);
      b.position(startPos + Integer.BYTES);
      ctran.boxing().accept(kclass, b);
      ktran.boxing().accept(e.getKey(), b);
      int vpos = b.position();
      ctran.boxing().accept(vclass, b);
      vtran.boxing().accept(e.getValue(), b);
      b.putInt(startPos, vpos);
    };
  }


  @Override
  public Function<BitBuffer, Map.Entry> unboxing() {
    return b -> {
      int vpos = b.getInt();
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      Class kclass = ctran.unboxing().apply(b);
      int kpos = b.position();
      Class vclass = ctran.unboxing().apply(b.position(vpos));
      vpos = b.position();
      BitTransform ktran = BoxRegistry.INSTANCE.getAnyTransform(kclass);
      BitTransform vtran = BoxRegistry.INSTANCE.getAnyTransform(vclass);
      Object k = ktran.unboxing().apply(b.position(kpos));
      Object v = vtran.unboxing().apply(b.position(vpos));
      return new AbstractMap.SimpleImmutableEntry<>(k, v);
    };
  }
  
}
