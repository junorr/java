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
    System.out.println("* current = "+ fs.current());
    System.out.println("* cd [users/juno] = "+ fs.cd("users/juno"));
    System.out.println("* current = "+ fs.current());
    System.out.println("* cd [Downloads] = "+ fs.cd("Downloads"));
    System.out.println("* current = "+ fs.current());
    System.out.println("* cd [..] = "+ fs.cd(".."));
    System.out.println("* current = "+ fs.current());
    System.out.println("* cd [Downloads] = "+ fs.cd("Downloads"));
    System.out.println("* current = "+ fs.current());
    System.out.println("* cd [../Downloads] = "+ fs.cd("../Downloads"));
    System.out.println("* current = "+ fs.current());
    System.out.println("* zip [cp_resid.zip] = "
        + fs.zip("cp_resid.zip", "cp_resid.pdf"));
  }
  
}
