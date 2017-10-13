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

package us.pserver.coreone;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import us.pserver.coreone.imple.ConsumerCycle;
import us.pserver.coreone.imple.IOCycle;
import us.pserver.coreone.imple.SupplierCycle;
import us.pserver.coreone.imple.TaskCycle;
import us.pserver.fun.ThrowableConsumer;
import us.pserver.fun.ThrowableFunction;
import us.pserver.fun.ThrowableSupplier;
import us.pserver.fun.ThrowableTask;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public interface Cycle<I,O> extends Runnable {

  public Duplex<O,I> start();
  
  public void suspend(long timeout);
  
  public void resume();
  
  public void join();
  
  
  
  public static <A,B> Cycle<A,B> of(ThrowableFunction<A,B> fun) {
    return new IOCycle(fun);
  }
  
  //public static <A,B> Cycle<B,A> of(Function<A,B> fun) {
    //return of(ThrowableFunction.of(fun));
  //}
  
  
  public static <A> Cycle<Void,A> of(ThrowableSupplier<A> sup) {
    return new SupplierCycle(sup);
  }
  
  //public static <A> Cycle<Void,A> of(Supplier<A> sup) {
    //return of(ThrowableSupplier.of(sup));
  //}
  
  
  public static <B> Cycle<B,Void> of(ThrowableConsumer<B> cs) {
    return new ConsumerCycle(cs);
  }
  
  //public static <B> Cycle<B,Void> of(Consumer<B> cs) {
    //return of(ThrowableConsumer.of(cs));
  //}
  
  
  public static Cycle<Void,Void> of(ThrowableTask tsk) {
    return new TaskCycle(tsk);
  }
  
  //public static Cycle<Void,Void> of(Runnable run) {
    //return of(ThrowableTask.of(run));
  //}
  
}
