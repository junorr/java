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

package us.pserver.micron.config.impl;

import java.util.Objects;
import us.pserver.micron.config.ServerConfig;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class ServerConfigImpl implements ServerConfig {
  
  private final String address;
  
  private final int port;
  
  public ServerConfigImpl(String addr, int port) {
    this.address = Match.notNull(addr).getOrFail("Bad null server address");
    this.port = Match.notBetweenExclusive(port, 1024, 65535).getOrFail("Bad port: %d (1024 < port < 65535)", port);
  }

  @Override
  public String getAddress() {
    return address;
  }


  @Override
  public int getPort() {
    return port;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.address);
    hash = 71 * hash + this.port;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final ServerConfigImpl other = (ServerConfigImpl) obj;
    if(this.port != other.port) {
      return false;
    }
    if(!Objects.equals(this.address, other.address)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "ServerConfig{" + "address=" + address + ", port=" + port + '}';
  }

}
