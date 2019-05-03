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
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.Reference;
import us.pserver.bitbox.ReferenceService;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.spec.GetterTarget;
import us.pserver.bitbox.spec.ObjectSpec;
import us.pserver.tools.Indexed;


/**
 *
 * @author juno
 */
public class ReferenceObjectTransform implements BitTransform<Object> {
  
  private final ReferenceService service;
  
  private final PolymorphMapTransform dtran;
  
  
  public ReferenceObjectTransform(ReferenceService rs) {
    this.service = Objects.requireNonNull(rs);
    this.dtran = new PolymorphMapTransform();
  }
  
  
  @Override
  public boolean match(Class c) {
    return !BitBoxRegistry.INSTANCE.containsTransform(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  private ObjectSpec getOrCreateSpec(Class c) {
    Optional<ObjectSpec> opt = BitBoxRegistry.INSTANCE.specFor(c);
    if(opt.isEmpty()) {
      ObjectSpec spec = ObjectSpec.createSpec(c);
      BitBoxRegistry.INSTANCE.addSpec(spec);
      return spec;
    }
    return opt.get();
  }
  
  @Override
  public int box(Object o, BitBuffer b) {
    BitTransform<Reference> rtran = BitBoxRegistry.INSTANCE.getAnyTransform(Reference.class);
    if(o == null) {
      return rtran.box(Reference.BAD_REFERENCE, b);
    }
    Class c = o.getClass();
    ObjectSpec spec = getOrCreateSpec(c);
    Reference ref = service.allocate(spec.serialType().orElse(c));
    Map<String,Object> m = new TreeMap<>();
    Set<GetterTarget> getters = spec.getters();
    getters.stream()
        .map(g -> new AbstractMap.SimpleEntry<>(g.getName(), g.apply(o)))
        .filter(e -> e.getKey() != null && e.getValue() != null)
        .forEach(e -> m.put(e.getKey(), e.getValue()));
    BitTransform<Class> ctran = BitBoxRegistry.INSTANCE.getAnyTransform(Class.class);
    ctran.box(spec.serialType().orElse(c), ref.getBuffer());
    dtran.box(m, ref.getBuffer());
    return rtran.box(ref, b);
  }
  
  @Override
  public Object unbox(BitBuffer b) {
    BitTransform<Reference> rtran = BitBoxRegistry.INSTANCE.getAnyTransform(Reference.class);
    Reference ref = rtran.unbox(b);
    if(Reference.BAD_REFERENCE.equals(ref)) {
      return null;
    }
    BitTransform<Class> ctran = BitBoxRegistry.INSTANCE.getAnyTransform(Class.class);
    Class c = ctran.unbox(ref.getBuffer());
    Map m = dtran.unbox(ref.getBuffer());
    ObjectSpec spec = getOrCreateSpec(c);
    Object[] args = new Object[spec.constructor().getParameterTypes().size()];
    Function<String,Indexed<String>> indexed = Indexed.builder();
    Stream<String> pars = spec.constructor().getParameterNames().stream();
    pars.map(indexed)
        .forEach(x -> args[x.index()] = m.get(x.value()));
    return spec.constructor().apply(args);
  }
  
}
