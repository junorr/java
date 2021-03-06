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

package us.pserver.bigjar;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/01/2014
 */
public class FileWalker implements FileVisitor<Path> {

  private LinkedList<String> entries;
  
  private Path dir;
  
  
  public FileWalker(String dir) {
    this(Paths.get(dir).toAbsolutePath());
  }
  
  
  public FileWalker(Path d) {
    if(d == null || !Files.exists(d))
      throw new IllegalArgumentException("Invalid dir to walk ["+ d+ "]");
    this.dir = d;
    entries = new LinkedList<>();
  }
  
  
  public List<String> walk() {
    try {
      Files.walkFileTree(dir, this);
    } catch(IOException e) {}
    return entries;
  }
  
  
  @Override
  public FileVisitResult preVisitDirectory(Path pd, BasicFileAttributes attrs) throws IOException {
    String str = dir.relativize(pd).toString();
    str = str.replace("\\", "/");
    if(!str.endsWith("/")) str += "/";
    if(!str.equals("/") && !entries.contains(str)) {
      entries.add(str);
    }
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String str = dir.relativize(file).toString();
    str = str.replace("\\", "/");
    if(!entries.contains(str))
      entries.add(str);
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }
  
  
  public static void main(String[] args) {
    FileWalker fw = new FileWalker("c:/.local/java/bigjar/dist/bigjar");
    List l = fw.walk();
    System.out.println("------------------------");
    for(Object o : l) {
      System.out.println(o);
    }
  }
  
}
