/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;
import us.pserver.bitbox.annotation.BitIgnore;


/**
 *
 * @author juno
 */
public class TestAnnotations {
  
  @Test
  public void test_annotations() throws Exception {
    Method m = IPerson.class.getMethod("getBirth");
    Logger.debug("{}() : {} .isAnnotationPresent( {} ): {}", 
        m.getName(), 
        m.getReturnType().getSimpleName(), 
        BitIgnore.class, 
        m.isAnnotationPresent(BitIgnore.class)
    );
    Arrays.asList(m.getDeclaredAnnotations()).forEach(Logger::debug);
  }
  
}
