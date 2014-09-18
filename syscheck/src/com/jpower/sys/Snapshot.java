/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import us.pserver.date.SerialDate;
import us.pserver.date.SimpleDate;


/**
 *
 * @author juno
 */
public class Snapshot {
  
  private Cpu cpu;
  
  private List<Disk> disks;
  
  private Memory mem;
  
  private List<IFNetwork> ifs;
  
  private SerialDate time;
  
  private List<SysProcess> processes;
  
  
  public Snapshot() {
    cpu = null;
    disks = null;
    mem = null;
    ifs = null;
    processes = null;
    time = new SimpleDate().toSerialDate();
  }


  public Cpu getCpu() {
    return cpu;
  }


  public Snapshot setCpu(Cpu cpu) {
    this.cpu = cpu;
    return this;
  }


  public List<SysProcess> getProcesses() {
    return processes;
  }


  public Snapshot setProcesses(List<SysProcess> processes) {
    this.processes = processes;
    return this;
  }


  public List<Disk> getDisks() {
    return disks;
  }


  public Snapshot setDisk(List<Disk> disks) {
    this.disks = disks;
    return this;
  }


  public Memory getMem() {
    return mem;
  }


  public Snapshot setMem(Memory mem) {
    this.mem = mem;
    return this;
  }


  public List<IFNetwork> getInterfaces() {
    return ifs;
  }


  public Snapshot setInterfaces(List<IFNetwork> ifs) {
    this.ifs = ifs;
    return this;
  }
  
  
  public SerialDate getSerialDate() {
    return time;
  }
  
  
  public Snapshot setSerialDate(SerialDate sd) {
    time = sd;
    return this;
  }


  public SimpleDate getTime() {
    return time.getDate();
  }


  public Snapshot setTime(Date time) {
    if(time instanceof SimpleDate)
      this.time = ((SimpleDate) time).toSerialDate();
    else
      this.time = new SerialDate(time);
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.time);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final Snapshot other = (Snapshot) obj;
    if(!Objects.equals(this.time, other.time)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Snapshot{" + "cpu=" + cpu + ", disks=" 
        + disks + ", memory=" + mem + ", time=" 
        + (time == null ? "null" : time.getDate().toString()) + '}';
  }
  
}
