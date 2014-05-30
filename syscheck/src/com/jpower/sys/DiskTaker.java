/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author juno
 */
public class DiskTaker implements Serializable, Taker<List<Disk>> {
  
  public static final String CMD = "df";
  
  public static final String[] ARGS = { "-T" };
  
  private SystemRun cmd;
  
  private List<Disk> disks;
  
  
  public DiskTaker() {
    cmd = new SystemRun(CMD, ARGS);
  }
  
  
  @Override
  public List<Disk> take() {
    cmd.run();
    DiskParser parser = new DiskParser();
    parser.parse(cmd.getOutput());
    disks = parser.get();
    return disks;
  }
  
  
  @Override
  public List<Disk> get() {
    return disks;
  }
  
  
  public static void main(String[] args) {
    DiskTaker mt = new DiskTaker();
    mt.take();
    for(Disk d : mt.get())
      System.out.println(d);
  }
  
}
