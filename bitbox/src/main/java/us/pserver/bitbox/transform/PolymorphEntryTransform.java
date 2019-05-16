/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;


/**
 *
 * @author juno
 */
public class PolymorphEntryTransform implements BitTransform<Map.Entry>{
  
  private final BitBoxConfiguration cfg;
  
  public PolymorphEntryTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
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
    //System.out.printf("!!  box< %s, %s > !!%n", kclass, vclass);
    BitTransform ktran = cfg.getTransform(kclass);
    BitTransform vtran = cfg.getTransform(vclass);
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
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
    //Logger.debug("vpos = {}", vpos);
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    Class kclass = ctran.unbox(b);
    //Logger.debug("Entry< {}, Y >", kclass);
    int kpos = b.position();
    Class vclass = ctran.unbox(b.position(vpos));
    //Logger.debug("Entry< {}, {} >", kclass, vclass);
    vpos = b.position();
    BitTransform ktran = cfg.getTransform(kclass);
    BitTransform vtran = cfg.getTransform(vclass);
    //Logger.debug("ktran = {}, vtran = {}", ktran.getClass().getSimpleName(), vtran.getClass().getSimpleName());
    Object k = ktran.unbox(b.position(kpos));
    Object v = vtran.unbox(b.position(vpos));
    //Logger.debug("key = {}, value = {}", k, tostr(v));
    return new AbstractMap.SimpleImmutableEntry<>(k, v);
  }
  
  
  private Object tostr(Object o) {
    if(!o.getClass().isArray()) return o;
    int len = Array.getLength(o);
    StringBuilder sb = new StringBuilder("[");
    for(int i = 0; i < len; i++) {
      sb.append(Array.get(o, i)).append(", ");
    }
    if(sb.indexOf(",") >= 0) {
      sb.delete(sb.length() -2, sb.length());
    }
    return sb.append("]");
  }
  
}
