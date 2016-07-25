/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack.test;

import us.pserver.fpack.OS;


/**
 *
 * @author juno
 */
public class TestOS {
  
  
  public static void main(String[] args) {
    System.out.println("OS.isWindows(): "+ OS.isWindows());
    System.out.println("OS.isUnix()...: "+ OS.isUnix());
    System.out.println("OS.isMacOS()..: "+ OS.isMacOS());
    System.out.println("OS.CURRENT....: "+ OS.CURRENT);
    System.out.println("OS.NAME.......: "+ OS.NAME);
  }
  
}
