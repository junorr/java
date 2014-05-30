/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pxy;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


/**
 *
 * @author juno
 */
public class DNSTest {
  
  
  public static void main(String[] args) throws UnknownHostException {
    String hostname = "www.google.com";
    System.out.println("* Resolving...");
    System.out.println("* hostname="+ hostname);
    System.out.println("* getAddress()="+ InetAddress.getByName(hostname).getHostAddress());
  }
  
}
