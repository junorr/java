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

package us.pserver.fastgear.spin;

import java.util.Optional;
import java.util.function.Consumer;
import us.pserver.fastgear.Running;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/06/2016
 */
@FunctionalInterface
public interface IConsumerSpin<I, E extends Exception> {

  public void spin(I t) throws E;
  
  public default void spin(Running<Void,I> run) {
    while(!run.output().isClosed()) {
      try {
        if(run.output().isAvailable()) {
          Optional<I> pull = run.output().pull(0);
          if(pull.isPresent()) {
            spin(pull.get());
          }
        }
        else synchronized(this) {
          this.wait(50);
        }
      }
      catch(Exception e) {
        e.printStackTrace();
        run.exception(e);
      }
    }//while
    run.complete();
    run.gear().signal();
  }

  
  public static <T> IConsumerSpin<T,RuntimeException> of(Consumer<T> c) {
    return i->c.accept(i);
  }
  
}
