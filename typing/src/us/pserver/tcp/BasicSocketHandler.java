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

package us.pserver.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/07/2015
 */
public class BasicSocketHandler implements SocketHandler {

  private Socket sock;
  
  
  public BasicSocketHandler() {
    sock = null;
  }
  
  
  public BasicSocketHandler(Socket sock) {
    this.sock = sock;
  }


  @Override
  public Socket getSocket() {
    return sock;
  }


  public BasicSocketHandler setSocket(Socket sock) {
    this.sock = sock;
    return this;
  }
  
  
  protected int search(byte[] buf, int off, int len, int b) {
    if(buf == null || buf.length < 1 || off < 0 || off+len > buf.length)
      return -1;
    for(int i = off; i < off+len; i++) {
      if(b == buf[i]) return i;
    }
    return -1;
  }
  
  
  @Override
  public void send(byte[] buf, int off, int len)  throws IOException {
    if(buf == null || buf.length < 1 || off < 0 || off+len > buf.length)
      return;
    if(sock == null) throw new IllegalStateException("Invalid Socket: "+ sock);
    sock.getOutputStream().write(buf, off, len);
    if(search(buf, off, len, 10) < 0)
      sock.getOutputStream().write(10);
    sock.getOutputStream().flush();
  }
  
  
  @Override
  public void send(String str) throws IOException {
    if(str == null) return;
    if(sock == null) throw new IllegalStateException("Invalid Socket: "+ sock);
    byte[] buf = str.getBytes(Charset.forName("UTF-8"));
    send(buf, 0, buf.length);
  }
  
  
  @Override
  public int receive(byte[] buf) throws IOException {
    if(buf == null || buf.length < 1)
      return -1;
    if(sock == null) throw new IllegalStateException("Invalid Socket: "+ sock);
    return sock.getInputStream().read(buf);
  }
  
  
  public byte[] receiveAll() throws IOException {
    if(sock == null) throw new IllegalStateException("Invalid Socket: "+ sock);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    InputStream in = sock.getInputStream();
    byte[] buf = new byte[1024];
    int read = in.read(buf);
    while(read > 0) {
      bos.write(buf, 0, read);
      if(search(bos.toByteArray(), 0, bos.size(), 10) >= 0)
        break;
      read = in.read(buf);
    }
    return bos.toByteArray();
  }
  
  
  @Override
  public String receive() throws IOException {
    if(sock == null) throw new IllegalStateException("Invalid Socket: "+ sock);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    InputStream in = sock.getInputStream();
    byte[] buf = new byte[1024];
    int read = in.read(buf);
    while(read > 0) {
      bos.write(buf, 0, read);
      if(search(bos.toByteArray(), 0, bos.size(), 10) >= 0)
        break;
      read = in.read(buf);
    }
    return bos.toString("UTF-8");
  }
}
