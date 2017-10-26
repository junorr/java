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

package us.pserver.coreone.impl;

import java.util.function.Function;
import us.pserver.coreone.Core;
import us.pserver.coreone.Duplex;
import us.pserver.fun.ThrowableConsumer;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class RepeatableConsumerCycle<O> extends AbstractCycle<O,Void> {
  
  private final Duplex<Void,O> duplex;
  
  private final ThrowableConsumer<O> fun;
  
  private final Function<Duplex<Void,O>,Boolean> until;
  
  
  public RepeatableConsumerCycle(ThrowableConsumer<O> fn, Function<Duplex<Void,O>,Boolean> until, CountDown cd) {
    super(cd);
    this.duplex = new OutputOnlyDuplex(new DefaultPipe(), this);
    this.fun = NotNull.of(fn).getOrFail("Bad null ThrowableFunction");
    this.until = NotNull.of(until).getOrFail("Bad null repeat condition Function<Duplex,Boolean>");
  }
  
  
  @Override
  public Duplex<Void,O> start() {
    countDown.increment();
    Core.INSTANCE.execute(this);
    return duplex;
  }


  @Override
  public void run() {
    try {
      while(!duplex.output().isClosed() && until.apply(duplex)) {
        suspend.get().suspend();
        fun.accept(duplex.output().pull());
      }
    } 
    catch(Exception e) {
      e.printStackTrace();
      duplex.output().error(e);
    }
    finally {
      locked(join::signalAll);
      countDown.decrement();
    }
  }

}
