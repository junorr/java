/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.config;

import java.util.Objects;
import net.keepout.KeepoutHandler;


/**
 *
 * @author juno
 */
public class Host {
  
  private final String address;
  
  private final int port;


  public Host(String address, int port) {
    this.address = address;
    this.port = port;
  }


  public String getAddress() {
    return address;
  }


  public int getPort() {
    return port;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.address);
    hash = 59 * hash + this.port;
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
    final Host other = (Host) obj;
    if (this.port != other.port) {
      return false;
    }
    return Objects.equals(this.address, other.address);
  }


  @Override
  public String toString() {
    return String.format("%s:%d", address, port);
  }
  
}
