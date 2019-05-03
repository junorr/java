/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.function.Consumer;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.impl.ReferenceBitBoxImpl;


/**
 *
 * @author juno
 */
public interface ReferenceBitBox {
  
  public BitBoxConfiguration configure();
  
  public Reference box(Object obj);
  
  public void box(Object obj, Consumer<Reference> cs);
  
  public <T> T unbox(BitBuffer buf);
  
  public <T> T unbox(Reference ref);
  
  
  public static ReferenceBitBox of(ReferenceService service) {
    return new ReferenceBitBoxImpl(service);
  }
  
  
  public static ReferenceBitBox of(ReferenceService service, BitBoxConfiguration cfg) {
    return new ReferenceBitBoxImpl(service, cfg);
  }
  
}
