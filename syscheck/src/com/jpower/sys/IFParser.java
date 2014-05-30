/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author juno
 */
public class IFParser implements Serializable, Parser<List<IFNetwork>> {
  
  private TextFieldParser tp;
  
  private String text;
  
  private List<IFNetwork> links;
  
  
  public IFParser() {
    text = null;
    tp = new TextFieldParser();
    links = new LinkedList<>();
  }
  
  
  public IFParser(String text) {
    this();
    this.text = text;
  }
  
  
  @Override
  public List<IFNetwork> get() {
    return links;
  }
  
  
  @Override
  public List<IFNetwork> parse(String text) {
    this.text = text;
    tp.setLine(text);
    parse();
    return get();
  }
  
  
  public void parse() {
    if(text == null || text.trim().isEmpty())
      return;
    
    IFNetwork net = new IFNetwork();
    tp.parse(" ", "\n");
    
    if(tp.peak() == null) return;
    
    while(tp.nextField() != null 
        && !tp.nextField().equalsIgnoreCase("Link")) {
      tp.pop();
    }
    
    net.setName(tp.pop());
    if(net.getName() == null 
        || !net.getName().contains("lo")) {
      while(tp.peak() != null && !tp.fieldContainsMany(":", 5)) {
        tp.pop();
      }
      net.setHw(tp.pop());
    }
    
    if(tp.peak() != null && tp.peak().equalsIgnoreCase("inet") 
        || net.getName().contains("lo")) {
      while(!tp.fieldContainsMany(".", 3) && tp.peak() != null) {
        tp.pop();
      }
      if(tp.peak() == null) {
        parse();
        return;
      }
      net.setAddress(tp.popAsNumberString());
      
      //bcast
      if(tp.fieldContainsMany(".", 3) && tp.peak().contains("cast"))
        tp.pop();
    
      while(!tp.fieldContainsMany(".", 3) && tp.peak() != null) {
        tp.pop();
      }
      if(tp.peak() == null) {
        parse();
        return;
      }
      net.setMask(tp.popAsNumberString());
    }
    
    while(tp.peak() != null && !tp.peak().contains("MTU")) {
      tp.pop();
    }
    if(tp.peak() == null) {
      parse();
      return;
    }
    net.setMtu((int) tp.doublePop());
    
    links.add(net);
    parse();
  }
  
  
  public static void main(String[] args) {
    IFParser ip = new IFParser();
    String parse = 
  "eth0      Link encap:Ethernet  Endereço de HW f4:6d:04:f8:be:d4  \n"
+ "          UP BROADCAST MULTICAST  MTU:1500  Métrica:1\n"
+ "          pacotes RX:0 erros:0 descartados:0 excesso:0 quadro:0\n"
+ "          Pacotes TX:0 erros:0 descartados:0 excesso:0 portadora:0\n"
+ "          colisões:0 txqueuelen:1000 \n"
+ "          RX bytes:0 (0.0 B) TX bytes:0 (0.0 B)\n"
+ "\n"
+ "eth0:0    Link encap:Ethernet  HWaddr 26:21:b9:52:66:4b\n"
+ "          inet addr:10.0.100.101  Bcast:10.0.100.255  Mask:255.255.255.0\n"
+ "          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1\n"
+ "          Interrupt:27"
+ "\n"
+ "lo        Link encap:Loopback Local  \n"
+ "          inet end.: 127.0.0.1  Masc:255.0.0.0\n"
+ "          endereço inet6: ::1/128 Escopo:Máquina\n"
+ "          UP LOOPBACK RUNNING  MTU:16436  Métrica:1\n"
+ "          pacotes RX:552 erros:0 descartados:0 excesso:0 quadro:0\n"
+ "          Pacotes TX:552 erros:0 descartados:0 excesso:0 portadora:0\n"
+ "          colisões:0 txqueuelen:0 \n"
+ "          RX bytes:100658 (100.6 KB) TX bytes:100658 (100.6 KB)\n"
+ "\n"
+ "wlan0     Link encap:Ethernet  Endereço de HW 74:2f:68:52:a2:2f  \n"
+ "          inet end.: 10.100.0.107  Bcast:10.100.0.255  Masc:255.255.255.0\n"
+ "          endereço inet6: fe80::762f:68ff:fe52:a22f/64 Escopo:Link\n"
+ "          UP BROADCAST RUNNING MULTICAST  MTU:1500  Métrica:1\n"
+ "          pacotes RX:31998 erros:0 descartados:0 excesso:0 quadro:0\n"
+ "          Pacotes TX:19554 erros:0 descartados:0 excesso:0 portadora:0\n"
+ "          colisões:0 txqueuelen:1000 \n"
+ "          RX bytes:42141268 (42.1 MB) TX bytes:2521576 (2.5 MB)\n";


    List<IFNetwork> nets = ip.parse(parse);
    for(IFNetwork net : nets) {
      System.out.println(net);
      if(net != null) {
        System.out.println("hw="+net.getHw());
        System.out.println("mtu="+net.getMtu());
      }
    }
  }
  
}
