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
public class NetworkTaker implements Serializable, Taker<List<IFNetwork>> {
  
  public static final String CMD_IFCONFIG = "ifconfig";
  
  public static final String[] ARGS_IFCONFIG = { "-a" };
  
  public static final String CMD_IFLOAD = "ifstat";
  
  public static final String[] ARGS_IFLOAD = { "-i", "net", "2", "3" };
  
  public static final String CMD_NTOP = "jnettop";
  
  public static final String[] ARGS_NTOP = { "-i", 
    "net", "--display", "text", "-t", "6", 
    "--format", "$src$   $dst$   $proto$   "
      + "$srcport$   $dstport$   $srcbytes$   "
      + "$dstbytes$   $totalbytes$   " };
  
  private SystemRun ifc;
  
  private SystemRun ifload;
  
  private SystemRun ntop;
  
  private List<IFNetwork> ifs;
  
  private List<IPTraffic> traffic;
  
  
  public NetworkTaker() {
    ifc = new SystemRun(CMD_IFCONFIG, ARGS_IFCONFIG);
    ifload = new SystemRun(CMD_IFLOAD);
    ntop = new SystemRun(CMD_NTOP);
  }
  
  
  public List<IFNetwork> takeInterfaces() {
    ifc.run();
    IFParser ps = new IFParser();
    ifs = ps.parse(ifc.getOutput());
    return ifs;
  }
  
  
  public List<IFNetwork> takeLoad() {
    if(ifs == null || ifs.isEmpty())
      return ifs;
    
    for(IFNetwork net : ifs) {
      if(net.getAddress() == null) continue;
      ARGS_IFLOAD[1] = net.getName();
      ifload.setArgs(ARGS_IFLOAD);
      ifload.run();
      IFLoadParser ps = new IFLoadParser();
      net.setLoad(ps.parse(ifload.getOutput()));
    }
    return ifs;
  }
  
  
  public List<IFNetwork> takeTraffic() {
    if(ifs == null || ifs.isEmpty())
      return ifs;
    
    for(IFNetwork net : ifs) {
      if(net.getAddress() == null) continue;
      ARGS_NTOP[1] = net.getName();
      ntop.setArgs(ARGS_NTOP);
      ntop.run();
      IPTrafficParser ps = new IPTrafficParser();
      List<IPTraffic> trf = ps.parse(ntop.getOutput());
      if(trf != null && !trf.isEmpty())
        net.setTraffic(trf);
    }
    return ifs;
  }
  
  
  @Override
  public List<IFNetwork> get() {
    return ifs;
  }
  
  
  @Override
  public List<IFNetwork> take() {
    this.takeInterfaces();
    this.takeLoad();
    return this.takeTraffic();
  }
  
  
  public static void main(String[] args) {
    NetworkTaker nt = new NetworkTaker();
    nt.takeInterfaces();
    nt.takeLoad();
    nt.takeTraffic();
    for(IFNetwork net : nt.get()) {
      System.out.println(net);
      System.out.println(net.getLoad());
      System.out.println();
      if(net.getTraffic() != null)
        for(IPTraffic tr : net.getTraffic())
          System.out.println(tr);
    }
  }
  
}
