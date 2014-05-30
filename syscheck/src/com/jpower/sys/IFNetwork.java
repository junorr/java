/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.util.List;
import java.util.Objects;


/**
 *
 * @author juno
 */
public class IFNetwork {
  
  private String name;
  
  private String address;
  
  private String mask;
  
  private String hw;
  
  private int mtu;
  
  private IFLoad load;
  
  private List<IPTraffic> traffic;
  
  
  public IFNetwork() {
    name = null;
    address = null;
    mask = null;
    hw = null;
    mtu = 0;
    load = null;
    traffic = null;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
    load = new IFLoad();
    load.setNetInterface(name);
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public String getMask() {
    return mask;
  }


  public void setMask(String mask) {
    this.mask = mask;
  }


  public String getHw() {
    return hw;
  }


  public void setHw(String hw) {
    this.hw = hw;
  }


  public int getMtu() {
    return mtu;
  }


  public void setMtu(int mtu) {
    this.mtu = mtu;
  }


  public IFLoad getLoad() {
    return load;
  }


  public void setLoad(IFLoad load) {
    this.load = load;
  }


  public List<IPTraffic> getTraffic() {
    return traffic;
  }


  public void setTraffic(List<IPTraffic> traffic) {
    this.traffic = traffic;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + Objects.hashCode(this.name);
    hash = 53 * hash + Objects.hashCode(this.address);
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
    final IFNetwork other = (IFNetwork) obj;
    if(!Objects.equals(this.name, other.name)) {
      return false;
    }
    if(!Objects.equals(this.address, other.address)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return name + " [address=" + address + ", mask=" + mask + ']';
  }
  
  
}
