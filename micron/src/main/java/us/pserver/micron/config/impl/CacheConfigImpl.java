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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.cache.configuration.Factory;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import us.pserver.micron.config.CacheConfig;
import us.pserver.micron.config.CacheConfig.CacheExpiryPolicy;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.ACCESSED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.CREATED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.ETERNAL;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.MODIFIED;
import static us.pserver.micron.config.CacheConfig.CacheExpiryPolicy.TOUCHED;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class CacheConfigImpl implements CacheConfig {
  
  private final String name;
  
  private final int backups;
  
  private final CacheMode mode;
  
  private final CacheRebalanceMode rebalance;
  
  private final Optional<CacheExpiryPolicy> expiryPolicy;
  
  private final int expiryDuration;


  public CacheConfigImpl(String name, int backups, CacheMode mode, CacheRebalanceMode rebalance, Optional<CacheExpiryPolicy> expiryPolicy, int expiryDuration) {
    this.name = Match.notEmpty(name).getOrFail("Bad null/empty name");
    this.backups = backups;
    this.mode = Match.notNull(mode).getOrFail("Bad null CacheMode");
    this.expiryPolicy = Match.notNull(expiryPolicy).getOrFail("Bad null expiryPolicy");
    this.expiryDuration = expiryDuration;
    this.rebalance = rebalance;
  }

  @Override
  public String getName() {
    return name;
  }


  @Override
  public int getBackups() {
    return backups;
  }


  @Override
  public CacheMode getCacheMode() {
    return mode;
  }
  
  
  @Override
  public CacheRebalanceMode getCacheRebalanceMode() {
    return rebalance;
  }
  
  
  @Override
  public Optional<CacheExpiryPolicy> getCacheExpiryPolicy() {
    return this.expiryPolicy;
  }


  @Override
  public int getExpiryDuration() {
    return this.expiryDuration;
  }


  @Override
  public Optional<Factory<ExpiryPolicy>> getExpiryPolicyFactory() {
    if(expiryPolicy.isPresent()) {
      switch(expiryPolicy.get()) {
        case ACCESSED:
          return Optional.of(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, expiryDuration)));
        case CREATED:
          return Optional.of(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, expiryDuration)));
        case ETERNAL:
          return Optional.of(EternalExpiryPolicy.factoryOf());
        case MODIFIED:
          return Optional.of(ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, expiryDuration)));
        case TOUCHED:
          return Optional.of(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, expiryDuration)));
      }
    }
    return Optional.empty();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(this.name);
    hash = 41 * hash + this.backups;
    hash = 41 * hash + Objects.hashCode(this.mode);
    hash = 41 * hash + Objects.hashCode(this.expiryPolicy);
    hash = 41 * hash + this.expiryDuration;
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
    if(!CacheConfig.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final CacheConfig other = (CacheConfig) obj;
    if(this.backups != other.getBackups()) {
      return false;
    }
    if(this.expiryDuration != other.getExpiryDuration()) {
      return false;
    }
    if(!Objects.equals(this.name, other.getName())) {
      return false;
    }
    if(this.mode != other.getCacheMode()) {
      return false;
    }
    if(this.rebalance != other.getCacheRebalanceMode()) {
      return false;
    }
    if(!Objects.equals(this.expiryPolicy, other.getCacheExpiryPolicy())) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "CacheConfig{" + "name=" + name + ", backups=" + backups + ", mode=" + mode+ ", rebalance=" + rebalance + ", expiryPolicy=" + expiryPolicy + ", expiryDuration=" + expiryDuration + '}';
  }

}
