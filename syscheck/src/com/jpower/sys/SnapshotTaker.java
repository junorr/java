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
public class SnapshotTaker implements Serializable, Taker<Snapshot> {
  
  private CpuTaker cpuTaker;
  
  private MemoryTaker memTaker;
  
  private DiskTaker diskTaker;
  
  private NetworkTaker netTaker;
  
  private Snapshot snapshot;
  
  
  public SnapshotTaker() {
    cpuTaker = new CpuTaker();
    memTaker = new MemoryTaker();
    diskTaker = new DiskTaker();
    netTaker = new NetworkTaker();
  }
  
  
  @Override
  public Snapshot get() {
    if(snapshot == null) take();
    return snapshot;
  }
  
  
  @Override
  public Snapshot take() {
    cpuTaker.take();
    memTaker.take();
    diskTaker.take();
    netTaker.take();
    
    snapshot = new Snapshot();
    snapshot.setCpu(cpuTaker.get())
        .setProcesses(cpuTaker.get().getProcesses())
        .setMem(memTaker.get())
        .setDisk(diskTaker.get())
        .setInterfaces(netTaker.get());
    
    return snapshot;
  }
  
}
