/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pserver.isys;

import com.jpower.spj.Option;
import com.jpower.spj.ShellParser;


/**
 *
 * @author juno
 */
public class INotesSysAdmin {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ShellParser sp = new ShellParser();
    sp.setAppName("EmailChecker")
        .setAuthor("pserver.us")
        .setContact("juno@pserver.us")
        .setYear("2013");
        
    Option stop = new Option("-s")
        .setLongName("--stop")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false)
        .setDescription("Stops EmailChecker");
    
    Option help = new Option("-h")
        .setLongName("--help")
        .setDescription("Show this help text")
        .setAcceptArgs(false)
        .setExclusive(true)
        .setMandatory(false);
    
    sp.addOption(stop).addOption(help);
    
    System.out.println(sp.createHeader(40));
    
    EmailChecker ec = new EmailChecker();
    
    sp.parseArgs(args);
    if(sp.getOption("-h").isPresent()) {
      System.out.println(sp.createUsage());
      System.exit(0);
    }
    
    if(!sp.parseErrors()) {
      System.out.println(sp.createUsage());
      System.exit(1);
    }
    
    if(sp.getOption("-s").isPresent()) {
      System.out.println("* Terminating EmailChecker "
          + "(PID="+ ec.getPid()+ ")...");
      ec.deletePid();
      System.exit(0);
    }
    
    ec.start();
  }
}
