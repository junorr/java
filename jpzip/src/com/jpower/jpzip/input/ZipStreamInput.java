package com.jpower.jpzip.input;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 *
 * @author juno
 */
public class ZipStreamInput implements ZipInput {

  private ZipInputStream zin;
  
  private InputStream in;
  
  private ZipEntry entry;
  
  private long length;
  
  private long usize;
  
  
  protected ZipStreamInput() {
    zin = null;
    entry = null;
    in = null;
    length = 0;
    usize = 0;
  }
  
  
  public ZipStreamInput(InputStream is, boolean usingBufferedStream) {
    this();
    this.setInput(is, usingBufferedStream);
  }
  
  
  protected void setInput(InputStream is, boolean usingBufferedStream) {
    if(is == null) throw new IllegalArgumentException(
        "Invalid InputStream: "+is);
    
    in = is;
    
    if(usingBufferedStream) is = new BufferedInputStream(is);
    
    zin = new ZipInputStream(is);
    
    try { length = is.available(); }
    catch(IOException ex) {}
  }
  

  @Override
  public InputStream getInput() {
    return zin;
  }


  @Override
  public ZipEntry getEntry() {
    return entry;
  }
  
  
  @Override
  public ZipEntry next() throws IOException {
    entry = zin.getNextEntry();
    return entry;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int length) throws IOException {
    return this.getInput().read(bs, off, length);
  }
  
  
  @Override
  public long getTotalLength() {
    return length;
  }
  
  
  @Override
  public long getUSize() {
    return usize;
  }
  
  
  public void setUSize(long us) {
    usize = us;
  }


  @Override
  public boolean close() {
    try { 
      zin.close(); 
      return true;
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    ZipStreamInput input = new ZipStreamInput(
        new FileInputStream("/home/juno/java/dir1.zip"), false);
    ZipEntry entry = null;
    while((entry = input.next()) != null)
      System.out.println(entry);
  }

}
