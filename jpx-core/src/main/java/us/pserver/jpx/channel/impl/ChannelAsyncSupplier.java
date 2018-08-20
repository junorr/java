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

package us.pserver.jpx.channel.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import us.pserver.jpx.channel.Channel;
import us.pserver.tools.fn.ThrowableFunction;
import us.pserver.tools.fn.ThrowableSupplier;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2018
 */
public class ChannelAsyncSupplier<O> extends AbstractChannelAsync<O> {
  
  private final ThrowableSupplier<O> fn;
  
  private final AtomicReference<O> output;
  
  
  public ChannelAsyncSupplier(Channel channel, ThrowableSupplier<O> fn) {
    super(channel);
    this.fn = Objects.requireNonNull(fn);
    this.output = new AtomicReference<>();
  }


  @Override
  public Optional<O> get() {
    return Optional.ofNullable(output.get());
  }
  
  
  @Override
  public void run() {
    try {
      output.compareAndSet(null, fn.supply());
      onSuccess();
    }
    catch(Exception e) {
      onError(e);
    }
    finally {
      onComplete();
      signalAll();
    }
  }
  
}
