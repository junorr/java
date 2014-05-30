/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.util.List;


/**
 *
 * @author juno
 */
public class CpuTaker implements Taker<Cpu> {
  
  public static final String CMD_LSCPU = "lscpu";
  
  public static final String CMD_TOP = "top";
  
  public static final String[] ARGS = { "-b", "-n1" };
  
  private SystemRun lscpu;
  
  private SystemRun top;
  
  private Cpu cpu;
  
  
  public CpuTaker() {
    lscpu = new SystemRun(CMD_LSCPU);
    top = new SystemRun(CMD_TOP, ARGS);
  }
  
  
  @Override
  public Cpu take() {
    lscpu.run();
    LscpuParser lsp = new LscpuParser();
    cpu = lsp.parse(lscpu.getOutput());
    TopParser cp = new TopParser(cpu);
    
    top.run();
    cpu = cp.parse(top.getOutput());
    return cpu;
  }
  
  
  @Override
  public Cpu get() {
    return cpu;
  }
  
  
  public static void main(String[] args) {
    CpuTaker ct = new CpuTaker();
    Cpu c = ct.take();
    System.out.println(c);
    System.out.println(c.getProcesses().get(0));
  }
  
}
