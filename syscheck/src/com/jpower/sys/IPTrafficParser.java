/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * jnettop -i eth0 --display text -t 10 
 * --format '$src$   $dst$   $proto$   $srcport$
 *    $dstport$   $srcbytes$   $dstbytes$   $totalbytes$   '
 * 
 * @author juno
 */
public class IPTrafficParser implements Serializable, Parser<List<IPTraffic>> {
  
  private TextFieldParser tp;
  
  private List<IPTraffic> traf;
  
  private String text;
  
  
  public IPTrafficParser() {
    tp = new TextFieldParser();
    traf = new LinkedList<>();
    text = null;
  }
  
  
  @Override
  public List<IPTraffic> parse(String text) {
    this.text = text;
    tp.setLine(text);
    return parse();
  }
  
  
  @Override
  public List<IPTraffic> get() {
    return traf;
  }
  
  
  public List<IPTraffic> parse() {
    tp.parse(" ");
    
    while(tp.peak() != null) {
      IPTraffic ip = new IPTraffic();
      while(tp.peak() != null && !tp.fieldContainsMany(".", 3)) {
        tp.pop();
      }
      ip.setSource(tp.popAsNumberString());
      ip.setDest(tp.popAsNumberString());
      ip.setProto(tp.pop());
      ip.setSrcPort((int) tp.doublePop());
      ip.setDstPort((int) tp.doublePop());
      ip.setSrcBytes((int) tp.doublePop());
      ip.setDstBytes((int) tp.doublePop());
      ip.setTotalBytes((int) tp.doublePop());
      if(ip.getSource() != null && !ip.getSource().trim().isEmpty())
        traf.add(ip);
    }
    return traf;
  }
  
  
  public static void main(String[] args) {
    String parse = 
  "Could not get HW address of interface nflog: No such device\n"
+ "Could not get HW address of interface any: No such device\n"
+ "172.24.74.68   172.24.74.127   UDP   137   137   1932   0   1932   \n"
+ "0.0.0.0   0.0.0.0   ARP   0   0   1680   0   1680   \n"
+ "172.24.74.31   172.18.51.12   UDP   11472   53   81   179   260   \n"
+ "172.24.74.31   172.18.51.12   UDP   34954   53   86   146   232   \n"
+ "172.24.74.31   172.18.51.10   UDP   32816   53   85   145   230   \n"
+ "172.24.74.31   172.18.51.11   UDP   32816   53   85   145   230   \n"
+ "172.24.74.31   172.18.51.12   UDP   32816   53   85   145   230   \n"
+ "172.24.74.31   172.18.51.34   UDP   32816   53   85   145   230   \n"
+ "172.24.74.31   10.8.4.1   UDP   32816   53   85   145   230   \n"
+ "172.24.74.31   172.18.51.12   UDP   52628   53   85   145   230   \n"
+ "172.24.74.31   172.18.51.12   UDP   28260   53   85   131   216   \n"
+ "172.24.74.31   172.18.51.12   UDP   58884   53   85   131   216   \n"
+ "172.24.74.31   172.18.51.12   UDP   42289   53   85   131   216   \n"
+ "172.24.74.31   172.18.51.12   UDP   17049   53   85   131   216   \n"
+ "0.0.0.0   0.0.0.0   ETHER   0   0   60   0   60   ";

    IPTrafficParser tp = new IPTrafficParser();
    tp.parse(parse);
    List l = tp.get();
    for(Object o : l)
      System.out.println(o);
  }
  
}
