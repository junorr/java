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

package us.pserver.ip;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/02/2017
 */
public class INetBuilder {
  
  private final NetworkInterface nif;
  
  
  private INetBuilder() {
    nif = null;
  }
  

  public INetBuilder(NetworkInterface nif) {
    if(nif == null) {
      throw new IllegalArgumentException("Bad Null NetworkInterface");
    }
    this.nif = nif;
  }
  
  
  public NetworkInterface getNetworkInterface() {
    return nif;
  }
  
  
  public INet build() throws RuntimeException {
    InetAddress ipv4 = null;
    InetAddress ipv6 = null;
    Enumeration<InetAddress> ads = nif.getInetAddresses();
    while(ads.hasMoreElements()) {
      InetAddress adr = ads.nextElement();
      if(ipv4 == null && Inet4Address.class.isAssignableFrom(adr.getClass())) {
        ipv4 = adr;
      }
      if(ipv6 == null && Inet6Address.class.isAssignableFrom(adr.getClass())) {
        ipv6 = adr;
      }
    }
    try {
      return INet.of(nif.getName(), nif.getIndex(), nif.isLoopback(), ipv4, ipv6);
    }
    catch(SocketException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
}
