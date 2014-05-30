/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import java.io.IOException;




/**
 *
 * @author juno
 */
public class SyscheckInstaller {
  
  
  public static void main(String[] args) throws IOException {
    String[] cmd = {"/opt/syscheck/service.sh", "sudo", "0988", "-s"};
    Runtime.getRuntime().exec(cmd);
  }
  
}
