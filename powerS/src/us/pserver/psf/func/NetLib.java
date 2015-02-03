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

package us.pserver.psf.func;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public class NetLib implements FSFunctionExtension {
  
  public static final byte[] BYTES_EOF = {69, 79, 70};

  public static final String
      TCPCONNECT = "tcpconnect",
      TCPLISTEN = "tcplisten",
      TCPREADLN = "tcpreadln",
      TCPWRITELN = "tcpwriteln",
      TCPREAD = "tcpread",
      TCPWRITE = "tcpwrite",
      TCPCLOSE = "tcpclose";
  
  
  private BasicIO bio;
  
  
  public NetLib(BasicIO bio) {
    if(bio == null)
      throw new IllegalArgumentException("Invalid BasicIO: "+ bio);
    this.bio = bio;
  }
  
  
  public Socket tcpconnect(String address, int port) throws FSException {
    if(address == null || address.trim().isEmpty()
        || port <= 0 || port > 65535) {
      throw new FSException("tcpconnect( "
          + address+ ":"+ port+ " ): Invalid Address/Port");
    }
    
    try {
      return new Socket(address, port);
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void tcplisten(String address, int port, final String func) throws FSException {
    if(address == null || address.trim().isEmpty()
        || port <= 0 || port > 65535) {
      throw new FSException("tcpconnect( >"
          + address+ ":"+ port+ "<, " + func+ " ): Invalid Address/Port");
    }
    if(func == null || func.trim().isEmpty()) {
      throw new FSException("tcpconnect( "
          + address+ ":"+ port+ ", >" + func+ "< ): Invalid Callback Function");
    }
    
    
    try {
      ServerSocket svr = new ServerSocket();
      svr.bind(new InetSocketAddress(address, port));
      tcplisten(svr, func);
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void tcplisten(Object server, final String func) throws FSException {
    final ServerSocket svr = get(server, ServerSocket.class);
    if(svr == null) return;
    if(func == null || func.trim().isEmpty()) {
      throw new FSException("tcpconnect( "
          + server+ ", >" + func+ "< ): Invalid Callback Function");
    }
    
    try {
      final Socket sock = svr.accept();
      
      Runnable run = new Runnable() {
        public void run() {
          try {
            ArrayList al = new ArrayList(2);
            al.add(sock);
            al.add(svr);
            bio.callScriptFunction(func, al);
          } catch(FSException | IOException fe) {}
        }
      };
      new Thread(run).start();
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public <T> T get(Object obj, Class<T> cls) throws FSException {
    if(obj == null)
      throw new FSException("Invalid Socket Object: "+ obj);
    if(cls == null) return null;
    
    if(obj instanceof FSObject)
      obj = ((FSObject) obj).getObject();
    
    if(!cls.isAssignableFrom(obj.getClass()))
      throw new FSException("Argument is not an instance of "+ cls);
    
    return cls.cast(obj);
  }
  
  
  public int tcpwriteln(Object socket, String cont) throws FSException {
    Socket sock = get(socket, Socket.class);
    
    try {
      PrintStream ps = new PrintStream(
          sock.getOutputStream());
      ps.println(cont);
      ps.flush();
      return 1;
    } 
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public String tcpreadln(Object socket) throws FSException {
    Socket sock = get(socket, Socket.class);
    
    try {
      BufferedReader br = new BufferedReader(
          new InputStreamReader(sock.getInputStream()));
      return br.readLine();
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int tcpwrite(Object socket, Object bytes) throws FSException {
    Socket sock = get(socket, Socket.class);
    if(sock == null || bytes == null) 
      throw new FSException("tcpwrite( "
          + socket+ ", "+ bytes+ " ): Invalid Arguments");
    
    if(bytes instanceof FSObject)
      bytes = ((FSObject)bytes).getObject();
    
    if(bytes instanceof Integer) {
      return tcpwrite(socket, (Integer) bytes );
    }
    
    byte[] bs = get(bytes, byte[].class);
    try {
      sock.getOutputStream().write((byte[]) bytes);
      sock.getOutputStream().flush();
      return 1;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int tcpwrite(Object socket, int bt) throws FSException {
    Socket sock = get(socket, Socket.class);
    if(sock == null || bt == -1) 
      throw new FSException("tcpwrite( "
          + socket+ ", "+ bt+ " ): Invalid Arguments");
    
    try {
      sock.getOutputStream().write(bt);
      sock.getOutputStream().flush();
      return 1;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public byte[] tcpread(Object socket, int size) throws FSException {
    Socket sock = get(socket, Socket.class);
    if(sock == null || size < 1)
      throw new FSException("tcpreadbytes( "
          + socket+ ", "+ size+ " ): Invalid Arguments");
    
    byte[] bs = new byte[size];
    try {
      int read = sock.getInputStream().read(bs);
      if(read <= 0) return BYTES_EOF;
      byte[] bt = new byte[read];
      System.arraycopy(bs, 0, bt, 0, read);
      return bt;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int tcpread(Object socket) throws FSException {
    Socket sock = get(socket, Socket.class);
    if(sock == null)
      throw new FSException("tcpreadbytes( "
          + socket+ " ): Invalid Socket");
    
    try {
      return sock.getInputStream().read();
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void tcpclose(Object obj) throws FSException {
    if(obj == null) return;
    if(obj instanceof FSObject)
      obj = ((FSObject)obj).getObject();
    
    try {
      if(obj instanceof Socket) {
        Socket sock = (Socket) obj;
        sock.shutdownOutput();
        sock.shutdownInput();
        sock.close();
      }
      else if(obj instanceof ServerSocket) {
        ServerSocket svr = (ServerSocket) obj;
        svr.close();
      }
    }
    catch(IOException e) {}
  }
  
  
  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case TCPCLOSE:
        FUtils.checkLen(al, 1);
        tcpclose(al.get(0));
        break;
      case TCPCONNECT:
        FUtils.checkLen(al, 2);
        return tcpconnect(FUtils.str(al, 0), FUtils._int(al, 1));
      case TCPLISTEN:
        FUtils.checkLen(al, 2);
        if(al.size() == 2)
          tcplisten(al.get(0), FUtils.str(al, 1));
        else if(al.size() > 2)
          tcplisten(FUtils.str(al, 0), FUtils._int(al, 1), FUtils.str(al, 2));
        break;
      case TCPREAD:
        FUtils.checkLen(al, 1);
        if(al.size() == 1)
          return tcpread(al.get(0));
        else
          return tcpread(al.get(0), FUtils._int(al, 1));
      case TCPREADLN:
        FUtils.checkLen(al, 1);
        return tcpreadln(al.get(0));
      case TCPWRITE:
        FUtils.checkLen(al, 2);
        return tcpwrite(al.get(0), al.get(1));
      case TCPWRITELN:
        FUtils.checkLen(al, 2);
        return tcpwriteln(al.get(0), FUtils.str(al, 1));
      default:
        throw new FSUnsupportedException();
    }
    return null;
  }
  
}
