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
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.config.ServerConfig;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class MicronConfigImpl implements MicronConfig {
  
  private final ServerConfig server;
  
  private final SecurityConfig security;
  
  private final IgniteConfig ignite;
  
  
  public MicronConfigImpl(ServerConfig server,  SecurityConfig security, IgniteConfig ignite) {
    this.server = Match.notNull(server).getOrFail("Bad null ServerConfig");
    this.security = Match.notNull(security).getOrFail("Bad null SecurityConfig");
    this.ignite = Match.notNull(ignite).getOrFail("Bad null IgniteConfig");
  }
  

  @Override
  public ServerConfig getServerConfig() {
    return server;
  }


  @Override
  public SecurityConfig getSecurityConfig() {
    return security;
  }


  @Override
  public IgniteConfig getIgniteConfig() {
    return ignite;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.server);
    hash = 67 * hash + Objects.hashCode(this.security);
    hash = 67 * hash + Objects.hashCode(this.ignite);
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
    final MicronConfigImpl other = (MicronConfigImpl) obj;
    if(!Objects.equals(this.server, other.server)) {
      return false;
    }
    if(!Objects.equals(this.security, other.security)) {
      return false;
    }
    if(!Objects.equals(this.ignite, other.ignite)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "MicronConfig{" + "server=" + server + ", security=" + security + ", ignite=" + ignite + '}';
  }
  
}
