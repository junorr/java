/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;


/**
 *
 * @author juno
 */
public class ClearMemoryCommand {
  
  public static final String CMD1 = "sh";
  
  public static final String[] ARGS1 = { "-c", "echo 3 > /proc/sys/vm/drop_caches" };
  
  public static final String CMD2 = "sh";
  
  public static final String[] ARGS2 = { "-c", "sysctl -w vm.drop_caches=3" };
  
  public static final String CMD3 = "free";
  
  public static final String[] ARGS3 = { "-m" };
  
  
  private SystemRun cmd;
  
  
  public ClearMemoryCommand() {
    cmd = new SystemRun();
  }
  
  
  public String clearCache() {
    cmd.setCommand(CMD1).setArgs(ARGS1).run();
    cmd.setCommand(CMD2).setArgs(ARGS2).run();
    cmd.setCommand(CMD3).setArgs(ARGS3).run();
    return cmd.getOutput();
  }
  
  
  public String getFree() {
    cmd.setCommand(CMD3).setArgs(ARGS3).run();
    return cmd.getOutput();
  }
  
  
  public static void main(String[] args) {
    ClearMemoryCommand fmc = new ClearMemoryCommand();
    System.out.println(fmc.getFree());
    System.out.println(fmc.clearCache());
  }
  
}
