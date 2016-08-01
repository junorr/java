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

package br.com.bb.disec.micro.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class PublicCache {

  public static final String NAME_CACHE_PUBLIC = "public-cache";
  
  public static final PublicCache INSTANCE = new PublicCache();

  
  private final HazelcastInstance hazelcast;
  
  private final IMap<String,String> cache;
  
  
  public PublicCache() {
    hazelcast = Hazelcast.newHazelcastInstance();
    cache = hazelcast.getMap(NAME_CACHE_PUBLIC);
  }
  
  
  public static HazelcastInstance getHazelcastInstance() {
    return INSTANCE.hazelcast;
  }
  
  
  public static IMap<String,String> getCache() {
    return INSTANCE.cache;
  }
  
  
  public static String get(String key) {
    return INSTANCE.cache.get(key);
  }
  
  
  public static long getTtl(String key) {
    return INSTANCE.cache.getEntryView(key).getTtl();
  }
  
  
  public static boolean contains(String key) {
    return INSTANCE.cache.containsKey(key);
  }
  
  
  public static void shutdown() {
    INSTANCE.hazelcast.shutdown();
  }
  
}
