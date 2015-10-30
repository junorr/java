/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author juno
 */
public class FPackFileWriter {
  
  private List<FPackFileEntry> entries;
  
  
  public FPackFileWriter() {
    entries = new LinkedList<>();
  }
  
  
  public FPackFileWriter addEntry(FPackFileEntry fe) {
    if(fe != null) {
      entries.add(fe);
    }
    return this;
  }
  
  
  public List<FPackFileEntry> getEntries() {
    return entries;
  }
  
  
  
  
}
