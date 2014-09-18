/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.listener;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author juno
 */
public class SimpleListener implements ProgressListener {

  private long size = 0;
  
  private long max = 1;
  
  private int perc = -1;
  
  private Path path;
  

  @Override
  public void setMax(long max) {
    this.max = max;
  }


  @Override
  public void update(Path path) {
    this.path = path;
  }


  @Override
  public void update(long current) {
    size += current;
    String end = "%\n";
    int np = (int) Math.round(perc() * 100.0);
    if(np > perc) {
      perc = np;
      int fill = (int) Math.round(perc()*14);
      System.out.print(path);
      System.out.print(": [" + rpt('=', fill)); 
      System.out.print(rpt(' ', (14 - fill)));
      System.out.print("] "+ perc+ end);
    }
  }
  
  
  private double perc() {
    double s = size;
    double m = max;
    return s / m;
  }
  
  
  private String rpt(char c, int times) {
    if(times < 1) return "";
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < times; i++) {
      sb.append(c);
    }
    return sb.toString();
  }


  @Override
  public void error(IOException ex) {
    ex.printStackTrace();
  }
  
  
  public static void main(String[] args) throws InterruptedException {
    SimpleListener sl = new SimpleListener();
    sl.setMax(100);
    sl.update(Paths.get("d:/ubuntu.zip"));
    sl.update(0);
    for(int i = 0; i < 100; i++) {
      sl.update(1);
      Thread.sleep(100);
    }
  }
  
}
