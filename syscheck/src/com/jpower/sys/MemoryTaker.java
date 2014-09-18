/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.io.Serializable;


/**
 *
 * @author juno
 */
public class MemoryTaker implements Serializable, Taker<Memory> {
  
  public static final String CMD = "free";
  
  public static final String[] ARGS = { "-m" };
  
  private SystemRun cmd;
  
  private Memory mem;
  
  
  public MemoryTaker() {
    cmd = new SystemRun(CMD, ARGS);
  }
  
  
  @Override
  public Memory take() {
    cmd.run();
    MemoryParser parser = new MemoryParser();
    mem = parser.parse(cmd.getOutput());
    return mem;
  }
  
  
  @Override
  public Memory get() {
    return mem;
  }
  
  
  public static void main(String[] args) {
    MemoryTaker mt = new MemoryTaker();
    System.out.println(mt.take());
  }
  
}
