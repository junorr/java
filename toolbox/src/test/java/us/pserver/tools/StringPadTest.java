/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;



/**
 *
 * @author juno
 */
public class StringPadTest {
  
  public StringPadTest() {
  }


  @Test
  public void testLpad() {
    System.out.println("lpad");
    String pad = "-";
    int length = 10;
    StringPad instance = StringPad.of("hello");
    String expResult = "-----hello";
    String result = instance.lpad(pad, length);
    assertEquals(expResult, result);
  }


  //@Test
  public void testRpad() {
    System.out.println("rpad");
    String pad = "";
    int length = 0;
    StringPad instance = null;
    String expResult = "";
    String result = instance.rpad(pad, length);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  //@Test
  public void testCpad() {
    System.out.println("cpad");
    String pad = "";
    int length = 0;
    StringPad instance = null;
    String expResult = "";
    String result = instance.cpad(pad, length);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  //@Test
  public void testConcat() {
    System.out.println("concat");
    String sep = "";
    int length = 0;
    String[] args = null;
    StringPad instance = null;
    String expResult = "";
    String result = instance.concat(sep, length, args);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
