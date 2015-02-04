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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;
import us.pserver.cdr.b64.Base64StringCoder;

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
      TCPCLOSE = "tcpclose",
      HTTPGET = "httpget";
  
  
  private BasicIO bio;
  
  
  public NetLib(BasicIO bio) {
    if(bio == null)
      throw new IllegalArgumentException("Invalid BasicIO: "+ bio);
    this.bio = bio;
  }
  
  
  public Socket httpget(String address, int port) throws FSException {
    return httpget(address, port, null, 0, null);
  }
  
  
  public Socket httpget(String address, int port, String proxyAddr, int proxyPort, String proxyAuth) throws FSException {
    if(address == null || address.trim().isEmpty()
        || port <= 0 || port > 65535) {
      throw new FSException("httpget( "
          + address+ ":"+ port+ " ): Invalid Address/Port");
    }
    
    String proto = "";
    String mainaddr = null;
    String compl = null;
    int start = 0;
    int end = 0;
    
    if(address.contains("://")) {
      start = address.indexOf("://") + 3;
      proto = address.substring(0, start);
    }
    if(address.indexOf("/", start) > 0) {
      end = address.indexOf("/", start);
    } else {
      address += "/";
      end = address.indexOf("/", start);
    }
    
    mainaddr = address.substring(start, end);
    compl = address.substring(end);
    
    Socket sock = null;
    if(proxyAddr != null && !proxyAddr.trim().isEmpty()) {
      sock = tcpconnect(proxyAddr, proxyPort);
    }
    else {
      sock = tcpconnect(mainaddr, port);
    }
    
    String req = "GET " + proto + mainaddr + ":" + String.valueOf(port) + compl + " HTTP/1.0";
    
    tcpwriteln(sock, req);
    tcpwriteln(sock, "User-Agent: Mozilla/5.0");
    tcpwriteln(sock, "Accept: text/html, text/xml, application/octet-stream");
    tcpwriteln(sock, "Accept-Encoding: deflate");
    tcpwriteln(sock, "Connection: close");
    tcpwriteln(sock, "Host: "+ mainaddr+ ":"+ String.valueOf(port));
    if(proxyAuth != null && !proxyAuth.trim().isEmpty()) {
      Base64StringCoder cdr = new Base64StringCoder();
      tcpwriteln(sock, "Proxy-Authorization: Basic "+ cdr.encode(proxyAuth));
    }
    tcpwriteln(sock, "\n\n");
    return sock;
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
  
  
  public void tcplisten(Object server, String func) throws FSException {
    final ServerSocket svr = get(server, ServerSocket.class);
    if(svr == null) return;
    if(func == null || func.trim().isEmpty()) {
      throw new FSException("tcpconnect( "
          + server+ ", >" + func+ "< ): Invalid Callback Function");
    }
    
    try {
      Socket sock = svr.accept();
      ArrayList al = new ArrayList(2);
      al.add(sock);
      al.add(svr);
      bio.callScriptFunction(func, al);
    } 
    catch(FSException | IOException fe) {
      throw new FSException(fe.toString());
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
  
  
  public int tcpwriteln(Object socket, String ln) throws FSException {
    if(ln == null) return 0;
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    try {
      OutputStream out = null;
      if(socket instanceof Socket) {
        out = ((Socket)socket).getOutputStream();
      }
      else {
        throw new FSException("tcpwriteln( >"
            + socket+ "<, "+ ln+ " ): Invalid Argument Type");
      }
      out.write((ln + "\n").getBytes("UTF-8"));
      return 1;
    } 
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public String tcpreadln(Object socket) throws FSException {
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    try {
      InputStream in = null;
      if(socket instanceof Socket) {
        in = ((Socket)socket).getInputStream();
      }
      else {
        throw new FSException("tcpreadln( "
            + socket+ " ): Invalid Argument Type");
      }
      
      byte[] bs = new byte[1];
      StringBuffer sb = new StringBuffer();
      int read = -1;
      while((read = in.read(bs)) > 0) {
        sb.append(new String(bs, 0, read, "UTF-8"));
        if(sb.toString().contains("\n") || read < bs.length)
          break;
      }
      if(sb.toString().contains("\n")) {
        return sb.toString().substring(0, sb.indexOf("\n"));
      }
      return (sb.toString().trim().isEmpty() ? IOLib.EOF : sb.toString());
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int tcpwrite(Object socket, Object bytes) throws FSException {
    if(bytes == null) return 0;
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    if(bytes instanceof FSObject)
      bytes = ((FSObject)bytes).getObject();
    
    if(bytes instanceof Integer) {
      return tcpwrite(socket, (Integer) bytes );
    }
    
    byte[] bs = get(bytes, byte[].class);
    try {
      OutputStream out = null;
      if(socket instanceof Socket) {
        out = ((Socket)socket).getOutputStream();
      }
      else {
        throw new FSException("tcpwrite( >"
            + socket+ "<, "+ bytes+ " ): Invalid Argument Type");
      }
      
      out.write((byte[]) bytes);
      out.flush();
      return 1;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int tcpwrite(Object socket, int bt) throws FSException {
    if(socket == null || bt == -1) 
      throw new FSException("tcpwrite( "
          + socket+ ", "+ bt+ " ): Invalid Arguments");
    
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    try {
      OutputStream out = null;
      if(socket instanceof Socket) {
        out = ((Socket)socket).getOutputStream();
      }
      else {
        throw new FSException("tcpwrite( >"
            + socket+ "<, "+ bt+ " ): Invalid Argument Type");
      }
      
      out.write(bt);
      out.flush();
      return 1;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public byte[] tcpread(Object socket, int size) throws FSException {
    if(socket == null || size < 1) 
      throw new FSException("tcpread( "+ socket
          + ", "+ String.valueOf(size)+ " ): Invalid Arguments");
    
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    try {
      InputStream in = null;
      if(socket instanceof Socket) {
        in = ((Socket)socket).getInputStream();
      }
      else {
        throw new FSException("tcpread( >"+ socket
            + "<, "+ String.valueOf(size)+ " ): Invalid Argument Type");
      }
      
      byte[] bs = new byte[size];
      int read = in.read(bs);
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
    if(socket == null) 
      throw new FSException(
          "tcpread( "+ socket+ " ): Invalid Arguments");
    
    if(socket instanceof FSObject) {
      socket = ((FSObject)socket).getObject();
    }
    
    try {
      InputStream in = null;
      if(socket instanceof Socket) {
        in = ((Socket)socket).getInputStream();
      }
      else {
        throw new FSException("tcpread( "+ socket
            + " ): Invalid Argument Type");
      }
      
      return in.read();
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
      case HTTPGET:
        FUtils.checkLen(al, 2);
        if(al.size() == 2)
          return httpget(FUtils.str(al, 0), FUtils._int(al, 1));
        else {
          FUtils.checkLen(al, 4);
          String proxyAuth = null;
          if(al.size() > 4)
            proxyAuth = FUtils.str(al, 4);
          return httpget(FUtils.str(al, 0), FUtils._int(al, 1), 
              FUtils.str(al, 2), FUtils._int(al, 3), proxyAuth);
        }
      default:
        throw new FSUnsupportedException();
    }
    return null;
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(HTTPGET, this);
    ext.addFunctionExtension(TCPCLOSE, this);
    ext.addFunctionExtension(TCPCONNECT, this);
    ext.addFunctionExtension(TCPLISTEN, this);
    ext.addFunctionExtension(TCPREAD, this);
    ext.addFunctionExtension(TCPREADLN, this);
    ext.addFunctionExtension(TCPWRITE, this);
    ext.addFunctionExtension(TCPWRITELN, this);
  }

  
  public static void main(String[] args) throws FSException {
    NetLib nlib = new NetLib(new BasicIO());
    //nlib.tcplisten("0.0.0.0", 22022, "sayhello");
    
    Object conn = nlib.httpget("http://www.google.com", 80, "localhost", 9000, null);
    String line = nlib.tcpreadln(conn);
    System.out.println("--------------");
    while(!line.equals(IOLib.EOF)) {
      System.out.println(line);
      line = nlib.tcpreadln(conn);
    }
    System.out.println("--------------");
    nlib.tcpclose(conn);
    /**/
  }
  
}
