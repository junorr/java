/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.transform.GetterTarget;
import us.pserver.bitbox.transform.GlobalObjectTransform;
import us.pserver.bitbox.transform.ObjectSpec;


/**
 *
 * @author juno
 */
public class TestObjectSpec {
  
  @Test
  public void test_getter_scan() {
    try {
      BoxRegistry.INSTANCE.lookup(MethodHandles.lookup());
      ObjectSpec<Person> spec = ObjectSpec.createSpec(Person.class);
      Set<GetterTarget<Person,Object>> getters = spec.getters();
      getters.forEach(System.out::println);
      System.out.printf("* contructor: %s%n", spec.constructor());
    }
    catch(Throwable e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void test_object_transform() {
    try {
      BitBuffer buf = BitBuffer.of(512, true);
      Address adr = new Address(
          "Cond. Mini Chacaras do Lago Sul", 21, 
          "Altiplano Leste", "7-4-21", 
          "Brasilia", Address.UF.DF, 71680621
      );
      Person per = new Person("Juno", "Roesler", LocalDate.of(1980, 7, 7), Arrays.asList(adr));
      System.out.println(per);
      BitTransform<Person> trans = BoxRegistry.INSTANCE.getAnyTransform(Person.class);
      trans.boxing().accept(per, buf);
      System.out.println(trans.unboxing().apply(buf.position(0)));
    }
    catch(Throwable e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
