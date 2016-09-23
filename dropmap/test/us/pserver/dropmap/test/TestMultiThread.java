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

package us.pserver.dropmap.test;

import java.time.Duration;
import us.pserver.dropmap.DMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/09/2016
 */
public class TestMultiThread {
  
  public static void pause(Duration dur) {
    if(dur != null) {
      try {
        Thread.sleep(dur.toMillis());
      } catch(InterruptedException e) {}
    }
  }

  
  public static void main(String[] args) throws InterruptedException {
    final DMap<String,Integer> map = DMap.newMap();
    
    new Thread(()->{
      for(int i = 1000; i < 2000; i++) {
        map.put(String.valueOf(i), i, Duration.ofMillis(500), e->System.out.println("#>> "+ e.getKey()));
        pause(Duration.ofMillis(60));
      }
      System.out.println("* Thread-1 Done!");
    }).start();
    
    new Thread(()->{
      for(int i = 2000; i < 3000; i++) {
        map.put(String.valueOf(i), i, Duration.ofMillis(500), e->System.out.println("#>> "+ e.getKey()));
        pause(Duration.ofMillis(60));
      }
      System.out.println("* Thread-2 Done!");
    }).start();
    
    new Thread(()->{
      for(int i = 3000; i < 4000; i++) {
        map.put(String.valueOf(i), i, Duration.ofMillis(500), e->System.out.println("#>> "+ e.getKey()));
        pause(Duration.ofMillis(60));
      }
      System.out.println("* Thread-3 Done!");
    }).start();
    
    new Thread(()->{
      for(int i = 4000; i < 5000; i++) {
        map.put(String.valueOf(i), i, Duration.ofMillis(500), e->System.out.println("#>> "+ e.getKey()));
        pause(Duration.ofMillis(60));
      }
      System.out.println("* Thread-4 Done!");
    }).start();
  }
  
}
