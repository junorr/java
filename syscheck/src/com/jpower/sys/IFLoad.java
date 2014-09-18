/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;


/**
 *
 * @author juno
 */
public class IFLoad {
  
  private String net;
  
  private double[] inPolls;
  
  private double[] outPolls;
  
  private int inidx, outidx;
  
  
  public IFLoad() {
    net = null;
    inidx = 0;
    outidx = 0;
    inPolls = new double[5];
    outPolls = new double[5];
  }
  
  
  public void addInPoll(double d) {
    if(inidx >= inPolls.length) return;
    inPolls[inidx++] = d;
  }


  public void addOutPoll(double d) {
    if(outidx >= outPolls.length) return;
    outPolls[outidx++] = d;
  }
  
  
  public void resetPolls() {
    inidx = outidx = 0;
  }


  public String getNetInterface() {
    return net;
  }


  public void setNetInterface(String net) {
    this.net = net;
  }


  public double[] getInPolls() {
    return inPolls;
  }


  public void setInPolls(double[] inPolls) {
    this.inPolls = inPolls;
  }


  public double[] getOutPolls() {
    return outPolls;
  }


  public void setOutPolls(double[] outPolls) {
    this.outPolls = outPolls;
  }
  
  
  public double getMaxInput() {
    if(inPolls == null || inPolls.length == 0)
      return -1;
    
    double max = 0;
    for(double d : inPolls)
      if(d > max) max = d;
    return max;
  }
  
  
  public double getMaxOutput() {
    if(outPolls == null || outPolls.length == 0)
      return -1;
    
    double max = 0;
    for(double d : outPolls)
      if(d > max) max = d;
    return max;
  }
  
  
  public double getInputAverage() {
    if(inPolls == null || inPolls.length == 0)
      return -1;
    
    double av = 0;
    for(double d : inPolls) {
      av += d;
    }
    av = av / inPolls.length;
    return av;
  }
  
  
  public double getOutputAverage() {
    if(outPolls == null || outPolls.length == 0)
      return -1;
    
    double av = 0;
    for(double d : outPolls) {
      av += d;
    }
    av = av / outPolls.length;
    return av;
  }
  
  
  public double getTotalAverage() {
    return getInputAverage() + getOutputAverage();
  }


  @Override
  public String toString() {
    return "IFLoad (" + net + ")" + " [Averages: " 
        + "in=" + getInputAverage() 
        + ", out=" + getOutputAverage() 
        + ", total=" + getTotalAverage()
        + " | Max: "
        + "in=" + getMaxInput()
        + ", out=" + getMaxOutput()
        + ']';
  }
  
}
