package com.jpower.jpzip.input;

import com.jpower.jpzip.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class FileInput implements Input {
  
  private InputStream in;
  
  private File file;
  
  private Path path;
  
  private long totallength;


  private FileInput() {
    totallength = 0;
    file = null;
    path = new Path();
    in = null;
  }
  
  
  public FileInput(File f) {
    this();
    this.setFile(f);
  }
  
  
  public FileInput(File f, String path) {
    this();
    this.path.addPath(path);
    this.setFile(f);
  }
  
  
  public void setFile(File f) {
    if(f == null || !f.exists())
      throw new IllegalArgumentException("File not found: "+f);
    
    this.file = f;
    if(file.isDirectory())
      path.addPath(file.getName());
    else {
      path.setName(file.getName());
      this.totallength = file.length();
    }
  }
  
  
  public File getFile() {
    return file;
  }
  
  
  public Path path() {
    return path;
  }
  
  
  @Override
  public long getTotalLength() {
    return totallength;
  }
  
  
  @Override
  public InputStream getInput() throws IOException {
    if(in == null)
      in = new FileInputStream(file);
    return in;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int length) throws IOException {
    return this.getInput().read(bs, off, length);
  }
  
  
  public FileChannel getChannel() throws IOException {
    this.getInput();
    return ((FileInputStream) in).getChannel();
  }


  @Override
  public ZipEntry getEntry() {
    ZipEntry z = new ZipEntry(path.resolve());
    z.setSize(file.length());
    z.setTime(file.lastModified());
    return z;
  }
  
  
  @Override
  public boolean close() {
    try {
      in.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public String toString() {
    return path.toString();
  }
  
}
