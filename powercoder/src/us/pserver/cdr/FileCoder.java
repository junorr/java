/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.cdr;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;



/**
 *
 * @author juno
 */
public interface FileCoder {
  
  public boolean apply(Path p1, Path p2, boolean encode);
  
  public boolean applyFrom(ByteBuffer buf, Path p, boolean encode);
  
  public boolean applyTo(Path p, PrintStream ps, boolean encode);
  
  public boolean encode(Path p1, Path p2);
  
  public boolean decode(Path p1, Path p2);
  
}
