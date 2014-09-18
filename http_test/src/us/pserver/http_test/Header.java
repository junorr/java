/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.http_test;

import java.util.Objects;


/**
 *
 * @author juno
 */
public class Header {
  
  public static final String BOUNDARY = "Boundary";
  
  private String name;
  
  private String value;
  
  
  public Header() {
    name = null;
    value = null;
  }
  
  
  public Header(String name, String value) {
    this.name = name;
    this.value = value;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }
  
  
  public static Header boundary(String value) {
    return new Header(BOUNDARY, value);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.value);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Header other = (Header) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    if(name == null) return null;
    if(BOUNDARY.equalsIgnoreCase(name))
      return value;
    
    return name + ": " + (value == null ? "" : value);
  }
  
}
