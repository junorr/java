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

package us.pserver.micro.config;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import us.pserver.micron.config.impl.IgniteConfigBuilderImpl;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2019
 */
public interface IgniteConfig {

  public ServerConfig getIgniteServerConfig();
  
  public Optional<Path> getStorage();
  
  public long getJoinTimeout();
  
  public Set<CacheConfig> getCacheConfigSet();
  
  
  
  public static IgniteConfigBuilder builder() {
    return new IgniteConfigBuilderImpl();
  }
  
  
  
  
  
  public interface IgniteConfigBuilder {
    
    public ServerConfig getIgniteServerConfig();

    public Optional<Path> getStorage();

    public long getJoinTimeout();

    public Set<CacheConfig> getCacheConfigSet();
    
    public IgniteConfigBuilder setIgniteServerConfig(String addr, int port);
    
    public IgniteConfigBuilder setIgniteServerConfig(ServerConfig cfg);
    
    public IgniteConfigBuilder setStorage(Optional<Path> storage);
    
    public IgniteConfigBuilder setJoinTimeout(long timeout);
    
    public IgniteConfigBuilder clearCacheConfigSet();
    
    public IgniteConfigBuilder add(CacheConfig cfg);
    
    public IgniteConfigBuilder addAll(Collection<CacheConfig> cfg);
    
    public CacheConfig.CacheConfigBuilder buildCacheConfig();
    
    public IgniteConfig build();
    
    public MicronConfig.MicronConfigBuilder setBuild();
    
  }
  
}
