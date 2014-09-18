package com.jpower.jpzip.output;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 *
 * @author juno
 */
public class ZipStreamOutput implements ZipOutput {

  private ZipOutputStream zout;
  
  private boolean entryopened;
  
  
  protected ZipStreamOutput() {
    this.zout = null;
    this.entryopened = false;
  }
  
  
  public ZipStreamOutput(OutputStream os, boolean usingBufferedStream) {
    this();
    this.init(os, usingBufferedStream);
  }
  
  
  protected void init(OutputStream os, boolean usingBufferedStream) {
    if(os == null) throw new IllegalArgumentException(
        "OutputStream must be NOT NULL.");
    
    if(usingBufferedStream) os = new BufferedOutputStream(os);
    zout = new ZipOutputStream(os);
    this.setLevel(5);
  }
  

  @Override
  public void putEntry(ZipEntry entry) {
    if(entry == null) throw new IllegalArgumentException(
        "ZipEntry must be NOT NULL.");
    try {
      if(this.entryopened) zout.closeEntry();
      
      zout.putNextEntry(entry);
      this.entryopened = true;
      
    } catch(IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }


  @Override
  public void setLevel(int level) {
    if(level < 0) level = 0;
    else if(level > 9) level = 9;
    zout.setLevel(level);
  }


  @Override
  public OutputStream getOutput() throws IOException {
    return zout;
  }


  @Override
  public boolean close() {
    try {
      zout.closeEntry();
      zout.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  @Override
  public void write(byte[] bs, int off, int length) throws IOException {
    this.getOutput().write(bs, off, length);
  }
  
}
