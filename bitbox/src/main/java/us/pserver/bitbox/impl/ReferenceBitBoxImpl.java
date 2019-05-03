/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.impl;

import java.util.Objects;
import java.util.function.Consumer;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.Reference;
import us.pserver.bitbox.ReferenceBitBox;
import us.pserver.bitbox.ReferenceService;
import us.pserver.bitbox.transform.ReferenceObjectTransform;
import us.pserver.bitbox.transform.ReferenceTransform;


/**
 *
 * @author juno
 */
public class ReferenceBitBoxImpl implements ReferenceBitBox {
  
  private final ReferenceService service;
  
  
  public ReferenceBitBoxImpl(ReferenceService service) {
    this.service = Objects.requireNonNull(service);
    BitBoxRegistry.INSTANCE.addTransform(new ReferenceTransform(service));
    BitBoxRegistry.INSTANCE.setGlobal(new ReferenceObjectTransform(service));
  }
  

  @Override
  public Reference box(Object obj) {
    Reference ref = service.allocate(obj.getClass());
    BitTransform tran = BitBoxRegistry.INSTANCE.getAnyTransform(obj.getClass());
    tran.box(obj, ref.getBuffer());
    return ref;
  }


  @Override
  public void box(Object obj, Consumer<Reference> cs) {
    Class cls = obj.getClass();
    Consumer<Reference> notify = r -> {
      if(r.getType() == cls) cs.accept(r);
    };
    service.addAllocationListener(notify);
    box(obj);
    service.removeAllocationListener(notify);
  }


  @Override
  public <T> T unbox(BitBuffer buf) {
    BitTransform<Reference> rtran = BitBoxRegistry.INSTANCE.getAnyTransform(Reference.class);
    Reference ref = rtran.unbox(buf);
    BitTransform tran = BitBoxRegistry.INSTANCE.getAnyTransform(ref.getType());
    return (T) tran.unbox(ref.getBuffer());
  }
  

  @Override
  public <T> T unbox(Reference ref) {
    return unbox(ref.getBuffer());
  }
  
}
