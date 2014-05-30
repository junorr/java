/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zip;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;



/**
 *
 * @author juno
 */
public class ZipEntryFileVisitor implements FileVisitor<Path> {
  
  private List<ZipInfo> entries;
  
  private String sdir;
  
  private Path origDir;
  
  
  public ZipEntryFileVisitor(Path dir) {
    entries = new LinkedList<>();
    origDir = dir;
    
    if(dir != null && !Files.isDirectory(dir))
      throw new IllegalArgumentException(
          "Path is not a directory: "+ dir.toString());
  }


  public List<ZipInfo> getEntries() {
    return entries;
  }


  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    String s = null;
    if(origDir != null)
      try {
        s = dir.toString().substring(origDir.toString().length());
        s = s.replace("\\", "/");
        if(s.startsWith("/"))
          s = s.substring(1);
      } catch(Exception e) {}
    else
      s = dir.toString();
    
    if(s != null && !s.isEmpty()) {
      ZipEntry z = new ZipEntry(s + "/");
      z.setSize(0);
      entries.add(new ZipInfo(z, dir));
    }
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String s = null;
    if(origDir != null)
      try {
        s = file.toString().substring(origDir.toString().length());
        s = s.replace("\\", "/");
        if(s.startsWith("/"))
          s = s.substring(1);
      } catch(Exception e) {}
    else
      s = file.toString();
    
    if(s != null && !s.isEmpty()) {
      ZipEntry z = new ZipEntry(s);
      z.setSize(Files.size(file));
      entries.add(new ZipInfo(z, file));
    }
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.TERMINATE;
  }


  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }
  
}
