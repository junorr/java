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

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import javax.cache.expiry.ExpiryPolicy;
import org.apache.ignite.cache.CacheMode;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2019
 */
public interface IgniteConfig {

  public String getAddress();
  
  public int getPort();
  
  public Optional<Path> getStorage();
  
  public long getJoinTimeout();
  
  public Set<CacheConfig> getCacheConfigSet();
  
  
  
  public static enum CacheExpiryPolicy {
    CREATED, ACCESSED, MODIFIED, TOUCHED, ETERNAL
  }

  
  
  public interface CacheConfig {
    
    public String getName();
    
    public int getBackups();
    
    public CacheMode getCacheMode();
    
    public Optional<CacheExpiryPolicy> getCacheExpiryPolicy();
    
    public int getExpiryDuration();
    
    public Optional<javax.cache.configuration.Factory<ExpiryPolicy>> getExpiryPolicyFactory();
    
  }
  
}
