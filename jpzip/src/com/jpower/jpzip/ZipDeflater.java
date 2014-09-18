package com.jpower.jpzip;

import com.jpower.jpzip.event.AbstractZipMonitor;
import com.jpower.jpzip.event.ZipUpdateEvent;
import com.jpower.jpzip.event.ZipUpdateListener;
import com.jpower.jpzip.input.DirectoryExplorer;
import com.jpower.jpzip.input.Input;
import com.jpower.jpzip.input.InputContainer;
import com.jpower.jpzip.output.Output;
import com.jpower.jpzip.output.ZipFileOutput;
import com.jpower.jpzip.output.ZipOutput;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class ZipDeflater extends AbstractZipMonitor {
  
  private ZipOutput output;
  
  private InputContainer container;
  
  private CRC32 crc;
  
  private ZipUpdateEvent event;
  
  private boolean includeCRC32;
  
  
  public ZipDeflater() {
    output = null;
    container = new InputContainer();
    crc = new CRC32();
    includeCRC32 = true;
    event = new ZipUpdateEvent(this);
  }
  
  
  public ZipDeflater(Input in, ZipOutput out) {
    this();
    this.setInput(in);
    this.setOutput(output);
  }


  public Input getInput() {
    return container.get();
  }


  public void setInput(Input input) {
    container.clear();
    container.add(input);
  }
  
  
  public void setInput(InputContainer cont) {
    container = cont;
  }
  
  
  public InputContainer container() {
    return container;
  }


  public ZipOutput getOutput() {
    return output;
  }


  public void setOutput(ZipOutput output) {
    this.output = output;
  }
  
  
  public void setIncludeCRC32(boolean include) {
    this.includeCRC32 = include;
  }
  
  
  public boolean isIncludeCRC32() {
    return this.includeCRC32;
  }


  public void deflate() throws IOException {
    Input input = null;
    
    event.setTotal(container.calculateTotalLength(container.getAll()) + 
        (includeCRC32 ? Long2ByteArray.LONG_SHIFT_SIZE : 0));
    
    Iterator<Input> iter = container.iterator();
    while(iter.hasNext()) {
      input = iter.next();
      
      event.setProcessedEntry(input.getEntry());
      output.putEntry(input.getEntry());
      this.transfer(input, output);
      
      input.close();
    }
    
    if(includeCRC32)
      this.putCRC32();
    
    output.close();
  }
  
  
  private void putLong(long l, ZipEntry e) throws IOException {
    if(e == null) return;
    e.setSize(Long2ByteArray.LONG_SHIFT_SIZE);
    output.putEntry(e);
    byte[] bs = Long2ByteArray.toByteArray(l);
    output.write(bs, 0, bs.length);
    this.fireZipUpdate(Long2ByteArray.LONG_SHIFT_SIZE);
  }
  
  
  private void putCRC32() throws IOException {
    this.putLong(crc.getValue(), new ZipEntry("CRC32"));
  }
  
  
  private void fireZipUpdate(long value) {
    event.setValue(value + event.getValue());
    this.notifyListeners(event);
  }
  
  
  private void transfer(Input in, Output out) throws IOException {
    if(in == null || out == null) return;
    
    byte[] buffer = new byte[2048];
    int read = -1;
    
    while((read = in.read(buffer, 0, buffer.length)) > 0) {
      crc.update(buffer, 0, read);
      out.write(buffer, 0, read);
      
      this.fireZipUpdate(read);
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    File dir = new File("/home/juno/java/dir1");
    DirectoryExplorer de = new DirectoryExplorer();
    de.setDirectory(dir);
    InputContainer input = de.explore();
    
    long length = input.calculateTotalLength(input.getAll());
    System.out.println("Size: " + length + " bytes");
    System.out.println("Size: " + (length / 1024) + " KB");
    
    Input i = null;
    Iterator<Input> iter = input.iterator();
    while(iter.hasNext()) System.out.println(iter.next());
    
    File zip = new File("/home/juno/java/dir1.zip");
    ZipFileOutput output = new ZipFileOutput(zip, true);
    
    ZipDeflater def = new ZipDeflater();
    def.setInput(input);
    def.setOutput(output);
    
    ZipUpdateListener listener = new ZipUpdateListener() {
      @Override
      public void update(ZipUpdateEvent event) {
        System.out.println(
            "Progress: " + event.getFormattedPercent() + 
            "; Entry: " + event.getName());
      }
    };
    
    def.addListener(listener);
    def.deflate();
    
    length = zip.length();
    System.out.println("Size: " + length + " bytes");
    System.out.println("Size: " + (length / 1024) + " KB");
  }
  
}
