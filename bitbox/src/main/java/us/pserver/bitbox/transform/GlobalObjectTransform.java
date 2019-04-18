/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import us.pserver.tools.Attached;
import us.pserver.tools.Indexed;


/**
 *
 * @author juno
 */
public class GlobalObjectTransform implements BitTransform<Object> {

  @Override
  public Predicate<Class> matching() {
    return c -> !BoxRegistry.INSTANCE.containsTransform(c);
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
  public BiConsumer<Object, BitBuffer> boxing() {
    return (o,b) -> {
      Class c = o.getClass();
      ObjectSpec spec = getOrCreateSpec(c);
      Map<String,Object> m = new TreeMap<>();
      Set<GetterTarget> getters = spec.getters();
      getters.stream()
          .forEach(g -> m.put(g.getName(), g.apply(o)));
      DynamicMapTransform dmt = new DynamicMapTransform();
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      int startPos = b.position();
      b.position(startPos + Integer.BYTES);
      ctran.boxing().accept(c, b);
      b.putInt(startPos, b.position());
      dmt.boxing().accept(m, b);
    };
  }
  
  @Override
  public Function<BitBuffer, Object> unboxing() {
    return b -> {
      BitTransform<Class> ctran = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
      Class c = ctran.unboxing().apply(b.position(b.position() + Integer.BYTES));
      DynamicMapTransform dmt = new DynamicMapTransform();
      Map m = dmt.unboxing().apply(b);
      ObjectSpec spec = getOrCreateSpec(c);
      Object[] args = new Object[spec.constructor().getParameters().size()];
      Function<String,Indexed<String>> fid = Indexed.builder();
      Stream<Parameter> pars = spec.constructor().getParameters().stream();
      pars.map(Parameter::getName)
          .map(fid)
          .forEach(x -> args[x.index()] = m.get(x.value()));
      return spec.constructor().apply(args);
    };
  }
  
}
