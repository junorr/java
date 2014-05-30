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



/**
 *
 * @author juno
 */
public class SizeFileVisitor implements FileVisitor<Path> {
  
  private long size;
  
  
  public SizeFileVisitor() {
    size = 0;
  }
  
  
  public SizeFileVisitor(long s) {
    size = s;
  }
  
  
  public long getSize() {
    return size;
  }
  
  
  public void setSize(long s) {
    size = s;
  }


  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if(Files.isRegularFile(file))
      size += Files.size(file);
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
