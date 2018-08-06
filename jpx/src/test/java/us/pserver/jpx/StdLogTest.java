/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jpx;

import us.pserver.jpx.log.Log;
import us.pserver.jpx.log.StdLog;
import org.junit.jupiter.api.Test;



/**
 *
 * @author juno
 */
public class StdLogTest {
  
  public StdLogTest() {
  }


  @Test
  public void testLog() {
    System.out.println("log");
    Log.Level lvl = Log.Level.WARN;
    String str = "Aaaahh!!";
    StdLog.STDOUT.log(lvl, str);
  }


  @Test
  public void testDebug() {
    StdLog.STDOUT.debug("Debugging");
  }


  @Test
  public void testInfo() {
    StdLog.STDOUT.info("Hello World");
  }


  @Test
  public void testWarn() {
    StdLog.STDOUT.warn("Aaah!");
  }


  @Test
  public void testError() {
    StdLog.STDOUT.error("Fudeu!");
  }

}
