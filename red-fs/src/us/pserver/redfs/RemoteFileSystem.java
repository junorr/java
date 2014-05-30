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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.remote_objects.DefaultProtocolFactoryProvider;
import us.pserver.remote_objects.Host;
import us.pserver.remote_objects.ProtocolConnection;
import us.pserver.remote_objects.RemoteMethod;
import us.pserver.remote_objects.RemoteObject;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/11/2013
 */
public class RemoteFileSystem {
  
  private RemoteObject rob;
  
  
  public RemoteFileSystem(Host host) {
    rob = new RemoteObject(host, DefaultProtocolFactoryProvider.getHttpRequestConnectionFactory());
    if(!this.checkConn())
      throw new IllegalArgumentException(
          "Server do not respond properly: "+ host);
  }
  
  
  public RemoteFileSystem(String host, int port) {
    this(new Host(host, port));
  }
  
  
  private boolean checkConn() {
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.ObjectServer.name())
        .method(Tokens.containsObject.name())
        .args(Tokens.LocalFileSystem.name());
    try { rob.invoke(rm); }
    catch(Exception e) { 
      return false; 
    }
    return true;
  }
  
  
  public void closeConnection() {
    rob.closeConnection();
  }
  
  
  public HostInfo getHostInfo() throws RemoteException {
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getHostInfo.name());
    return (HostInfo) rob.invoke(rm);
  }
  
  
  public RemoteFile getFile(RemoteFile rf) throws RemoteException {
    if(rf == null || rf.getPath() == null)
      return rf;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getFile.name())
        .argTypes(RemoteFile.class)
        .args(rf);
    return (RemoteFile) rob.invoke(rm);
  }
  
  
  public RemoteFile getFile(String path) throws RemoteException {
    if(path == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getFile.name())
        .argTypes(String.class)
        .args(path);
    return (RemoteFile) rob.invoke(rm);
  }
  
  
  public RemoteFile getCurrent() throws RemoteException {
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getCurrent.name());
    return (RemoteFile) rob.invoke(rm);
  }
  
  
  public List<RemoteFile> ls() throws RemoteException {
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name());
    return (List<RemoteFile>) rob.invoke(rm);
  }
  
  
  public List<RemoteFile> ls(RemoteFile rf) throws RemoteException {
    if(rf == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name())
        .argTypes(RemoteFile.class)
        .args(rf);
    return (List<RemoteFile>) rob.invoke(rm);
  }
  
  
  public List<RemoteFile> ls(String str) throws RemoteException {
    if(str == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name())
        .argTypes(String.class)
        .args(str);
    return (List<RemoteFile>) rob.invoke(rm);
  }
  
  
  public boolean cd(RemoteFile rf) throws RemoteException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.cd.name())
        .argTypes(RemoteFile.class)
        .args(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean cd(String str) throws RemoteException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.cd.name())
        .argTypes(String.class)
        .args(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rm(RemoteFile rf) throws RemoteException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rm.name())
        .argTypes(RemoteFile.class)
        .addArg(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rmDir(RemoteFile rf) throws RemoteException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rmDir.name())
        .argTypes(RemoteFile.class)
        .addArg(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rm(String str) throws RemoteException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rm.name())
        .argTypes(String.class)
        .addArg(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rmDir(String str) throws RemoteException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rmDir.name())
        .argTypes(String.class)
        .addArg(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean mkDir(RemoteFile rf) throws RemoteException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.mkDir.name())
        .argTypes(RemoteFile.class)
        .addArg(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean mkDir(String str) throws RemoteException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.mkDir.name())
        .argTypes(String.class)
        .addArg(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean zip(RemoteFile out, RemoteFile ... srcs) throws RemoteException {
    if(out == null || srcs == null || srcs.length < 1) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.zip.name())
        .argTypes(RemoteFile.class, RemoteFile[].class)
        .addArg(out).addArg(srcs);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean unzip(RemoteFile src, RemoteFile out) throws RemoteException {
    if(out == null || src == null) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.unzip.name())
        .argTypes(RemoteFile.class, RemoteFile.class)
        .addArg(out).addArg(out);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean zip(String out, String ... srcs) throws RemoteException {
    if(out == null || srcs == null || srcs.length < 1) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.zip.name())
        .argTypes(String.class, String[].class)
        .addArg(out).addArg(srcs);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean unzip(String src, String out) throws RemoteException {
    if(out == null || src == null) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.unzip.name())
        .argTypes(String.class, String.class)
        .addArg(src).addArg(out);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean readFile(IOData data) throws RemoteException {
    if(data == null || data.getRemoteFile() == null
        || data.getPath() == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.readFile.name())
        .argTypes(RemoteFile.class, SocketChannel.class)
        .addArg(data.getRemoteFile()).addArg(null);
    
    if(data.getRemoteFile().getSize() == null)
      data.setRemoteFile(this.getFile(
          data.getRemoteFile()));
    if(data.getRemoteFile() == null) return false;
    data.setMax(data.getRemoteFile()
        .getSize().getSize());
    
    try {
      Path path = Paths.get(data.getPath().toString() 
          + "/" + data.getRemoteFile().getName());
      if(path.getParent() != null
          && !Files.exists(path.getParent()))
        Files.createDirectories(path.getParent());
      if(!Files.exists(path))
        Files.createFile(path);
      data.update(path);
      
      ProtocolConnection conn = rob.invokeRaw(rm);
      FileChannel fch = FileChannel.open(
          path, StandardOpenOption.WRITE);
      
      this.copyChannels(conn, fch, data);
      fch.close();
      conn.close();
      return true;
    } 
    catch(IOException e) {
      throw new RemoteException(e);
    }
  }
  
  
  private void copyChannels(ByteChannel src, ByteChannel dst, IOData data) throws IOException {
    if(src == null || dst == null || data == null)
      return;
    
    System.out.println("* copying channels...");
    ByteBuffer buf = ByteBuffer.allocateDirect(
        FSConstants.BUFFER_SIZE);
    int read = 1;
    while(read > 0) {
      read = src.read(buf);
      data.update(read);
      buf.flip();
      dst.write(buf);
      buf.clear();
    }
  }
  
  
  public boolean write(IOData data) throws RemoteException {
    if(data == null || data.getRemoteFile() == null 
        || data.getPath() == null 
        || data.getStartPos() < 0
        || !Files.exists(data.getPath())) return false;
    
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.write.name())
        .argTypes(SocketChannel.class, RemoteFile.class, long.class)
        .args(null, data.getRemoteFile(), 
            data.getStartPos());
    
    try {
      if(data.getStartPos() >= Files.size(data.getPath()))
        return false;
      
      data.update(data.getPath());
      data.setMax(Files.size(data.getPath()));
      
      FileInputStream fis = new FileInputStream(
          data.getPath().toFile());
      if(data.getStartPos() != 0) 
        fis.skip(data.getStartPos());
      rob.invokeAndSend(rm, fis);
      rob.closeConnection();
      return true;
    }
    catch(IOException e) {
      throw new RemoteException(e);
    }
  }
  
  
  public long getCRC32(RemoteFile rf) throws RemoteException {
    if(rf == null || rf.getPath() == null) 
      return -1;
    RemoteMethod rm = new RemoteMethod()
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getCRC32.name())
        .argTypes(RemoteFile.class)
        .args(rf);
    return (long) rob.invoke(rm);
  }
  
  
  private boolean checkLocalPath(IOData io) {
    if(io == null || io.getPath() == null 
        || io.getRemoteFile() == null)
      return false;
    if(Files.isDirectory(io.getPath()))
      io.setPath(Paths.get(io.getPath().toString(), 
          io.getRemoteFile().getName()));
    
    try {
      if(Files.exists(io.getPath()))
        Files.delete(io.getPath());
      
      if(io.getPath().getParent() != null
          && !Files.exists(io.getPath().getParent()))
        Files.createDirectories(io.getPath().getParent());
      Files.createFile(io.getPath());
    } catch(IOException e) {
      return false;
    }
    return true;
  }
  
  
  public boolean asyncCopyFile(IOData data, int connections) throws RemoteException {
    if(connections < 1) connections = 1;
    if(data == null || !this.checkLocalPath(data))
      return false;
    
    data.setRemoteFile(
        this.getFile(data.getRemoteFile()));
    if(data.getRemoteFile() == null 
        || data.getRemoteFile().getSize() == null)
      return false;
    
    long size = data.getRemoteFile().getSize().getSize();
    long part = (size / connections) + (size % connections);
    data.update(data.getPath());
    data.setMax(size);
    
    ExecutorService exec = Executors.newCachedThreadPool();
    CountDownLatch cdown = new CountDownLatch(connections);
    
    for(int i = 0; i < connections; i++) {
      IOData io = data.clone()
          .setLength(part)
          .setStartPos(part * i);
      NetConnector nc = new NetConnector(
          rob.getNetConnector().getAddress(),
          rob.getNetConnector().getPort());
      exec.submit(new AsyncFileReader(nc, io, cdown));
      //new Thread(new AsyncFileReader(nc, io, cdown)).start();
    }
    
    try { cdown.await(); }
    catch(InterruptedException e) {}
    
    exec.shutdown();
    return true;
  }
  
}
