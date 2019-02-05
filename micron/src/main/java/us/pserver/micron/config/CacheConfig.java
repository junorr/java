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

package us.pserver.micron.config;

import java.util.Optional;
import javax.cache.configuration.Factory;
import javax.cache.expiry.ExpiryPolicy;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import us.pserver.micron.config.impl.CacheConfigBuilderImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2019
 */
public interface CacheConfig {

  public static enum CacheExpiryPolicy {
    CREATED, ACCESSED, MODIFIED, TOUCHED, ETERNAL
  }

  public String getName();

  public int getBackups();

  public CacheMode getCacheMode();
  
  public CacheRebalanceMode getCacheRebalanceMode();
  
  public Optional<CacheExpiryPolicy> getCacheExpiryPolicy();

  public int getExpiryDuration();

  public Optional<Factory<ExpiryPolicy>> getExpiryPolicyFactory();
  
  
  
  public static CacheConfigBuilder builder() {
    return new CacheConfigBuilderImpl();
  }
  
  
  
  
  
  public interface CacheConfigBuilder {
    
    public String getName();

    public int getBackups();

    public CacheMode getCacheMode();
    
    public CacheRebalanceMode getCacheRebalanceMode();
    
    public Optional<CacheExpiryPolicy> getCacheExpiryPolicy();

    public int getExpiryDuration();

    public CacheConfigBuilder setName(String name);
    
    public CacheConfigBuilder setBackups(int backups);
    
    public CacheConfigBuilder setCacheMode(CacheMode mode);
    
    public CacheConfigBuilder setCacheRebalanceMode(CacheRebalanceMode mode);
    
    public CacheConfigBuilder setCacheExpiryPolicy(Optional<CacheExpiryPolicy> expiryPolicy);
    
    public CacheConfigBuilder setExpiryDuration(int duration);
    
    public CacheConfig build();
    
    public IgniteConfig.IgniteConfigBuilder addBuild();
    
  }
    
}
