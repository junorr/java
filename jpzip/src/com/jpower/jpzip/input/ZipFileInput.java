package com.jpower.jpzip.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 *
 * @author juno
 */
public class ZipFileInput extends ZipStreamInput {
  
  private File file;
  
  private long length;
  
  private List<ZipEntry> entries;
  
  private Iterator<ZipEntry> iterator;
  
  
  private ZipFileInput() {
    super();
    file = null;
    length = 0;
    entries = new LinkedList<ZipEntry>();
    iterator = null;
  }
  
  
  public ZipFileInput(File f) {
    this();
    if(f == null || !f.exists()) throw new IllegalArgumentException(
        "File must be NOT NULL.");
    
    this.file = f;
    length = file.length();
    
    try {
      ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
      ZipEntry e = null;
      while((e = zis.getNextEntry()) != null) {
        entries.add(e);
      }
      zis.close();
      zis = null;
    
      iterator = entries.iterator();
    
      FileInputStream fos = new FileInputStream(file);
      super.setInput(fos, false);
    } catch(IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
  
  
  public File getFile() {
    return file;
  }
  
  
  @Override
  public long getTotalLength() {
    return length;
  }
  
  
  @Override
  public long getUSize() {
    long usize = 0;
    for(ZipEntry e : entries)
      usize += e.getSize();
    return usize;
  }


  public static void main(String[] args) throws IOException {
    ZipFileInput input = new ZipFileInput(new File("/home/juno/java/dir1.zip"));
    ZipEntry entry = null;
    while((entry = input.next()) != null)
      System.out.println(entry);
  }
  
}
