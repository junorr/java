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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.rob.MethodChain;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/11/2013
 */
public class RemoteFileSystem {
  
  private RemoteObject rob;
  
  private Credentials cred;
  
  
  public RemoteFileSystem(NetConnector nc, Credentials cr) {
    nullarg(NetConnector.class, nc);
    rob = new RemoteObject(nc, 
        DefaultFactoryProvider
            .getHttpRequestChannelFactory());
    cred = cr;
    if(!this.checkConn())
      throw new IllegalArgumentException(
          "Server do not respond properly: "+ nc);
  }
  
  
  public RemoteFileSystem(String host, int port, Credentials cr) {
    this(new NetConnector(host, port), cr);
  }
  
  
  private boolean checkConn() {
    MethodChain chain = new MethodChain();
    chain.add(Tokens.NetworkServer.name(), 
        Tokens.container.name())
        .credentials(cred);
    chain.add(Tokens.contains.name())
        .types(String.class)
        .params(Tokens.LocalFileSystem.name());
    
    try { rob.invoke(chain); }
    catch(Exception e) { 
      e.printStackTrace();
      return false; 
    }
    return true;
  }
  
  
  public void closeConnection() {
    rob.close();
  }
  
  
  public HostInfo getHostInfo() throws MethodInvocationException {
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getHostInfo.name());
    return (HostInfo) rob.invoke(rm);
  }
  
  
  public RFile getFile(RFile rf) throws MethodInvocationException {
    if(rf == null || rf.getPath() == null)
      return rf;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getFile.name())
        .types(RFile.class)
        .params(rf);
    return (RFile) rob.invoke(rm);
  }
  
  
  public RFile getFile(String path) throws MethodInvocationException {
    if(path == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getFile.name())
        .types(String.class)
        .params(path);
    return (RFile) rob.invoke(rm);
  }
  
  
  public RFile current() throws MethodInvocationException {
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.current.name());
    return (RFile) rob.invoke(rm);
  }
  
  
  public List<RFile> ls() throws MethodInvocationException {
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name());
    return (List<RFile>) rob.invoke(rm);
  }
  
  
  public List<RFile> ls(RFile rf) throws MethodInvocationException {
    if(rf == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name())
        .types(RFile.class)
        .params(rf);
    return (List<RFile>) rob.invoke(rm);
  }
  
  
  public List<RFile> ls(String str) throws MethodInvocationException {
    if(str == null) return null;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.ls.name())
        .types(String.class)
        .params(str);
    return (List<RFile>) rob.invoke(rm);
  }
  
  
  public boolean cd(RFile rf) throws MethodInvocationException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.cd.name())
        .types(RFile.class)
        .params(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean cd(String str) throws MethodInvocationException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.cd.name())
        .types(String.class)
        .params(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rm(RFile rf) throws MethodInvocationException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rm.name())
        .types(RFile.class)
        .params(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rmdir(RFile rf) throws MethodInvocationException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rmdir.name())
        .types(RFile.class, boolean.class)
        .params(rf, true);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rm(String str) throws MethodInvocationException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rm.name())
        .types(String.class)
        .params(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean exec(String ... cmds) throws MethodInvocationException {
    if(cmds == null || cmds.length == 0)
      throw new MethodInvocationException("Invalid command array ["+ cmds+ "]");
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.exec.name())
        .types(String[].class)
        .params(cmds);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean osrm(String str) throws MethodInvocationException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.osrm.name())
        .types(String.class)
        .params(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean osrm(RFile rf) throws MethodInvocationException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.osrm.name())
        .types(RFile.class)
        .params(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean rmdir(String str) throws MethodInvocationException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.rmdir.name())
        .types(String.class, boolean.class)
        .params(str, true);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean mkdir(RFile rf) throws MethodInvocationException {
    if(rf == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.mkdir.name())
        .types(RFile.class)
        .params(rf);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean mkdir(String str) throws MethodInvocationException {
    if(str == null) return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.mkdir.name())
        .types(String.class)
        .params(str);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean zip(RFile out, RFile ... srcs) throws MethodInvocationException {
    if(out == null || srcs == null || srcs.length < 1) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.zip.name())
        .types(RFile.class, RFile[].class)
        .params(out, srcs);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean unzip(RFile src, RFile out) throws MethodInvocationException {
    if(out == null || src == null) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.unzip.name())
        .types(RFile.class, RFile.class)
        .params(src, out);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean zip(String out, String ... srcs) throws MethodInvocationException {
    if(out == null || srcs == null || srcs.length < 1) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.zip.name())
        .types(String.class, String[].class)
        .params(out, srcs);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean unzip(String src, String out) throws MethodInvocationException {
    if(out == null || src == null) 
      return false;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.unzip.name())
        .types(String.class, String.class)
        .params(src, out);
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean readFile(IOData data) throws IOException, MethodInvocationException {
    nullarg(IOData.class, data);
    nullarg(RFile.class, data.getRFile());
    nullarg(Path.class, data.getPath());
    
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.readFile.name())
        .types(IOData.class)
        .params(data.getWriteVersion());
    
    RFile rf = data.getRFile();
    if(rf.getSize() == null)
      data.setRFile(this.getFile(
          data.getRFile()));
    if(data.getRFile() == null) 
      throw new FileNotFoundException("No such RFile ["+ rf+ "]");
    
    long crc = this.getCRC32(data.getRFile());
    data.setMax(data.getRFile()
        .getSize().size());
    InputStream is = (InputStream) rob.invoke(rm);
    OutputStream os = IO.os(data.getPath());
    data.update(data.getRFile().toPath());
    FSConst.transfer(is, os, data);
    IO.cl(is, os);
    long crc2 = FSConst.getCRC32(data.getPath());
    if(crc != crc2)
      throw new IOException("Currupted data readed [CRC32: "+ crc+ " != "+ crc2+ "]");
    return true;
  }
  
  
  public boolean write(InputStream is, IOData data) throws IOException, MethodInvocationException {
    nullarg(InputStream.class, is);
    nullarg(IOData.class, data);
    nullarg(RFile.class, data.getRFile());
    
    ProgressInputStream pis = new ProgressInputStream(is, data);
    RemoteMethod rm = new RemoteMethod(
        Tokens.LocalFileSystem.name(),
        Tokens.write.name())
        .credentials(cred)
        .types(InputStream.class, IOData.class)
        .params(pis, data.getWriteVersion());
    return (boolean) rob.invoke(rm);
  }
  
  
  public boolean writeFile(IOData data) throws IOException, MethodInvocationException {
    nullarg(IOData.class, data);
    nullarg(RFile.class, data.getRFile());
    nullarg(Path.class, data.getPath());
    
    long crc = FSConst.getCRC32(data.getPath());
    data.update(data.getPath());
    data.setMax(Files.size(data.getPath()));
    if(!write(IO.is(data.getPath()), data))
      return false;
    long crc2 = this.getCRC32(data.getRFile());
    if(crc != crc2)
      throw new IOException("Currupted data writed [CRC32: "+ crc+ " != "+ crc2+ "]");
    return true;
  }
  
  
  public long getCRC32(RFile rf) throws MethodInvocationException {
    if(rf == null || rf.getPath() == null) 
      return -1;
    RemoteMethod rm = new RemoteMethod()
        .credentials(cred)
        .forObject(Tokens.LocalFileSystem.name())
        .method(Tokens.getCRC32.name())
        .types(RFile.class)
        .params(rf);
    return (long) rob.invoke(rm);
  }
  
}
