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

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/02/2017
 */
public class INetDiscovery {

  private final List<INet> nifs;
  
  
  private INetDiscovery() {
    nifs = new ArrayList<>();
  }
  
  
  public List<INet> getInterfaces() {
    return Collections.unmodifiableList(nifs);
  }
  
  
  private INet create(NetworkInterface ni) {
    if(ni == null) return null;
    try {
      return new INetBuilder(ni).build();
    } catch(RuntimeException e) {
      return null;
    }
  }
  
  
  public INetDiscovery discover() throws RuntimeException {
    try {
      Enumeration<NetworkInterface> eis = NetworkInterface.getNetworkInterfaces();
      while(eis.hasMoreElements()) {
        INet in = create(eis.nextElement());
        if(in != null) nifs.add(in);
      }
      return this;
    }
    catch(SocketException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public INet guessMainINet() {
    return nifs.stream()
        .filter(i->i.index() == 2)
        .findFirst().orElse(null);
  }
  
  
  public INet getLoopback() {
    return nifs.stream()
        .filter(INet::isLoopback)
        .findFirst().orElse(null);
  }
  
  
  public static INetDiscovery create() throws RuntimeException {
    return new INetDiscovery().discover();
  }
  
}
