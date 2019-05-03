/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import us.pserver.bitbox.impl.AbstractReferenceService;


/**
 *
 * @author juno
 */
public interface ReferenceService {
  
  public Reference allocate(Class c);
  
  public Reference getFor(long id, Class c);
  
  public ReferenceService addAllocationListener(Consumer<Reference> cs);
  
  public boolean removeAllocationListener(Consumer<Reference> cs);
  
  
  
  public static ReferenceService of(Function<Class,Reference> alloc, BiFunction<Long,Class,Reference> recover) {
    return new AbstractReferenceService(alloc, recover) {};
  }
  
}
