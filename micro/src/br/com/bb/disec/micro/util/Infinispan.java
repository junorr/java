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

package br.com.bb.disec.micro.util;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/02/2017
 */
public class Infinispan {

  private final Cache<String,Object> cache;
  
  private static final Infinispan instance = new Infinispan();
  
  
  private Infinispan() {
    if(instance != null) {
      throw new IllegalStateException(
          "InfinispanSetup is already instantiated");
    }
    cache = setup();
  }
  
  
  private Cache setup() {
    GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
    ConfigurationBuilder config = new ConfigurationBuilder();
    config.clustering().cacheMode(CacheMode.REPL_ASYNC);
    DefaultCacheManager m = new DefaultCacheManager(global.build(), config.build());
    return m.getCache();
  }
  
  
  public static Cache<String,Object> cache() {
    return instance.cache;
  }
  
  
  public static <T> T getAs(String key) {
    return (T) instance.cache.get(key);
  }
  
  
  public static void stop() {
    instance.cache.stop();
    instance.cache.getCacheManager().stop();
  }
  
}
