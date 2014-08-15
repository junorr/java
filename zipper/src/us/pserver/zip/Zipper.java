/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;



/**
 *
 * @author juno
 */
public class Zipper {
  
  private List<ZipInfo> entries;
  
  private List<Path> paths;
  
  private long totalSize;
  
  private Path output;
  
  private List<ProgressListener> listeners;
  
  private int level;
  
  
  public Zipper() {
    entries = new LinkedList<>();
    paths = new LinkedList<>();
    listeners = new LinkedList<>();
    output = null;
    totalSize = 0;
    level = 7;
  }
  
  
  public int getLevel() {
    return level;
  }


  public Zipper setLevel(int level) {
    this.level = level;
    return this;
  }
  
  
  public Zipper add(String path) {
    if(path.contains("*")) {
      processExpr(path);
    }
    else if(path != null && !path.isEmpty()) {
      paths.add(Paths.get(path));
    }
    return this;
  }
  
  
  public Zipper add(Path path) {
    if(path != null)
      paths.add(path);
    return this;
  }
  
  
  private void processExpr(String str) {
    if(str == null || str.isEmpty())
      return;
    int idx = str.indexOf("*");
    Path par = Paths.get(str.substring(0, idx));
    if(str.length() > (idx + 1))
      str = str.substring(idx+1);
    else {
      this.add(par);
      return;
    }
    
    try {
      DirectoryStream<Path> ds = Files.newDirectoryStream(par);
      for(Path p : ds) {
        if(p.toString().endsWith(str))
          this.add(p);
      }
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public boolean remove(Path p) {
    return paths.remove(p);
  }
  
  
  public List<Path> paths() {
    return paths;
  }


  public List<ProgressListener> listeners() {
    return listeners;
  }


  public Zipper addListener(ProgressListener pl) {
    if(pl != null)
      listeners.add(pl);
    return this;
  }
  
  
  public List<ZipInfo> zipEntries() {
    return entries;
  }
  
  
  public ZipInfo getZipEntry(String name) {
    if(name == null || entries.isEmpty())
      return null;
    for(ZipInfo z : entries)
      if(z.getEntry().getName().equals(name))
        return z;
    return null;
  }
  
  
  public ZipInfo removeZipEntry(String name) {
    ZipInfo z = this.getZipEntry(name);
    entries.remove(z);
    return z;
  }
  
  
  public long getTotalSize() {
    if(totalSize == 0 && !paths.isEmpty())
      for(Path p : paths)
        totalSize += this.size(p);
    
    return totalSize;
  }
  
  
  public long getZipSize() {
    if(entries.isEmpty()) return 0;
    long total = 0;
    for(ZipInfo zi : entries) {
      total += zi.getEntry().getSize();
    }
    return total;
  }
  
  
  private long size(Path p) {
    if(p == null) return 0;
    
    try {
      
      if(!Files.isDirectory(p))
        return Files.size(p);
      
      else {
        SizeFileVisitor sf = new SizeFileVisitor();
        Files.walkFileTree(p, sf);
        return sf.getSize();
      }
      
    } catch(IOException ex) {
      return 0;
    }
  }
  
  
  private void createZipEntries() {
    if(paths.isEmpty()) return;
    
    for(Path p : paths) {
      if(!Files.isDirectory(p)) {
        ZipEntry z = new ZipEntry(p.getFileName().toString());
        try { z.setSize(Files.size(p)); }
        catch(IOException e) {}
        entries.add(new ZipInfo(z, p));
        continue;
      }
      
      ZipEntryFileVisitor zef = new ZipEntryFileVisitor(p.getParent());
      try {
        Files.walkFileTree(p, zef);
      } catch(IOException ex) {
        ex.printStackTrace();
      }
      entries.addAll(zef.getEntries());
    }
  }
  
  
  public ZipFile searchZipEntries() {
    if(paths.isEmpty()) return null;
    try {
      ZipFile zf = new ZipFile(paths.get(0).toFile());
      entries.clear();
      Enumeration<ZipEntry> ens = (Enumeration<ZipEntry>) zf.entries();
      while(ens.hasMoreElements()) {
        entries.add(new ZipInfo(ens.nextElement(), null));
      }
      return zf;
    } catch(IOException ex) {
      return null;
    }
  }


  public Path getOutput() {
    return output;
  }


  public Zipper setOutput(Path out) {
    this.output = out;
    return this;
  }
  
  
  private Path getDir(Path p) {
    if(p == null) return null;
    if(Files.isDirectory(p))
      return p;
    else
      return p.getParent();
  }
  
  
  public boolean safeZip() {
    try {
      doZip();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public void doZip() throws IOException {
    this.createZipEntries();
    this.notifyMax(this.getTotalSize());
      
    if(output == null)
      output = Paths.get(paths.get(0).toString() + ".zip");
    if(Files.exists(output))
      Files.delete(output);
      
    if(output.getParent() != null 
        && !output.getRoot().equals(output.getParent()))
      Files.createDirectories(output.getParent());
    Files.createFile(output);
      
    this.doZip(output);
  }
  
  
  private void doZip(Path out) throws IOException {
    OutputStream os = Files.newOutputStream(out, StandardOpenOption.WRITE);
    ZipOutputStream zos = new ZipOutputStream(os);
    if(level >= 0 && level <= 10)
      zos.setLevel(level);
    
    for(ZipInfo zi : entries) {
      ZipEntry z = zi.getEntry();
      zos.putNextEntry(z);
      this.notifyListeners(zi.getPath());
      
      if(z.isDirectory()) {
        zos.closeEntry();
        continue;
      }
      
      InputStream is = Files.newInputStream(zi.getPath(), 
          StandardOpenOption.READ);
      this.transfer(is, zos);
      is.close();
      zos.flush();
      zos.closeEntry();
    }
    zos.flush();
    zos.close();
  }
  
  
  public boolean safeUnzip() {
    try {
      doUnzip();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  public void doUnzip() throws IOException {
    if(paths.isEmpty()) return;
    while(!paths.isEmpty()) {
      ZipFile zf = this.searchZipEntries();
      Path p = paths.remove(0);
      if(zf == null) continue;
      
      this.notifyMax(this.getZipSize());
      
      Path out = output;
      if(out == null)
        out = getDir(p);
        
      this.doUnzip(zf, out);
    }
  }
  
  
  private void doUnzip(ZipFile zf, Path out) throws IOException {
    for(ZipInfo z : entries) {
      Path dst;
      if(out != null)
        dst = Paths.get(out.toString() + "/" + z.getEntry().getName());
      else
        dst = Paths.get(z.getEntry().getName());
      this.notifyListeners(dst);
      
      if(z.getEntry().isDirectory()) {
        Path p = Paths.get(out.toString() + "/" + z.getEntry().getName());
        if(!Files.exists(p))
          Files.createDirectories(p);
      }
      else {
        if(!Files.exists(dst))
          Files.createFile(dst);
        OutputStream os = Files.newOutputStream(dst, StandardOpenOption.WRITE);
        InputStream is = zf.getInputStream(z.getEntry());
        this.transfer(is, os);
        os.close();
      }
    }
  }
  
  
  private void transfer(InputStream is, OutputStream os) throws IOException {
    if(is == null || os == null)
      return;
    
    int read = -1;
    byte[] buf = new byte[1024];
    while((read = is.read(buf)) > 0) {
      os.write(buf, 0, read);
      os.flush();
      this.notifyListeners(read);
    }
  }
  
  
  private void notifyListeners(IOException ex) {
    for(ProgressListener p : listeners)
      p.error(ex);
  }
  
  
  private void notifyListeners(long current) {
    for(ProgressListener p : listeners)
      p.update(current);
  }
  
  
  private void notifyListeners(Path current) {
    for(ProgressListener p : listeners)
      p.update(current);
  }
  
  
  private void notifyMax(long max) {
    for(ProgressListener p : listeners)
      p.setMax(max);
  }
  
}
