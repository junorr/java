/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class DynamicEntryTransform implements BitTransform<Map.Entry>{
  
  private final ClassTransform ctran = new ClassTransform();

  @Override
  public boolean match(Class c) {
    return Map.Entry.class.isAssignableFrom(c);
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.of(Map.Entry.class);
  }
  
  @Override
  public int box(Map.Entry e, BitBuffer b) {
    Objects.requireNonNull(e, "Bad null Map.Entry");
    Objects.requireNonNull(e.getKey(), "Bad null Map.Entry.KEY");
    int startPos = b.position();
    int len = Integer.BYTES;
    Class kclass = e.getKey().getClass();
    Class vclass = e.getValue() != null ? e.getValue().getClass() : Void.class;
    System.out.printf("!!  box< %s, %s > !!%n", kclass, vclass);
    BitTransform ktran = BoxRegistry.INSTANCE.getAnyTransform(kclass);
    BitTransform vtran = BoxRegistry.INSTANCE.getAnyTransform(vclass);
    b.position(startPos + Integer.BYTES);
    len += ctran.box((Class)ktran.serialType().orElse(kclass), b);
    len += ktran.box(e.getKey(), b);
    int vpos = b.position();
    len += ctran.box((Class)vtran.serialType().orElse(vclass), b);
    len += vtran.box(e.getValue(), b);
    b.putInt(startPos, vpos);
    b.position(startPos + len);
    return len;
  }


  @Override
  public Map.Entry unbox(BitBuffer b) {
    int vpos = b.getInt();
    Class kclass = ctran.unbox(b);
    int kpos = b.position();
    Class vclass = ctran.unbox(b.position(vpos));
    System.out.printf("!!  unbox< %s, %s > !!%n", kclass, vclass);
    System.getLogger(getClass().getName()).log(System.Logger.Level.DEBUG, "unbox< %s, %s >", kclass, vclass);
    vpos = b.position();
    BitTransform ktran = BoxRegistry.INSTANCE.getAnyTransform(kclass);
    BitTransform vtran = BoxRegistry.INSTANCE.getAnyTransform(vclass);
    Object k = ktran.unbox(b.position(kpos));
    Object v = vtran.unbox(b.position(vpos));
    return new AbstractMap.SimpleImmutableEntry<>(k, v);
  }
  
}
