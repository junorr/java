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

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import us.pserver.micron.config.CacheConfig;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.ServerConfig;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class IgniteConfigImpl implements IgniteConfig {
  
  private final ServerConfig ignite;
  
  private final Optional<Path> storage;
  
  private final long joinTimeout;
  
  private final Set<CacheConfig> caches;


  public IgniteConfigImpl(ServerConfig ignite, Optional<Path> storage, long joinTimeout, Set<CacheConfig> caches) {
    this.ignite = Match.notNull(ignite).getOrFail("Bad null ignite ServerConfig");
    this.storage = Match.notNull(storage).getOrFail("Bad null storage Optional<Path>");
    this.joinTimeout = joinTimeout;
    this.caches = Match.notNull(caches).getOrFail("Bad null caches Set<CacheConfig>");
  }
  
  
  @Override
  public ServerConfig getIgniteServerConfig() {
    return ignite;
  }


  @Override
  public Optional<Path> getStorage() {
    return storage;
  }


  @Override
  public long getJoinTimeout() {
    return joinTimeout;
  }


  @Override
  public Set<CacheConfig> getCacheConfigSet() {
    return caches;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.ignite);
    hash = 37 * hash + Objects.hashCode(this.storage);
    hash = 37 * hash + (int) (this.joinTimeout ^ (this.joinTimeout >>> 32));
    hash = 37 * hash + Objects.hashCode(this.caches);
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
    final IgniteConfigImpl other = (IgniteConfigImpl) obj;
    if(this.joinTimeout != other.joinTimeout) {
      return false;
    }
    if(!Objects.equals(this.ignite, other.ignite)) {
      return false;
    }
    if(!Objects.equals(this.storage, other.storage)) {
      return false;
    }
    if(!Objects.equals(this.caches, other.caches)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "IgniteConfig{" + "ignite=" + ignite + ", storage=" + storage + ", joinTimeout=" + joinTimeout + ", caches=" + caches + '}';
  }

}
