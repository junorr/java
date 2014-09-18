/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.bbm;


/**
 *
 * @author juno
 */
public enum Instancia {
  
  AGENCIA, CSO;
  
  
  public static boolean isCSO(String s) {
    if(s == null || s.trim().isEmpty())
      return false;
    return s.contains("1900")
        || s.contains("1903")
        || s.contains("1908")
        || s.contains("1962")
        || s.contains("3901")
        || s.contains("1970");
  }
  
  
  public static Instancia getInstancia(String s) {
    if(isCSO(s)) return Instancia.CSO;
    else return Instancia.AGENCIA;
  }
  
  
  
  
  public static void main(String[] args) {
    String s = "0000012345";
    System.out.println("s: "+ s);
    double d = Double.parseDouble(s);
    System.out.println("d: "+ d);
    System.out.println(d/100);
  }
  
}
