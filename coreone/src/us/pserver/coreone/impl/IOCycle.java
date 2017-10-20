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

import java.util.concurrent.Phaser;
import us.pserver.coreone.Core;
import us.pserver.coreone.Duplex;
import us.pserver.fun.ThrowableFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class IOCycle<O,I> extends AbstractCycle<O,I> {
  
  private final Duplex<I,O> duplex;
  
  private final ThrowableFunction<O,I> fun;
  
  
  public IOCycle(ThrowableFunction<O,I> fn, Phaser ph) {
    super(ph);
    this.duplex = new IODuplex(new DefaultPipe(), new DefaultPipe(), this);
    this.fun = NotNull.of(fn).getOrFail("Bad null ThrowableFunction");
  }
  
  
  @Override
  public Duplex<I,O> start() {
    phaser.register();
    Core.INSTANCE.execute(this);
    return duplex;
  }


  @Override
  public void run() {
    try {
      System.out.println(">>> IOCycle.entering while");
      while(true) {
        if(duplex.output().isClosed()) break;
        O in = duplex.output().pull();
        suspend.get().suspend();
        I out = fun.apply(in); 
        if(duplex.input().isClosed()) break;
        duplex.input().push(out);
      }
      System.out.println(">>> IOCycle.done while!");
    }
    catch(Exception e) {
      e.printStackTrace();
      duplex.input().error(e);
    }
    finally {
      locked(join::signalAll);
      phaser.arriveAndDeregister();
      System.out.println(">>> IOCycle.finished: "+ phaser.getUnarrivedParties());
    }
  }

}
