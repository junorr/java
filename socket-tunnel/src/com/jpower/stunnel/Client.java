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
package com.jpower.stunnel;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class Client {
  
  private Configuration config;
  
  private Socket socket;
  
  
  public Client( Configuration conf ) {
    if(conf == null)
      throw new IllegalArgumentException(
          "Invalid Configuration: "+ conf);
    
    config = conf;
  }
  
  
  private void init() throws IOException {
    if(config.getProxyAddress() != null 
        && !config.getProxyAddress().isEmpty())
      socket = new Socket(config.getProxyAddress(), config.getProxyPort());
    else
      socket = new Socket(config.getRemoteAddress(), config.getPort());
  }


  public Configuration getConfig() {
    return config;
  }


  public Client setConfig( Configuration config ) {
    if(config != null)
      this.config = config;
    return this;
  }
  
  
  public byte[] receive( int length ) throws IOException {
    if(length <= 0) 
      throw new IllegalArgumentException(
          "Invalid length: "+ length);
    
    byte[] buf = new byte[length];
    socket.getInputStream().read(buf);
    return buf;
  }
  
  
  public DynamicBuffer receiveBlocking( boolean partial ) throws IOException {
    byte[] pbuf = new byte[256];
    int read = 0;
    DynamicBuffer buffer = new DynamicBuffer();
    InputStream is = socket.getInputStream();
    do {
      read = is.read(pbuf);
      buffer.write(pbuf, 0, read);
      if(partial && read != pbuf.length)
        break;
    } while(read > 0);
    
    return buffer;
  }
  
  
  public void send( byte[] data, int start, int length ) throws IOException {
    if(data == null || data.length == 0
        || start < 0 
        || start > data.length
        || length < 1 
        || length > (data.length - start))
      return;
    
    socket.getOutputStream().write(data, start, length);
    socket.getOutputStream().flush();
  }
  
  
  public void send( byte[] data ) throws IOException {
    this.send(data, 0, data.length);
  }
  
  
  public void send( DynamicBuffer buffer ) throws IOException {
    this.send(buffer.toArray());
  }
  
  
  public Client connect() throws IOException {
    this.init();
    return this;
  }
  
  
  public Client close() throws IOException {
    socket.shutdownInput();
    socket.shutdownOutput();
    socket.close();
    return this;
  }
  
}
