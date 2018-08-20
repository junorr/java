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

package us.pserver.jpx.pool.test;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAsync;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2018
 */
public class TestChannelAsync {
  
  private static Channel channel = Mockito.mock(Channel.class);
  
  private static ChannelEngine engine = new DefaultChannelEngine(new DefaultChannelConfiguration());
  

  @Test
  public void testChannelAsyncTask() {
    System.out.println("* testChannelAsyncTask");
    ChannelAsync task = engine.execute(channel, () -> {
      Sleeper.of(3000).sleep();
      System.out.println("Task " + Thread.currentThread().getName() + " executed!");
    });
    task.appendCompleteListener(c -> System.out.println("onComplete!"));
    System.out.println("Sync...");
    task.sync();
    System.out.println("After sync!");
  }
  
  @Test
  public void testChannelAsyncConsumer() {
    System.out.println("* testChannelAsyncConsumer");
    ChannelAsync task = engine.execute(channel, 5, i -> {
      Sleeper.of(3000).sleep();
      System.out.println("Consumer(" + i + ") " + Thread.currentThread().getName() + " executed!");
    });
    task.appendCompleteListener(c -> System.out.println("onComplete!"));
    System.out.println("Sync...");
    task.sync(4, TimeUnit.SECONDS);
    System.out.println("After sync: "+ task.get());
    System.out.println(engine.getConfiguration());
  }
  
  @Test
  public void testChannelAsyncFunction() {
    System.out.println("* testChannelAsyncFunction");
    ChannelAsync<String> task = engine.executeIO(channel, 5, i -> {
      Sleeper.of(3000).sleep();
      System.out.println("Function(" + i + ") " + Thread.currentThread().getName() + " executed!");
      return String.format("ChannelAsyncFunction( %d )", i);
    });
    task.appendCompleteListener(c -> System.out.println("onComplete!"));
    System.out.println("Sync...");
    task.sync();
    System.out.println("After sync: "+ task.get());
  }
  
  @Test
  public void testChannelAsyncSupplier() {
    System.out.println("* testChannelAsyncSupplier");
    ChannelAsync<String> task = engine.executeIO(channel, () -> {
      Sleeper.of(3000).sleep();
      System.out.println("Supplier<String> " + Thread.currentThread().getName() + " executed!");
      return String.format("ChannelAsyncSupplier( %s )", Thread.currentThread().getName());
    });
    task.appendCompleteListener(c -> System.out.println("onComplete!"));
    System.out.println("Sync...");
    task.sync();
    System.out.println("After sync: "+ task.get());
  }
  
}
