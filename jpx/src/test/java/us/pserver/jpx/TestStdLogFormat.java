/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jpx;

import us.pserver.jpx.log.LogFormat;
import us.pserver.jpx.log.Log;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


/**
 *
 * @author juno
 */
public class TestStdLogFormat {
  
  private final String fmt = "@LVL (@TMS) [@CLS.@MTH]-@LNR: @MSG";
  
  
  public TestStdLogFormat() {
  }


  @Test
  public void testGetMessage() {
    System.out.println("getMessage");
    LogFormat instance = new LogFormat(fmt);
    String expResult = fmt;
    String result = instance.getMessage();
    assertEquals(expResult, result);
  }


  @Test
  public void testParse() {
    System.out.println("parse");
    LogFormat instance = new LogFormat(fmt);
    List<LogFormat.Var> expResult = Arrays.asList(LogFormat.Var.LEVEL,
        LogFormat.Var.INSTANT,
        LogFormat.Var.CLASS,
        LogFormat.Var.METHOD,
        LogFormat.Var.LINE_NUMBER,
        LogFormat.Var.MESSAGE
    );
    List<LogFormat.Var> result = instance.parse();
    assertEquals(expResult, result);
  }
  
  @Test
  public void testFormat() {
    System.out.println("format");
    LogFormat instance = new LogFormat(fmt);
    Instant inst = Instant.now();
    String msg = "Hello";
    StackTraceElement elm = Thread.currentThread().getStackTrace()[1];
    String expResult = String.format("INFO (%s) [%s.%s]-%d: %s", 
        inst, 
        elm.getClassName(),
        elm.getMethodName(),
        elm.getLineNumber(),
        msg
    );
    System.out.println("* expResult: "+ expResult);
    String result = instance.format(Log.Level.INFO, "us.pserver.jpx.TestStdLogFormat", "testFormat", 61, msg, inst);
    System.out.println("* result: "+ result);
    assertEquals(expResult, result);
  }
  
}
