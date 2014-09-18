/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zip;

import java.nio.file.Path;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class ZipInfo {
  
  private ZipEntry entry;
  
  private Path path;
  
  
  public ZipInfo() {
    entry = null;
    path = null;
  }
  
  
  public ZipInfo(ZipEntry e, Path p) {
    entry = e;
    path = p;
  }


  public ZipEntry getEntry() {
    return entry;
  }


  public void setEntry(ZipEntry entry) {
    this.entry = entry;
  }


  public Path getPath() {
    return path;
  }


  public void setPath(Path path) {
    this.path = path;
  }
  
}
