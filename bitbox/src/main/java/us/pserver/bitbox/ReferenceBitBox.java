/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public interface ReferenceBitBox {
  
  public Reference box(Object obj);
  
  public void box(Object obj, Consumer<Reference> cs);
  
  public <T> T unbox(BitBuffer buf);
  
  public <T> T unbox(Reference ref);
  
  
  public static ReferenceBitBox of(ReferenceService service) {
    return new ReferenceBitBox() {
      @Override
      public Reference box(Object obj) {
        Reference
        
      }
      @Override
      public void box(Object obj, Consumer<Reference> cs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }
      @Override
      public <T> T unbox(BitBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }
    };
  }
  
}
