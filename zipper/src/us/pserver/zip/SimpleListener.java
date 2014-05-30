/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zip;

import java.io.IOException;
import java.nio.file.Path;


/**
 *
 * @author juno
 */
public class SimpleListener implements ProgressListener {

  private long size = 0;
  
  private long max = 1;
  

  @Override
  public void setMax(long max) {
    this.max = max;
    System.out.println("* maxSize = "+ max);
  }


  @Override
  public void update(Path path) {
    System.out.println("* path = "+ path);
  }


  @Override
  public void update(long current) {
    size += current;
    System.out.println("* current = "+ size+ "\t\t( "+ ((double)size / max * 100)+ "% )");
  }


  @Override
  public void error(IOException ex) {
    ex.printStackTrace();
  }
  
}
