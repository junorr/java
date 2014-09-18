/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;


/**
 *
 * @author juno
 */
public class IPTraffic {
  
  private String source;
  
  private String dest;
  
  private String proto;
  
  private int srcport;
  
  private int dstport;
  
  private int srcbytes;
  
  private int dstbytes;
  
  private int totalbytes;
  
  
  public IPTraffic() {
    source = null;
    dest = null;
    proto = null;
    srcport = 0;
    dstport = 0;
    srcbytes = 0;
    dstbytes = 0;
    totalbytes = 0;
  }


  public String getSource() {
    return source;
  }


  public void setSource(String s) {
    this.source = s;
  }


  public String getDest() {
    return dest;
  }


  public void setDest(String s) {
    this.dest = s;
  }


  public String getProto() {
    return proto;
  }


  public void setProto(String s) {
    this.proto = s;
  }


  public int getSrcPort() {
    return srcport;
  }


  public void setSrcPort(int srcport) {
    this.srcport = srcport;
  }


  public int getDstPort() {
    return dstport;
  }


  public void setDstPort(int dstport) {
    this.dstport = dstport;
  }


  public int getSrcBytes() {
    return srcbytes;
  }


  public void setSrcBytes(int srcbytes) {
    this.srcbytes = srcbytes;
  }


  public int getDstBytes() {
    return dstbytes;
  }


  public void setDstBytes(int dstbytes) {
    this.dstbytes = dstbytes;
  }


  public int getTotalBytes() {
    return totalbytes;
  }


  public void setTotalBytes(int totalbytes) {
    this.totalbytes = totalbytes;
  }


  @Override
  public String toString() {
    return "IPTraf {" + source + ":" + srcport + " <-> " + dest + ":" + dstport + "   " + proto + ", srcbytes=" + srcbytes + ", dstbytes=" + dstbytes + ", totalbytes=" + totalbytes + '}';
  }
  
}
