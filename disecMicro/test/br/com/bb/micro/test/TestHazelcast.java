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

package br.com.bb.micro.test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public class TestHazelcast {

  
  public static void main(String[] args) throws InterruptedException {
    HazelcastInstance hi = Hazelcast.newHazelcastInstance();
    IMap<String,Object> map = hi.getMap("my-hazelcast-map");
    map.put("key", "value", 5, TimeUnit.SECONDS);
    System.out.println("* Thread.sleep(4000);");
    Thread.sleep(4000);
    System.out.println("* map.get(key): "+ map.get("key"));
    System.out.println("* Thread.sleep(1000);");
    Thread.sleep(1000);
    System.out.println("* map.get(key): "+ map.get("key"));
  }
  
}
