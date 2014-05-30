package com.jpower.jpzip;

import com.jpower.jpzip.event.AbstractZipMonitor;
import com.jpower.jpzip.event.ZipUpdateEvent;
import com.jpower.jpzip.event.ZipUpdateListener;
import com.jpower.jpzip.input.ZipFileInput;
import com.jpower.jpzip.input.Input;
import com.jpower.jpzip.input.ZipInput;
import com.jpower.jpzip.input.ZipInputContainer;
import com.jpower.jpzip.output.FileOutputFactory;
import com.jpower.jpzip.output.Output;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class ZipInflater extends AbstractZipMonitor {
  
  private ZipInputContainer container;
  
  private Output output;
  
  private CRC32 crc;
  
  private long readedcrc;
  
  private ZipUpdateEvent event;
  
  private Path path;
  
  
  public ZipInflater() {
    container = new ZipInputContainer();
    output = null;
    crc = new CRC32();
    readedcrc = 0;
    event = new ZipUpdateEvent(this);
    path = new Path();
  }
  
  
  public ZipInflater(ZipInput in, Output out) {
    this();
    container.add(in);
    this.output = out;
  }
  
  
  public Path path() {
    return path;
  }
  
  
  public void setPath(Path p) {
    path = p.clone();
  }


  public ZipInput getInput() {
    return container.get();
  }


  public void setInput(ZipInput input) {
    container.clear();
    container.add(input);
  }


  public Output getOutput() {
    return output;
  }


  public void setOutput(Output output) {
    this.output = output;
  }
  
  
  public void setInput(ZipInputContainer cont) {
    if(cont == null || cont.size() == 0)
      throw new IllegalArgumentException(
          "Invalid Input: " + 
          (cont == null ? cont : "size="+cont.size()));
    
    container = cont;
  }
  
  
  public ZipInputContainer container() {
    return container;
  }
  
  
  public void inflate() throws IOException {
    if(container == null || container.size() == 0)
      throw new IllegalArgumentException(
          "Invalid Input: " + 
          (container == null ? container 
          : "size=" + container.size()));
    
    ZipInput input = null;
    boolean createOutput = false;
    
    event.setTotal(container.calculateTotalUSize());
    System.out.println("event.getTotal(): "+event.getTotal());
    
    Iterator<ZipInput> iter = container.iterator();
    while(iter.hasNext()) {
      input = iter.next();
      ZipEntry entry = null;
      
      while((entry = input.next()) != null) {
      
        if(this.readCRC32(input)) continue;
        
        event.setProcessedEntry(entry);
        
        if(output == null) {
          createOutput = true;
          output = FileOutputFactory.create(input.getEntry(), path.resolve());
        }

        this.transfer(input, output);
        output.close();
        if(createOutput) output = null;
      }
    }
    
    input.close();
    /*
    if(readedcrc > 0) {
      long value = crc.getValue();
      if(readedcrc != value)
        throw new IOException("Corrupted CRC32 Zip File: "
            + "Recorded= "+readedcrc + "; "
            + "Calculated= "+value);
    }*/
  }
  
  
  private boolean readCRC32(ZipInput in) throws IOException {
    if(in == null) return false;
    if(in.getEntry().getName().equals("CRC32")) {
      readedcrc = this.readLong(in);
      return true;
    }
    return false;
  }
  
  
  private long readLong(ZipInput in) throws IOException {
    byte[] bs = new byte[Long2ByteArray.LONG_SHIFT_SIZE];
    int read = in.read(bs, 0, bs.length);
    if(read <= 0) return -1;
    
    this.fireZipUpdate(read);
    long l = Long2ByteArray.fromByteArray(bs, 0, read);
    return l;
  }
  
  
  private void fireZipUpdate(int value) {
    event.setValue(value + event.getValue());
    this.notifyListeners(event);
  }
  
  
  private void transfer(Input in, Output out) throws IOException {
    if(in == null || out == null) return;
    
    byte[] buffer = new byte[2048];
    int read = -2;
    
    while((read = in.read(buffer, 0, buffer.length)) > 0) {
      crc.update(buffer, 0, read);
      out.write(buffer, 0, read);
      
      this.fireZipUpdate(read);
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    File zip = new File("/home/juno/java/dir1.zip");
    
    ZipFileInput input = new ZipFileInput(zip);
    
    ZipUpdateListener listener = new ZipUpdateListener() {
      @Override
      public void update(ZipUpdateEvent event) {
        System.out.println(
            "Progress: " + event.getFormattedPercent() + 
            "; Entry: " + event.getName());
      }
    };
    
    System.out.println("Size: " + input.getTotalLength() + " bytes");
    System.out.println("Size: " + (input.getTotalLength() / 1024) + " KB");
    
    ZipInflater inf = new ZipInflater();
    inf.setInput(input);
    inf.addListener(listener);
    inf.path().addPath("/home/juno/java/dir1-test");
    inf.inflate();
  }
    
}
