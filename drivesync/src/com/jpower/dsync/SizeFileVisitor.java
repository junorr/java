/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.dsync;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/04/2013
 */
public class SizeFileVisitor implements FileVisitor<Path> {

  private Path path;
  
  private long size;
  
  
  public SizeFileVisitor(String path) {
    if(path == null || path.trim().isEmpty())
      throw new IllegalArgumentException(
          "Invalid path: "+ path);
    
    size = 0;
    this.path = Paths.get(path);
    if(!Files.exists(this.path))
      throw new IllegalArgumentException(
          "path does not exists: "+ path);
  }
  
  
  public SizeFileVisitor(Path path) {
    if(path == null || !Files.exists(path))
      throw new IllegalArgumentException(
          "Invalid path: "+ path);
    size = 0;
    this.path = path;
  }
  
  
  public long calculateSize() {
    size = 0;
    try {
      Files.walkFileTree(path, this);
      return size;
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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

  
  public static void main(String[] args) {
    SizeFileVisitor sf = new SizeFileVisitor("F:/java");
    System.out.println("* calculating size (F:/java) ...");
    System.out.println("* size = "+ sf.calculateSize());
  }
  
}
