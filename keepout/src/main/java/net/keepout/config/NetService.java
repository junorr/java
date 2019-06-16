/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.config;

import java.util.Objects;


/**
 *
 * @author juno
 */
public class NetService {
  
  private final Host bind;
  
  private final Host target;
  
  public NetService(Host bindAddress, Host target) {
    this.bind = bindAddress;
    this.target = target;
  }
  
  public Host getBind() {
    return bind;
  }
  
  public Host getTarget() {
    return target;
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 13 * hash + Objects.hashCode(this.bind);
    hash = 13 * hash + Objects.hashCode(this.target);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final NetService other = (NetService) obj;
    if (!Objects.equals(this.bind, other.bind)) {
      return false;
    }
    return Objects.equals(this.target, other.target);
  }
  
  @Override
  public String toString() {
    return "NetService{" + "bind=" + bind + ", target=" + target + '}';
  }
  
}
