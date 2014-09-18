/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.listener;

import java.io.IOException;
import java.nio.file.Path;



/**
 *
 * @author juno
 */
public interface ProgressListener {
  
  public void setMax(long max);
  
  public void update(Path path);
  
  public void update(long current);
  
  public void error(IOException ex);
  
}
