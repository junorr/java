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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import us.pserver.micron.config.CacheConfig;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.config.ServerConfig;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class IgniteConfigBuilderImpl implements IgniteConfig.IgniteConfigBuilder {
  
  private MicronConfig.MicronConfigBuilder micron;
  
  private ServerConfig server;
  
  private Optional<Path> storage;
  
  private long joinTimeout;
  
  private Set<CacheConfig> caches;
  
  
  public IgniteConfigBuilderImpl(MicronConfig.MicronConfigBuilder micron) {
    this.micron = micron;
    server = new ServerConfigImpl("localhost", 5555);
    storage = Optional.empty();
    joinTimeout = 0;
    caches = new HashSet();
  }
  
  
  public IgniteConfigBuilderImpl() {
    this(null);
  }
  

  @Override
  public ServerConfig getIgniteServerConfig() {
    return server;
  }


  @Override
  public Optional<Path> getStorage() {
    return storage;
  }


  @Override
  public long getJoinTimeout() {
    return this.joinTimeout;
  }


  @Override
  public Set<CacheConfig> getCacheConfigSet() {
    return this.caches;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder setIgniteServerConfig(String addr, int port) {
    this.server = new ServerConfigImpl(addr, port);
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder setIgniteServerConfig(ServerConfig cfg) {
    this.server = cfg;
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder setStorage(Optional<Path> storage) {
    this.storage = storage;
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder setJoinTimeout(long timeout) {
    this.joinTimeout = timeout;
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder clearCacheConfigSet() {
    caches.clear();
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder add(CacheConfig cfg) {
    caches.add(cfg);
    return this;
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder addAll(Collection<CacheConfig> cfg) {
    caches.addAll(cfg);
    return this;
  }


  @Override
  public CacheConfig.CacheConfigBuilder buildCacheConfig() {
    return new CacheConfigBuilderImpl(this);
  }


  @Override
  public IgniteConfig build() {
    return new IgniteConfigImpl(server, storage, joinTimeout, caches);
  }


  @Override
  public MicronConfig.MicronConfigBuilder setBuild() {
    if(micron == null) {
      throw new IllegalStateException("IgniteConfigBuilder is not in MicronConfigBuilder context");
    }
    return micron.setIgniteConfig(build());
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.micron);
    hash = 79 * hash + Objects.hashCode(this.server);
    hash = 79 * hash + Objects.hashCode(this.storage);
    hash = 79 * hash + (int) (this.joinTimeout ^ (this.joinTimeout >>> 32));
    hash = 79 * hash + Objects.hashCode(this.caches);
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
    final IgniteConfigBuilderImpl other = (IgniteConfigBuilderImpl) obj;
    if(this.joinTimeout != other.joinTimeout) {
      return false;
    }
    if(!Objects.equals(this.micron, other.micron)) {
      return false;
    }
    if(!Objects.equals(this.server, other.server)) {
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
    return "IgniteConfigBuilder{" + "server=" + server + ", storage=" + storage + ", joinTimeout=" + joinTimeout + ", caches=" + caches + '}';
  }

}
