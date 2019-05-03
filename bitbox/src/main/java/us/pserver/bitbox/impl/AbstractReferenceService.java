/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import us.pserver.bitbox.Reference;
import us.pserver.bitbox.ReferenceService;


/**
 *
 * @author juno
 */
public abstract class AbstractReferenceService implements ReferenceService {
  
  private final Set<Consumer<Reference>> listeners;
  
  private final Function<Class,Reference> alloc;
  
  private final BiFunction<Long,Class,Reference> recover;
  
  public AbstractReferenceService(Function<Class,Reference> alloc, BiFunction<Long,Class,Reference> recover) {
    this.listeners = new CopyOnWriteArraySet<>();
    this.alloc = Objects.requireNonNull(alloc);
    this.recover = Objects.requireNonNull(recover);
  }
  
  @Override
  public Reference allocate(Class c) {
    Reference ref = alloc.apply(c);
    listeners.forEach(cs -> cs.accept(ref));
    return ref;
  }
  
  @Override
  public Reference getFor(long id, Class c) {
    return recover.apply(id, c);
  }
  
  @Override
  public ReferenceService addAllocationListener(Consumer<Reference> cs) {
    this.listeners.add(Objects.requireNonNull(cs));
    return this;
  }
  
  @Override
  public boolean removeAllocationListener(Consumer<Reference> cs) {
    return this.listeners.add(cs);
  }
  
}
