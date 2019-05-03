/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.impl;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import us.pserver.bitbox.BitBoxConfiguration;
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
  
  private final BitBoxConfiguration cfg;
  
  
  public ReferenceBitBoxImpl(ReferenceService service) {
    this(service, new BitBoxConfiguration());
  }
  
  
  public ReferenceBitBoxImpl(ReferenceService service, BitBoxConfiguration cfg) {
    this.service = Objects.requireNonNull(service);
    this.cfg = Objects.requireNonNull(cfg);
    this.cfg.addTransform(new ReferenceTransform(service, cfg));
    this.cfg.setGlobalTransform(new ReferenceObjectTransform(service, cfg));
  }
  
  
  public BitBoxConfiguration configure() {
    return cfg;
  }
  

  @Override
  public Reference box(Object obj) {
    BitBuffer buf = BitBuffer.of(128, false);
    BitTransform tran = cfg.getGlobalTransform();
    tran.box(obj, buf);
    BitTransform<Reference> rtran = cfg.getTransform(Reference.class);
    return rtran.unbox(buf.position(0));
  }


  @Override
  public void box(Object obj, Consumer<Reference> cs) {
    service.addAllocationListener(cs);
    box(obj);
    service.removeAllocationListener(cs);
  }


  @Override
  public <T> T unbox(BitBuffer buf) {
    BitTransform<Reference> rtran = cfg.getTransform(Reference.class);
    Reference ref = rtran.unbox(buf);
    BitTransform tran = cfg.getTransform(ref.getType());
    return (T) tran.unbox(ref.getBuffer());
  }
  

  @Override
  public <T> T unbox(Reference ref) {
    BitTransform<Reference> rtran = cfg.getTransform(Reference.class);
    BitTransform tran = cfg.getGlobalTransform();
    BitBuffer buf = BitBuffer.of(128, false);
    rtran.box(ref, buf);
    return (T) tran.unbox(buf.position(0));
  }
  
}
