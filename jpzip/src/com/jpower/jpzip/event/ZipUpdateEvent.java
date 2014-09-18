/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.jpzip.event;

import java.text.DecimalFormat;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class ZipUpdateEvent {
  
  private ZipMonitor src;
  
  private ZipEntry entry;
  
  private long value;
  
  private long total;
  
  
  public ZipUpdateEvent() {
    src = null;
    value = 0;
    total = 0;
    entry = null;
  }
  
  
  public ZipUpdateEvent(ZipMonitor source) {
    this();
    src = source;
  }
  
  
  public void setSource(ZipMonitor s) {
    src = s;
  }
  
  
  public ZipMonitor getSource() {
    return src;
  }
  
  
  public void setValue(long v) {
    value = v;
  }
  
  
  public long getValue() {
    return value;
  }
  
  
  public void setTotal(long t) {
    total = t;
  }
  
  
  public long getTotal() {
    return total;
  }
  
  
  public void setProcessedEntry(ZipEntry e) {
    entry = e;
  }
  
  
  public ZipEntry getProcessedEntry() {
    return entry;
  }
  
  
  public String getName() {
    if(entry == null) return null;
    return entry.getName();
  }
  
  
  public double getPercent() {
    return value / new Long(total).doubleValue();
  }
  
  
  public String getFormattedPercent() {
    DecimalFormat format = new DecimalFormat("0.00%");
    return format.format(this.getPercent());
  }
  
}
