/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;
import us.pserver.bitbox.Reference;
import us.pserver.bitbox.ReferenceBitBox;
import us.pserver.bitbox.impl.AbstractReferenceService;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class TestReferenceBitBox extends AbstractReferenceService {
  
  private static final Random rdm = new Random();
  
  private static final Set<Reference> refs = new HashSet<>();
  
  private final ReferenceBitBox bitb = ReferenceBitBox.of(this);
  
  
  public TestReferenceBitBox() {
    super(c -> {
      Reference r = Reference.of(rdm.nextLong(), c, BitBuffer.of(256, true));
      refs.add(r);
      return r;
    }, (Long i, Class c) -> {
      return refs.stream()
          .filter(r -> r.getId() == i && r.getType() == c)
          .peek(r -> r.getBuffer().position(0))
          .findAny()
          .orElseThrow();
    });
    bitb.configure().lookup(MethodHandles.lookup());
  }
  
  
  @Test
  public void test() {
    try {
      Address a = new Address("Cond Mini Chacaras", new int[]{21}, "Altiplano Leste", "7-4-21", "Brasilia", Address.UF.DF, 71680621);
      Person p = new Person(Arrays.asList(a), LocalDate.of(1980, 7, 7), "Roesler", "Juno");
      Logger.debug(p);
      Reference r = bitb.box(p);
      Logger.debug(r);
      Person q = bitb.unbox(r);
      Logger.debug(q);
      Assertions.assertEquals(p, q);
    }
    catch(Throwable t) {
      t.printStackTrace();
      throw t;
    }
  }
  
}
