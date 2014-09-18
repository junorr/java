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
package com.jpower.httpsocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public abstract class AbstractConnectionHandler 
implements ExtConnectionHandler {
  
  protected Socket clientSocket;
  
  protected boolean closeSocketOnFinished;
  
  
  protected AbstractConnectionHandler() {
    clientSocket = null;
    closeSocketOnFinished = true;
  }
  
  
  protected AbstractConnectionHandler(Socket s) {
    clientSocket = s;
  }
  
  
  protected void testProperties() {
    if(clientSocket == null)
      throw new IllegalStateException("Invalid Socket: ["+ clientSocket+ "]");
    else if(!clientSocket.isConnected())
      throw new IllegalStateException("Unconnected Socket: ["+ clientSocket+ "]");
  }
  
  
  public boolean isCloseSocketOnFinished() {
    return closeSocketOnFinished;
  }
  
  
  public ExtConnectionHandler setCloseSocketOnFinished(boolean b) {
    closeSocketOnFinished = b;
    return this;
  }
  
  
  @Override
  public Socket getSocket() {
    return clientSocket;
  }
  
  
  @Override
  public ConnectionHandler setSocket(Socket s) {
    clientSocket = s;
    return this;
  }
  
  
  public String getClientAddress() {
    if(clientSocket != null)
      return clientSocket.getInetAddress().getHostAddress();
    return null;
  }
  
  
  protected StringBuilder readContent(InputStream is) {
    try {
      
      StringBuilder content = new StringBuilder();
      int buf_size = 1024;
      byte[] buf = new byte[buf_size];
      int read = 0;
      while((read = is.read(buf)) >= 0) {
        content.append(new String(buf, 0, read));
        if(read != buf_size) break;
      }
      return content;
      
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public void writeContent(StringBuilder content, OutputStream os) {
    try {
      
      os.write(content.toString().getBytes());
      os.flush();
      
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public OutputStream getOutput(Socket s) {
    try {
      return s.getOutputStream();
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public InputStream getInput(Socket s) {
    try {
      return s.getInputStream();
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public void close(Socket s) {
    try {
      s.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void shutdownInput(Socket s) {
    try {
      s.shutdownInput();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void shutdownOutput(Socket s) {
    try {
      s.shutdownOutput();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  @Override
  public abstract void handle(Socket s);
  
  @Override
  public abstract void handle() throws IllegalStateException;
  
}
