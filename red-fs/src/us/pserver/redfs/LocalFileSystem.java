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
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import us.pserver.zip.Zipper;

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
  
  
  public RemoteFile getCurrent() {
    return LocalFileFactory.create(current);
  }
  
  
  public List<RemoteFile> ls() {
    return ls(current);
  }
  
  
  public List<RemoteFile> ls(Path path) {
    path = getPath(path);
    if(path == null) return null;
    try {
      DirectoryStream<Path> ds = Files.newDirectoryStream(path);
      List<RemoteFile> list = new LinkedList<>();
      for(Path p : ds) {
        list.add(LocalFileFactory.create(p));
      }
      return list;
    } catch(IOException ex) {
      return null;
    }
  }
  
  
  public List<RemoteFile> ls(String path) {
    return ls(getPath(path));
  }
  
  
  public List<RemoteFile> ls(RemoteFile rf) {
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
  
  
  public boolean contains(RemoteFile rf) {
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
    if(Files.exists(path)) return path;
    
    return null;
  }
  
  
  private Path getPath(RemoteFile rf) {
    if(rf == null || current == null)
      return null;
    return getPath(Paths.get(rf.getPath()));
  }
  
  
  private Path getPath(String path) {
    if(path == null || current == null)
      return null;
    return getPath(Paths.get(path));
  }
  
  
  public RemoteFile getFile(RemoteFile rf) {
    Path p = this.getPath(rf);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  public RemoteFile getFile(String path) {
    Path p = this.getPath(path);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  public RemoteFile getFile(Path path) {
    Path p = this.getPath(path);
    if(p == null) return null;
    return LocalFileFactory.create(p);
  }
  
  
  private boolean cd(Path path) {
    if(path == null || current == null)
      return false;
    Path p = getPath(path);
    if(p == null) return false;
    current = p;
    return true;
  }
  
  
  public boolean cd(RemoteFile rf) {
    if(rf == null || rf.getName() == null 
        || current == null)
      return false;
    return cd(getPath(rf));
  }
  
  
  public boolean cd(String path) {
    return cd(getPath(path));
  }
  
  
  public boolean rm(RemoteFile rf) throws IOException {
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
    
    Files.delete(path);
    return true;
  }
  
  
  public boolean rmDir(RemoteFile rf, boolean ignoreErrors) throws IOException {
    if(rf == null || rf.getPath() == null) {
      throw new FileNotFoundException(Objects.toString(rf));
    }
    return rmDir(getPath(rf), ignoreErrors);
  }
  
  
  public boolean rmDir(String str, boolean ignoreErrors) throws IOException {
    if(str == null || str.isEmpty()) {
      throw new FileNotFoundException(Objects.toString(str));
    }
    return rmDir(getPath(str), ignoreErrors);
  }
  
  
  public boolean rmDir(Path path, final boolean ignoreErrors) throws IOException {
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
  
  
  public boolean mkDir(String path) throws IOException {
    if(path == null || path.isEmpty())
      return false;
    return mkDir(getPath(path));
  }
  
  
  public boolean mkDir(RemoteFile path) throws IOException {
    if(path == null || path.getPath() == null)
      return false;
    return mkDir(getPath(path));
  }
  
  
  public boolean mkDir(Path path) throws IOException {
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
    
    Zipper zip = new Zipper();
    for(Path p : srcs) {
      if(p.getRoot() == null)
        p = Paths.get(current.toString(), 
            "/", p.toString());
      zip.add(getPath(p));
    }
    zip.setOutput(out);
    zip.doZip();
    return true;
  }
  
  
  public boolean zip(String out, String ... srcs) throws IOException {
    if(out == null || srcs == null || srcs.length < 1)
      return false;
    Path pt = getPath(out);
    Path[] pts = new Path[srcs.length];
    for(int i = 0; i < srcs.length; i++) {
      pts[i] = getPath(srcs[i]);
    }
    return zip(pt, pts);
  }
  
  
  public boolean zip(RemoteFile out, RemoteFile ... srcs) throws IOException {
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
    Zipper zip = new Zipper();
    if(out != null) zip.setOutput(out);
    zip.add(src).doUnzip();
    return true;
  }
  
  
  public boolean unzip(String src, String out) throws IOException {
    if(src == null || src.isEmpty())
      return false;
    return unzip(getPath(src), getPath(out));
  }
  
  
  public boolean unzip(RemoteFile src, RemoteFile out) throws IOException {
    if(src == null || src.getPath() == null)
      return false;
    return unzip(getPath(src), getPath(out));
  }
  
  
  public void readFile(Path path, SocketChannel channel) throws IOException {
    path = getPath(path);
    if(path == null) return;
    readPart(path, 0, Files.size(path), channel);
  }
  
  
  public void readFile(String path, SocketChannel channel) throws IOException {
    readFile(getPath(path), channel);
  }
  
  
  public void readFile(RemoteFile rf, SocketChannel channel) throws IOException {
    if(rf == null) return;
    readFile(getPath(rf), channel);
  }
  
  
  public void readPart(Path path, long from, long len, 
      SocketChannel channel) throws IOException {
    
    path = getPath(path);
    if(path == null || channel == null
        || !channel.isConnected()
        || from < 0 || len < 1) 
      return;
    
    ByteBuffer buf = ByteBuffer.allocateDirect(
        FSConstants.BUFFER_SIZE);
    FileChannel fch = FileChannel.open(
        path, StandardOpenOption.READ);
    if(from >= fch.size()) {
      fch.close();
      channel.close();
      return;
    }
    fch.position(from);
    
    int read = 1;
    long total = 0;
    while(read > 0 && total < len) {
      read = fch.read(buf);
      buf.flip();
      if((len - total) < read) {
        buf.limit((int) (len - total));
        total += (len - total);
      } 
      else total += read;
      channel.write(buf);
      buf.clear();
    }
    fch.close();
    channel.shutdownInput();
    channel.shutdownOutput();
    channel.close();
  }
  
  
  public void readPart(String path, long from, long len,
      SocketChannel channel) throws IOException {
    readPart(getPath(path), from, len, channel);
  }
  
  
  public void readPart(RemoteFile rf, long from, long len,
      SocketChannel channel) throws IOException {
    if(rf == null) return;
    readPart(getPath(rf), from, len, channel);
  }
  
  
  public boolean write(SocketChannel channel, Path path, long pos) throws IOException {
    if(path == null || channel == null || pos < 0
        || !channel.isConnected()) return false;
    
    if(path.getRoot() == null)
      path = Paths.get(current.toString(), 
          "/", path.toString());
    
    if(path.getParent() != null
        && !path.getParent().equals(path.getRoot()))
      Files.createDirectories(path.getParent());
    if(!Files.exists(path))
      Files.createFile(path);
      
    FileChannel fch = FileChannel.open(path, 
        StandardOpenOption.WRITE);
    if(pos != 0) fch.position(pos);
    this.copyChannels(channel, fch);
    fch.close();
    channel.shutdownInput();
    channel.shutdownOutput();
    channel.close();
    return true;
  }
  
  
  public boolean write(SocketChannel channel, String path, long pos) throws IOException {
    return this.write(channel, getPath(path), pos);
  }
  
  
  public boolean write(SocketChannel channel, RemoteFile rf, long pos) throws IOException {
    return this.write(channel, getPath(rf), pos);
  }
  
  
  private void copyChannels(ByteChannel src, ByteChannel dst) throws IOException {
    if(src == null || dst == null) return;
    ByteBuffer buf = ByteBuffer.allocateDirect(
        FSConstants.BUFFER_SIZE);
    int read = 1;
    while(read > 0) {
      read = src.read(buf);
      buf.flip();
      dst.write(buf);
      buf.clear();
    }
  }
  
  
  public long getCRC32(Path pth) throws IOException {
    Path path = this.getPath(pth);
    if(path == null) return -1;
    CRC32 crc = new CRC32();
    FileChannel fc = FileChannel.open(path, 
        StandardOpenOption.READ);
    ByteBuffer buf = ByteBuffer.allocateDirect(
        FSConstants.BUFFER_SIZE);
    int read = 1;
    while(read > 0) {
      read = fc.read(buf);
      if(read <= 0) break;
      buf.flip();
      byte[] bs = new byte[read];
      buf.get(bs);
      crc.update(bs);
      buf.clear();
    }
    return crc.getValue();
  }
  
  
  public long getCRC32(RemoteFile rf) throws IOException {
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
