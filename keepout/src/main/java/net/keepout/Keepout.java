/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout;

import net.keepout.client.ClientMain;
import net.keepout.server.ServerMain;


/**
 *
 * @author juno
 */
public class Keepout {
  
  private static void printHelp() {
    System.out.println("Usage: keepout.jar <option>");
    System.out.println("Options:");
    System.out.println("  -s/--server: Start in Server mode");
    System.out.println("  -c/--client: Start in Client mode");
    System.out.println("  -h/--help  : Print help");
  }
  
  public static void main(String[] args) throws Exception {
    if(args == null || args.length < 1) {
      printHelp();
      return;
    }
    switch(args[0]) {
      case "-s":
      case "--server":
        ServerMain.main(null);
      case "-c":
      case "--client":
        ClientMain.main(null);
      default:
        printHelp();
    }
  }
  
}
