/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.inspect.GetterTarget;
import us.pserver.bitbox.inspect.ObjectSpec;
import us.pserver.tools.Indexed;


/**
 *
 * @author juno
 */
public class GlobalObjectTransform implements BitTransform<Object> {
  
  private final ClassTransform ctran = new ClassTransform();
  
  private final PolymorphMapTransform dtran = new PolymorphMapTransform();
  
  @Override
  public boolean match(Class c) {
    return !BoxRegistry.INSTANCE.containsTransform(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  private ObjectSpec getOrCreateSpec(Class c) {
    Optional<ObjectSpec> opt = BoxRegistry.INSTANCE.specFor(c);
    if(opt.isEmpty()) {
      ObjectSpec spec = ObjectSpec.createSpec(c);
      BoxRegistry.INSTANCE.addSpec(spec);
      return spec;
    }
    return opt.get();
  }
  
  @Override
  public int box(Object o, BitBuffer b) {
    if(o == null) {
      b.putInt(0);
      return Integer.BYTES;
    }
    Class c = o.getClass();
    ObjectSpec spec = getOrCreateSpec(c);
    Map<String,Object> m = new TreeMap<>();
    Set<GetterTarget> getters = spec.getters();
    getters.stream()
        .map(g -> new AbstractMap.SimpleEntry<>(g.getName(), g.apply(o)))
        .filter(e -> e.getKey() != null && e.getValue() != null)
        .forEach(e -> m.put(e.getKey(), e.getValue()));
    int startPos = b.position();
    int len = Integer.BYTES;
    b.position(startPos + Integer.BYTES);
    len += ctran.box(spec.serialType().orElse(c), b);
    int mpos = b.position();
    len += dtran.box(m, b);
    b.putInt(startPos, mpos);
    b.position(startPos + len);
    return len;
  }
  
  @Override
  public Object unbox(BitBuffer b) {
    int pos = b.position();
    int mpos = b.getInt();
    if(mpos == 0) return null;
    b.position(pos + Integer.BYTES);
    Class c = ctran.unbox(b);
    Map m = dtran.unbox(b);
    ObjectSpec spec = getOrCreateSpec(c);
    Object[] args = new Object[spec.constructor().getParameterTypes().size()];
    Function<String,Indexed<String>> indexed = Indexed.builder();
    Stream<String> pars = spec.constructor().getParameterNames().stream();
    pars.map(indexed)
        .forEach(x -> args[x.index()] = m.get(x.value()));
    return spec.constructor().apply(args);
  }
  
}
