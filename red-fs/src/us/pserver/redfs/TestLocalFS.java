/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.redfs;

import java.io.IOException;



/**
 *
 * @author juno
 */
public class TestLocalFS {
  
  
  public static void main(String[] args) throws IOException {
    LocalFileSystem fs = new LocalFileSystem();
    System.out.println("* current = "+ fs.getCurrent());
    System.out.println("* cd [home/juno] = "+ fs.cd("home/juno"));
    System.out.println("* current = "+ fs.getCurrent());
    System.out.println("* cd [../../media] = "+ fs.cd("../../media"));
    System.out.println("* current = "+ fs.getCurrent());
    System.out.println("* cd [..] = "+ fs.cd(".."));
    System.out.println("* current = "+ fs.getCurrent());
    System.out.println("* cd [home/juno] = "+ fs.cd("home/juno"));
    System.out.println("* current = "+ fs.getCurrent());
    System.out.println("* unzip [syscheck.zip] = "
        + fs.unzip("syscheck.zip", "unzip"));
  }
  
}
