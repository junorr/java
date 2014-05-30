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
public class IFLoadParser implements Serializable, Parser<IFLoad> {
  
  private TextFieldParser tp;
  
  private IFLoad ifstat;
  
  
  public IFLoadParser() {
    tp = new TextFieldParser();
    ifstat = null;
  }
  
  
  @Override
  public IFLoad get() {
    return ifstat;
  }
  
  
  @Override
  public IFLoad parse(String text) {
    if(text == null || text.trim().isEmpty())
      return null;
    
    tp.setLine(text).parse(" ");
    ifstat = new IFLoad();
    
    ifstat.setNetInterface(tp.pop());
    
    while(tp.peak() != null) {
      
      while(tp.doubleField(0) < 0) {
        if(tp.pop().contains("n/a"))
          break;
      }
     
      double d = tp.doublePop();
      if(d < 0) d = 0;
      ifstat.addInPoll(d);
      
      d = tp.doublePop();
      if(d < 0) d = 0;
      ifstat.addOutPoll(d);
    }
    
    return ifstat;
  }
  
  
  public static void main(String[] args) {
    IFLoadParser ip = new IFLoadParser();
    String parse = 
  "       eth0       \n"
+ " KB/s in  KB/s out\n"
+ "    0.18      0.00\n"
+ "    0.18      0.00\n"
+ "    0.44      0.00\n"
+ "    0.59      0.00\n"
+ "    0.29      0.00\n";

    parse = 
  "      eth0:0\n" +
" KB/s in  KB/s out\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a\n" +
"     n/a       n/a";
    
    System.out.println(ip.parse(parse));
  }
  
}
