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

package us.pserver.test;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import us.pserver.fastgear.Wire;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/06/2016
 */
public interface TestWire<T> {

  public static void main(String[] args) throws InterruptedException {
    final Wire<Integer> wire = Wire.defaultWire();
    new Thread(() -> {
      System.out.printf("* %s pulling wire for 5 seconds...%n", Thread.currentThread().getName());
      Instant start = Instant.now();
      Optional<Integer> opt = wire.pull(5000);
      Instant end = Instant.now();
      Duration dur = Duration.between(start, end);
      System.out.printf("* %s Finally got the value after %s%n", Thread.currentThread().getName(), dur.toString().substring(2));
      System.out.printf("* %s value is: %s%n", Thread.currentThread().getName(), opt);
      System.out.printf("* %s is done!%n", Thread.currentThread().getName());
    }).start();
    new Thread(() -> {
      System.out.printf("* %s assigning a consumer to wire...%n", Thread.currentThread().getName());
      Instant start = Instant.now();
      wire.onAvailable(t -> {
        Instant end = Instant.now();
        Duration dur = Duration.between(start, end);
        System.out.printf("* %s Finally got the value after %s%n", Thread.currentThread().getName(), dur.toString().substring(2));
        System.out.printf("* %s value is: %d%n", Thread.currentThread().getName(), t);
      });
      System.out.printf("* %s is done!%n", Thread.currentThread().getName());
    }).start();
    System.out.println("- Sleeping for 3 seconds...");
    Thread.sleep(3000);
    System.out.println("- Wake Up after 3 seconds");
    System.out.println("- wire.push( 32 );");
    wire.push(32);
    System.out.print("- Trying to get a value: wire.pull(1000)=");
    System.out.println(wire.pull(1000));
    System.out.printf("- available_processors="+ Runtime.getRuntime().availableProcessors());
    System.out.println("- Alright, finishing up here!");
  }
  
}
