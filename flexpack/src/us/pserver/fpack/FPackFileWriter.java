/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.streams.StreamConnector;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class FPackFileWriter {
  
  private final List<FPackFileEntry> entries;
  
  
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
  
  
  public void write(Path path) throws IOException {
    this.write(Files.newOutputStream(path, 
        StandardOpenOption.WRITE, 
        StandardOpenOption.CREATE
    ));
  }
  
  
  public void write(OutputStream out) throws IOException {
    Valid.off(out).forNull().fail(OutputStream.class);
    Valid.off(entries).forEmpty().fail();
    FPackFileHeader hds = new FPackFileHeader();
    entries.forEach(e->hds.add(e.getHeader()));
    out.write(1);
    byte[] end = FPackConstants.ENTRY_END.getBytes();
    int hdlen = new UTF8String(JsonWriter.objectToJson(hds)).getBytes().length;
    long pos = 1 + hdlen + end.length;
    for(FPackFileEntry e : entries) {
      e.setPosition(pos);
      pos += e.getSize() + end.length;
    }
    
    hds.clear();
    entries.forEach(e->hds.add(e.getHeader()));
    out.write(new UTF8String(JsonWriter.objectToJson(hds)).getBytes());
    out.write(end);
    Iterator<FPackFileEntry> it = entries.iterator();
    while(it.hasNext()) {
      FPackFileEntry e = it.next();
      out.write(new UTF8String(JsonWriter.objectToJson(e)).getBytes());
      out.write(FPackConstants.ENTRY_END.getBytes());
      StreamConnector.builder()
          .from(e.getInputStream())
          .to(out)
          .get()
          .connect()
          .closeInput();
      out.write(FPackConstants.ENTRY_END.getBytes());
    }
    out.flush();
    out.close();
  }
  
}
