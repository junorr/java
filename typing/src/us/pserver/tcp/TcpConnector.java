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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/07/2015
 */
public class TcpConnector extends BasicSocketHandler {

  private String address;
  
  private int port;
  
  
  public TcpConnector() {
    address = null;
    port = 0;
  }
  
  
  public TcpConnector(String address, int port) {
    this.address = address;
    this.port = port;
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public int getPort() {
    return port;
  }


  public TcpConnector setPort(int port) {
    if(port > 0) this.port = port;
    return this;
  }
  
  
  public SocketAddress getSocketAddress() {
    if(address == null || port < 1)
      throw new IllegalStateException("Invalid "+ toString());
    return new InetSocketAddress(address, port);
  }
  
  
  public Socket connect() throws IOException {
    setSocket(new Socket());
    getSocket().setReuseAddress(false);
    getSocket().connect(getSocketAddress());
    return getSocket();
  }
  
  
  public void disconnect() throws IOException {
    if(getSocket() == null) return;
    getSocket().shutdownOutput();
    getSocket().shutdownInput();
    getSocket().close();
    setSocket(null);
  }
  
  
  public boolean isConnected() {
    return getSocket() != null;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.address);
    hash = 97 * hash + this.port;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcpConnector other = (TcpConnector) obj;
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (this.port != other.port) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "TcpConnector{" + "address=" + address + ", port=" + port + ", connected="+ isConnected()+  '}';
  }
  
}
