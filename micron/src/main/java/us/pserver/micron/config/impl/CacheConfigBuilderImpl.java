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
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import us.pserver.micron.config.CacheConfig;
import us.pserver.micron.config.CacheConfig.CacheConfigBuilder;
import us.pserver.micron.config.IgniteConfig;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public class CacheConfigBuilderImpl implements CacheConfig.CacheConfigBuilder {
  
  private IgniteConfig.IgniteConfigBuilder ignite;
  
  private String name;
  
  private int backups;
  
  private CacheMode mode;
  
  private CacheRebalanceMode rebalance;
  
  private Optional<CacheConfig.CacheExpiryPolicy> expirypol;
  
  private int duration;
  

  public CacheConfigBuilderImpl(IgniteConfig.IgniteConfigBuilder ignite) {
    this.ignite = ignite;
    name = null;
    backups = 0;
    mode = CacheMode.LOCAL;
    rebalance = CacheRebalanceMode.ASYNC;
    expirypol = Optional.empty();
    duration = 0;
  }
  
  
  public CacheConfigBuilderImpl() {
    this(null);
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
  public Optional<CacheConfig.CacheExpiryPolicy> getCacheExpiryPolicy() {
    return this.expirypol;
  }


  @Override
  public int getExpiryDuration() {
    return this.duration;
  }


  @Override
  public CacheConfig.CacheConfigBuilder setName(String name) {
    this.name = name;
    return this;
  }


  @Override
  public CacheConfig.CacheConfigBuilder setBackups(int backups) {
    this.backups = backups;
    return this;
  }


  @Override
  public CacheConfig.CacheConfigBuilder setCacheMode(CacheMode mode) {
    this.mode = mode;
    return this;
  }
  
  
  @Override
  public CacheConfig.CacheConfigBuilder setCacheRebalanceMode(CacheRebalanceMode mode) {
    this.rebalance = mode;
    return this;
  }
  
  
  @Override
  public CacheConfig.CacheConfigBuilder setCacheExpiryPolicy(Optional<CacheConfig.CacheExpiryPolicy> expiryPolicy) {
    this.expirypol = expiryPolicy;
    return this;
  }


  @Override
  public CacheConfig.CacheConfigBuilder setExpiryDuration(int duration) {
    this.duration = duration;
    return this;
  }


  @Override
  public CacheConfig build() {
    return new CacheConfigImpl(name, backups, mode, rebalance, expirypol, duration);
  }


  @Override
  public IgniteConfig.IgniteConfigBuilder addBuild() {
    if(ignite == null) {
      throw new IllegalStateException("CacheConfigBuilder is not in IgniteConfigBuilder context");
    }
    return ignite.add(build());
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 83 * hash + Objects.hashCode(this.ignite);
    hash = 83 * hash + Objects.hashCode(this.name);
    hash = 83 * hash + this.backups;
    hash = 83 * hash + Objects.hashCode(this.mode);
    hash = 83 * hash + Objects.hashCode(this.rebalance);
    hash = 83 * hash + Objects.hashCode(this.expirypol);
    hash = 83 * hash + this.duration;
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
    if(!CacheConfigBuilder.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final CacheConfigBuilder other = (CacheConfigBuilder) obj;
    if(this.backups != other.getBackups()) {
      return false;
    }
    if(this.duration != other.getExpiryDuration()) {
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
    if(!Objects.equals(this.expirypol, other.getCacheExpiryPolicy())) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "CacheConfigBuilder{" + "name=" + name + ", backups=" + backups + ", mode=" + mode + ", rebalance=" + rebalance + ", expirypol=" + expirypol + ", duration=" + duration + '}';
  }

}
