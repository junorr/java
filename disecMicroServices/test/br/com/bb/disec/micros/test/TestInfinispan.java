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

package br.com.bb.disec.micros.test;

import br.com.bb.disec.micros.db.Infinispan;
import org.infinispan.Cache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.Listener.Observation;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/02/2017
 */
@Listener(clustered = true, observation = Observation.POST)
public class TestInfinispan {

  @CacheEntryCreated
  public void entryAdded(CacheEntryCreatedEvent evt) {
    if (!evt.isPre()) {
      String key = (String) evt.getKey();
      System.out.println("* New entry: '" + key + "'");
      Long l = (Long) evt.getValue();
      System.out.println("* Time elapsed: "+ (System.currentTimeMillis() - l));
      if(key.equals("k4")) {
        try {
          evt.getCache().stop();
          evt.getCache().getCacheManager().stop();
        } catch(Exception e) {}
      }
    }
  }
  
  
  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  
  public static void client() {
    Cache<String,Object> cache = Infinispan.cache();
    cache.addListener(new TestInfinispan());
    System.out.println(cache.getCacheManager().getMembers());
  }
  
  
  public static void server() {
    Cache<String,Object> cache = Infinispan.cache();
    cache.addListener(new TestInfinispan());
    sleep(5000);
    System.out.println(cache.getCacheManager().getMembers());
    for(int i = 0; i < 5; i++) {
      cache.put("k"+i, System.currentTimeMillis());
      sleep(800);
    }
    sleep(5000);
    cache.stop();
    cache.getCacheManager().stop();
  }
  
  
  
  public static void main(String[] args) {
    server();
    //client();
  }
  
}
