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

package us.pserver.redfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.range;
import us.pserver.streams.IO;
import us.pserver.zip.Unzip;
import us.pserver.zip.Zip;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/11/2013
 */
public class LocalFileSystem {

  private Path current;
  
  private HostInfo hostInfo;
  
  
  public LocalFileSystem() {
    Iterator<Path> it = FileSystems.getDefault()
        .getRootDirectories().iterator();
    current = it.next();
    hostInfo = HostInfo.getHostInfo();
  }
  
  
  public HostInfo getHostInfo() {
    return hostInfo;
  }
  
  
  public RFile current() {
    return LocalFileFactory.create(current);
  }
  
  
  public List<RFile> ls() {
    return ls(current);
  }
  
  
  public List<RFile> ls(Path path) {
    path = getPath(path);
    if(path == null) return null;
    try {
      List<RFile> list = new LinkedList<>();
      if(!Files.isDirectory(path)) {
        list.add(getFile(path));
        return list;
      }
      DirectoryStream<Path> ds = Files.newDirectoryStream(path);
      for(Path p : ds) {
        RFile rf = LocalFileFactory.create(p);
        if(rf != null) list.add(rf);
      }
      return list;
    } catch(IOException ex) {
      return null;
    }
  }
  
  
  public List<RFile> ls(String path) {
    return ls(getPath(path));
  }
  
  
  public List<RFile> ls(RFile rf) {
    return ls(getPath(rf));
  }
  
  
  public boolean contains(Path path) {
    if(path == null || current == null)
      return false;
    try {
      DirectoryStream<Path> ds = Files.newDirectoryStream(current);
      for(Path p : ds) {
        if(p.getFileName().equals(path)
            || p.getFileName().equals(path.getFileName())) 
          return true;
      }
      return false;
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  public boolean contains(RFile rf) {
    if(rf == null || current == null)
      return false;
    return contains(getPath(rf));
  }
  
  
  public boolean contains(String path) {
    if(path == null || current == null)
      return false;
    return contains(getPath(path));
  }
  
  
  private Path getPath(Path path) {
    if(path == null || current == null)
      return null;
    
    if(path.getRoot() == null || path.startsWith(".")) {
      path = Paths.get(current.toString(), "/", 
          path.toString()).toAbsolutePath().normalize();
    }
    return path;
  }
  
  
  private Path getPath(RFile rf) {
    if(rf == null || current == null)
      return null;
    return getPath(Paths.get(rf.getPath()));
  }
  
  
  private Path getPath(String path) {
    if(path == null || current == null)
      return null;
    return getPath(Paths.get(path));
  }
  
  
  public RFile getFile(RFile rf) {
    Path p = this.getPath(rf);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  public RFile getFile(String path) {
    Path p = this.getPath(path);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  public RFile getFile(Path path) {
    Path p = this.getPath(path);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  private boolean cd(Path path) {
    if(path == null || current == null)
      return false;
    Path p = getPath(path);
    if(p == null || !Files.exists(p)) return false;
    current = p;
    return true;
  }
  
  
  public boolean cd(RFile rf) {
    if(rf == null || rf.getName() == null 
        || current == null)
      return false;
    return cd(getPath(rf));
  }
  
  
  public boolean cd(String path) {
    return cd(getPath(path));
  }
  
  
  public boolean rm(RFile rf) throws IOException {
    if(rf == null || rf.getPath() == null)
      throw new FileNotFoundException(Objects.toString(rf));
    return rm(getPath(rf));
  }
  
  
  public boolean rm(String str) throws IOException {
    if(str == null || str.isEmpty()) {
      throw new FileNotFoundException(Objects.toString(str));
    }
     return rm(getPath(str));
  }
  
  
  public boolean rm(Path path) throws IOException {
    path = getPath(path);
    if(path == null || !Files.exists(path)) {
      throw new FileNotFoundException(Objects.toString(path));
    }
    if(Files.isDirectory(path)) {
      throw new IOException(
          "Path is a Directory: "+ path.toString());
    }
    try {
      System.out.println("* Files.delete("+ path+ ")");
      Files.delete(path);
    } catch(IOException e) {
      e.printStackTrace();
      throw e;
    }
    return true;
  }
  
  
  public boolean rmdir(RFile rf, boolean ignoreErrors) throws IOException {
    if(rf == null || rf.getPath() == null) {
      throw new FileNotFoundException(Objects.toString(rf));
    }
    return rmdir(getPath(rf), ignoreErrors);
  }
  
  
  public boolean rmdir(String str, boolean ignoreErrors) throws IOException {
    if(str == null || str.isEmpty()) {
      throw new FileNotFoundException(Objects.toString(str));
    }
    return rmdir(getPath(str), ignoreErrors);
  }
  
  
  public boolean rmdir(Path path, final boolean ignoreErrors) throws IOException {
    path = getPath(path);
    if(path == null || !Files.exists(path)) {
      throw new FileNotFoundException(Objects.toString(path));
    }
    if(!Files.isDirectory(path)) {
      throw new IOException(
          "Path is Not a Directory: "+ path.toString());
    }
    
    if(path.equals(current) || path.getFileName()
        .equals(current.getFileName()))
      this.cd("..");
    
    FileVisitor<Path> fv = new FileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        rm(file);
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if(!ignoreErrors) 
          return FileVisitResult.TERMINATE;
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    };
    Files.walkFileTree(path, fv);
    return true;
  }
  
  
  public boolean mkdir(String path) throws IOException {
    if(path == null || path.isEmpty())
      return false;
    return mkdir(getPath(path));
  }
  
  
  public boolean mkdir(RFile path) throws IOException {
    if(path == null || path.getPath() == null)
      return false;
    return mkdir(getPath(path));
  }
  
  
  public boolean mkdir(Path path) throws IOException {
    if(path == null) return false;
    if(path.getRoot() == null)
      path = Paths.get(current.toString(), "/", path.toString());
    if(!Files.exists(path))
      Files.createDirectories(path);
    return true;
  }
  
  
  public boolean zip(Path out, Path ... srcs) throws IOException {
    if(out.getRoot() == null)
      out = Paths.get(current.toString(), "/", out.toString());
    if(out == null || srcs == null || srcs.length < 1)
      return false;
    
    Zip zip = new Zip().output(out);
    for(Path p : srcs) {
      if(p.getRoot() == null)
        p = Paths.get(current.toString(), 
            "/", p.toString());
      zip.add(getPath(p));
    }
    zip.run();
    return true;
  }
  
  
  public boolean zip(String out, String ... srcs) throws IOException {
    if(out == null || srcs == null || srcs.length < 1)
      return false;
    Path pt = getPath(out);
    System.out.println("* zip.out="+ pt);
    Path[] pts = new Path[srcs.length];
    for(int i = 0; i < srcs.length; i++) {
      pts[i] = getPath(srcs[i]);
      System.out.println("* zip.in="+ pts[i]);
    }
    return zip(pt, pts);
  }
  
  
  public boolean zip(RFile out, RFile ... srcs) throws IOException {
    if(out == null || srcs == null || srcs.length < 1)
      return false;
    String[] ss = new String[srcs.length];
    for(int i = 0; i < srcs.length; i++) {
      ss[i] = srcs[i].getPath();
    }
    return zip(out.getPath(), ss);
  }
  
  
  public boolean unzip(Path src, Path out) throws IOException {
    if(src == null)
      return false;
    if(src.getRoot() == null)
      src = Paths.get(current.toString(), 
          "/", src.toString());
    if(out != null && out.getRoot() == null)
      out = Paths.get(current.toString(), 
          "/", out.toString());
    
    Unzip uz = new Unzip().input(src);
    if(out != null) uz.output(out);
    uz.run();
    return true;
  }
  
  
  public boolean unzip(String src, String out) throws IOException {
    if(src == null || src.isEmpty())
      return false;
    return unzip(getPath(src), getPath(out));
  }
  
  
  public boolean unzip(RFile src, RFile out) throws IOException {
    if(src == null || src.getPath() == null)
      return false;
    return unzip(getPath(src), getPath(out));
  }
  
  
  public InputStream readFile(IOData data) throws IOException {
    nullarg(IOData.class, data);
    nullarg(RFile.class, data.getRFile());
    
    RFile rf = data.getRFile();
    if(rf.getSize() == null || rf.getSize().size() == 0)
      rf = this.getFile(rf);
    
    nullarg(RFile.class, rf);
    nullarg(Size.class, rf.getSize());
    range(data.getStartPos(), 0, rf.getSize().size()-1);
    
    Path p = getPath(rf);
    return new ProgressInputStream(IO.is(p), data);
  }
  
  
  public boolean write(InputStream is, IOData data) throws IOException {
    nullarg(InputStream.class, is);
    nullarg(IOData.class, data);
    
    data.setStartPos(0);
    ProgressInputStream pis = new ProgressInputStream(is, data);
    Path p = getPath(data.getRFile());
    OutputStream os = IO.os(p);
    FSConst.transfer(pis, os);
    IO.cl(is, os);
    return true;
  }
  
  
  public long getCRC32(Path pth) throws IOException {
    return FSConst.getCRC32(this.getPath(pth));
  }
  
  
  public long getCRC32(RFile rf) throws IOException {
    return getCRC32(this.getPath(rf));
  }
  
  
  public long getCRC32(String pth) throws IOException {
    return getCRC32(this.getPath(pth));
  }
  
  
  @Override
  public String toString() {
    return String.valueOf(current);
  }
  
}
