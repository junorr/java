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
public class TextProgressBar implements ProgressListener {
  
  public static final char PCHAR = '=';
  
  
  private long max;
  
  private long current;
  
  private Path path;
  
  private boolean showProcessedFiles = false;
  
  private String ln = "\r";
  
  
  public TextProgressBar() {};


  public boolean isShowProcessedFiles() {
    return showProcessedFiles;
  }


  public void setShowProcessedFiles(boolean showProcessedFiles) {
    this.showProcessedFiles = showProcessedFiles;
  }


  @Override
  public void setMax(long m) {
    current = 0;
    max = m;
  }


  @Override
  public void update(Path p) {
    path = p;
    if(showProcessedFiles)
      ln = "\n";
  }


  @Override
  public void update(long cur) {
    if(max == 0) return;
    current += cur;
    int maxChars = 20;
    double perc = ((double) current) / max;
    int chars = (int) Math.round(maxChars * perc);
    
    System.out.print("[");
    for(int i = 0; i < maxChars; i++) {
      if(i < chars) System.out.print(PCHAR);
      else System.out.print(' ');
    }
    System.out.printf("]\t%3.0f", perc*100);
    if(showProcessedFiles) {
      System.out.print("%");
      System.out.printf(" - \t%s%s", path.getFileName(), ln);
      ln = "\r";
    }
    else {
      System.out.print("%\r");
    }
  }


  @Override
  public void error(IOException ex) {
    System.out.println("\n# Error (un)zipping file: "+ ex.toString());
  }
  
}
