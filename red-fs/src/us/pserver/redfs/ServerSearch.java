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

import com.jpower.date.DateDiff;
import com.jpower.date.SimpleDate;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Credentials;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/12/2013
 */
public class ServerSearch {

  public static final int DEFAULT_PORT = 9099;
  
  public static final int DEFAULT_SEARCH_TIMEOUT = 120;
  
  private String baseAddress;
  
  private int port;
  
  private int timeout;
  
  
  public ServerSearch() {
    this(getBaseIPAddress());
  }
  
  
  public ServerSearch(String baseAddress) {
    if(baseAddress == null || !baseAddress.endsWith("."))
      throw new IllegalArgumentException(
          "Address do not follow the base format: 0.0.0.");
    port = DEFAULT_PORT;
    this.baseAddress = baseAddress;
    timeout = DEFAULT_SEARCH_TIMEOUT;
  }


  public int getConnectionTimeout() {
    return timeout;
  }


  public ServerSearch setConnectionTimeout(int searchTimeout) {
    this.timeout = searchTimeout;
    return this;
  }


  public String getBaseAddress() {
    return baseAddress;
  }


  public ServerSearch setBaseAddress(String baseAddress) {
    this.baseAddress = baseAddress;
    return this;
  }


  public int getPort() {
    return port;
  }


  public ServerSearch setPort(int port) {
    this.port = port;
    return this;
  }
  
  
  public static String getBaseIPAddress() {
    HostInfo hi = HostInfo.getHostInfo();
    if(hi.getAddress() == null) return null;
    String address = hi.getAddress();
    int i = address.lastIndexOf(".");
    return address.substring(0, i+1);
  }
  
  
  public List<HostInfo> search(ProgressListener p, Credentials cred) {
    if(baseAddress == null) return null;
    List<HostInfo> lh = new LinkedList<>();
    if(p != null) p.setMax(254);
    for(int i = 1; i < 255; i++) {
      String addr = baseAddress + String.valueOf(i);
      if(p != null) {
        p.update(Paths.get(addr));
        p.update(1);
      }
      try(Socket sock = new Socket();) {
        sock.connect(new InetSocketAddress(addr, port), timeout);
        if(!sock.isConnected()) continue;
        NetConnector net = new NetConnector(addr, port);
        RemoteFileSystem rf = new RemoteFileSystem(net, cred);
        lh.add(rf.getHostInfo());
        rf.closeConnection();
      } catch(Exception e) {}
    }
    return lh;
  }
  
  
  public static void main(String[] args) throws UnknownHostException, SocketException {
    SimpleDate start = new SimpleDate();
    System.out.println(start.format(SimpleDate.HHMMSSNNN));
    Credentials cred = new Credentials("juno", new StringBuffer("32132155"));
    List<HostInfo> list = new ServerSearch().search(new SimpleListener(), cred);
    SimpleDate end = new SimpleDate();
    System.out.println(end.format(SimpleDate.HHMMSSNNN));
    System.out.println(new DateDiff(start, end));
    for(HostInfo h : list) System.out.println(h);
  }
  
}
