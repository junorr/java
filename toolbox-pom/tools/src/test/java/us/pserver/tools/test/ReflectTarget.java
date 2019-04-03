/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.test;

import java.util.Objects;


/**
 *
 * @author juno
 */
public class ReflectTarget {
  
  public int magic;
  
  private final String hello;
  
  
  public ReflectTarget() {
    hello = "";
  }
  
  public ReflectTarget(String hello) {
    this.hello = hello;
  }
  
  public ReflectTarget(String hello, int magic) {
    this.hello = hello;
    this.magic = magic;
  }
  
  public String greet() {
    StringBuilder sb = new StringBuilder("Hello");
    if(hello != null) sb.append(" ").append(hello);
    return sb.append("!").toString();
  }
  
  public ReflectTarget withHello(String hello) {
    return new ReflectTarget(hello, magic);
  }
  
  public String greet(String hello) {
    return withHello(hello).greet();
  }
  
  public String greet(String hello, String hello2) {
    return withHello(hello + " " + hello2).greet();
  }
  
  public void printGreet() {
    System.out.println(greet());
  }

  public void printGreet(String hello) {
    System.out.println(greet(hello));
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + this.magic;
    hash = 97 * hash + Objects.hashCode(this.hello);
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
    final ReflectTarget other = (ReflectTarget) obj;
    if (this.magic != other.magic) {
      return false;
    }
    return Objects.equals(this.hello, other.hello);
  }
  
  @Override
  public String toString() {
    return greet();
  }
  
}
