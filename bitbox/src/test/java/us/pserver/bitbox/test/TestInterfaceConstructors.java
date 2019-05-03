/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.spec.ObjectSpec;
import us.pserver.tools.Reflect;


/**
 *
 * @author juno
 */
public class TestInterfaceConstructors {
  
  @Test
  public void interface_contructors() {
    Constructor[] cs = Reflect.of(IPerson.class).constructors();
    System.out.println(Arrays.asList(cs));
  }
  
  @Test
  public void spec_interface() {
    try {
      ObjectSpec spec = ObjectSpec.createSpec(IPerson.class, new BitBoxConfiguration());
      System.out.println(spec.constructor());
    }
    catch(Throwable t) {
      t.printStackTrace();
      throw t;
    }
  }
  
}
