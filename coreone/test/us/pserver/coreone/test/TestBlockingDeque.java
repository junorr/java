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

package us.pserver.coreone.test;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.fun.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/10/2017
 */
public class TestBlockingDeque {

  
  public static void main(String[] args) {
    BlockingDeque<String> deque = new LinkedBlockingDeque<>();
    AtomicInteger count = new AtomicInteger(1);
    
    Thread t1 = new Thread(()->{
      for(int i = 0; i < 10; i++) {
        String s = String.format("%s: %d", Thread.currentThread().getName(), count.getAndIncrement());
        Rethrow.unchecked().apply(()->deque.putLast(s));
      }
    });
    t1.start();
    Thread t2 = new Thread(()->{
      for(int i = 0; i < 10; i++) {
        String s = String.format("%s: %d", Thread.currentThread().getName(), count.getAndIncrement());
        Rethrow.unchecked().apply(()->deque.putLast(s));
      }
    });
    t2.start();
    Thread t3 = new Thread(()->{
      for(int i = 0; i < 10; i++) {
        String s = String.format("%s: %d", Thread.currentThread().getName(), count.getAndIncrement());
        Rethrow.unchecked().apply(()->deque.putLast(s));
      }
    });
    t3.start();
    for(int i = 0; i < 30; i++) {
      System.out.printf(">>> %s <<<%n", Rethrow.unchecked().apply(()->deque.takeFirst()));
    }
  }
  
}
