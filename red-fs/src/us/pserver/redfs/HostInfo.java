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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/12/2013
 */
public class HostInfo {

  private String address;
  
  private String name;
  
  
  public HostInfo() {
    address = null;
    name = null;
  }
  
  
  public HostInfo(String name, String address) {
    this.address = address;
    this.name = name;
  }


  public String getAddress() {
    return address;
  }


  public HostInfo setAddress(String address) {
    this.address = address;
    return this;
  }


  public String getName() {
    return name;
  }


  public HostInfo setName(String name) {
    this.name = name;
    return this;
  }
  
  
  public static HostInfo getHostInfo() {
    HostInfo hi = new HostInfo();
    InetAddress addr = getLocalHost();
    InetAddress name = null;
    try {
      name = InetAddress.getLocalHost();
    } catch(UnknownHostException e) {}
    if(addr == null || name == null) return hi;
    return hi.setName(name.getHostName()) 
        .setAddress(addr.getHostAddress());
  }


  public static InetAddress getLocalHost() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while(nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        if(ni.isLoopback()) continue;
        Enumeration<InetAddress> ias = ni.getInetAddresses();
        while(ias.hasMoreElements()) {
          InetAddress ia = ias.nextElement();
          if(!ia.getHostAddress().equals("127.0.0.1")
              && ia.getHostAddress().contains(".")
              && !ia.getHostAddress().contains(":")) {
            return ia;
          }
        }
      }
    }
    catch(SocketException e) {}
    return null;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.address);
    hash = 89 * hash + Objects.hashCode(this.name);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final HostInfo other = (HostInfo) obj;
    if (!Objects.equals(this.address, other.address))
      return false;
    if (!Objects.equals(this.name, other.name))
      return false;
    return true;
  }


  @Override
  public String toString() {
    return "HostInfo{ " + "address=" + address + ", name=" + name + " }";
  }
  
  
  public static void main(String[] args) throws UnknownHostException {
    System.out.println(getHostInfo());
  }
  
}
