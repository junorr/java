package com.jpower.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Classe abstrata implementa parcialmente
 * as funções de PingTool.
 * @author Juno Roesler
 */
public abstract class AbstractPingTool
    implements PingTool
{

  private String host;

  private int count;

  private int ttl;

  private int timeout;

  private int packetSize;

  private static PingTool ping = null;


  protected AbstractPingTool()
  {
    host = "localhost";
    count = 1;
    ttl = -1;
    timeout = -1;
    packetSize = -1;
  }

  /**
   * Retorna uma instância de PingTool
   * relativa ao sistema operacional.
   * @return PingTool.
   */
  public static final PingTool getSystemPingTool()
  {
    if(ping == null)
      ping = (OS.isLinux() ?
        new LinuxPingTool() :
        new WindowsPingTool());

    return ping;
  }

  public void setHost(String host) {
    this.host =
        (host == null || host.equals("") ?
          "localhost" : host);
  }

  public String getHost() {
    return host;
  }

  public InetAddress getHostAddress() {
    try {
      return InetAddress.getByName(host);
    } catch (UnknownHostException ex) {
      return null;
    }
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public void setPacketSize(int size) {
    this.packetSize = size;
  }

  public int getPacketSize() {
    return packetSize;
  }

  public void setTTL(int ttl) {
    this.ttl = ttl;
  }

  public int getTTL() {
    return ttl;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public int getTimeout() {
    return timeout;
  }

  @Override
  public String toString()
  {
    return "PingTool {\n"+
        "  OS   : "+
        (OS.CURRENT == OS.OsType.LINUX ? "Linux\n" : "Windows\n")+
        "  Host : "+ this.getHost()+ "\n"+
        (this.getCount() > 0 ?
          "  Count: "+ this.getCount()+ "\n" : "")+
        (this.getPacketSize() > 0 ?
          "  Size : "+ this.getPacketSize()+ " bytes\n" : "")+
        (this.getTTL() > 0 ?
          "  TTL  : "+ this.getTTL()+ "\n" : "")+
        (this.getTimeout() > 0 ?
          "  Time : "+ this.getTimeout()+ "\n" : "")+
        "}\n";
  }

  public static void main(String[] args)
  {
    PingTool cmdPing = AbstractPingTool.getSystemPingTool();

    cmdPing.setHost("localhost");
    cmdPing.setCount(2);
    cmdPing.setTTL(4);
    cmdPing.setPacketSize(256);

    System.out.println(cmdPing.toString());
    cmdPing.run();
    System.out.println("Successful: "+ cmdPing.isSuccessful());
    System.out.println("Hits: "+ cmdPing.getHits());
    System.out.println(cmdPing.getReturnLine());
    System.out.println();

    cmdPing.setHost("10.100.0.100");
    cmdPing.setCount(3);
    cmdPing.setTTL(2);
    cmdPing.setPacketSize(128);

    System.out.println(cmdPing.toString());
    cmdPing.run();
    System.out.println("Successful: "+ cmdPing.isSuccessful());
    System.out.println("Hits: "+ cmdPing.getHits());
    System.out.println(cmdPing.getReturnLine());
    System.out.println();

    cmdPing.setHost("10.100.0.200");
    cmdPing.setCount(1);
    cmdPing.setTTL(3);
    cmdPing.setPacketSize(32);

    System.out.println(cmdPing.toString());
    cmdPing.run();
    System.out.println("Successful: "+ cmdPing.isSuccessful());
    System.out.println("Hits: "+ cmdPing.getHits());
    System.out.println(cmdPing.getReturnLine());
    System.out.println();
  }

}
