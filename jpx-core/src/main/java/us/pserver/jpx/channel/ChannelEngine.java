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

package us.pserver.jpx.channel;

import java.util.concurrent.ExecutorService;
import us.pserver.jpx.pool.impl.ByteBufferPool;
import us.pserver.tools.fn.ThrowableConsumer;
import us.pserver.tools.fn.ThrowableFunction;
import us.pserver.tools.fn.ThrowableSupplier;
import us.pserver.tools.fn.ThrowableTask;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/08/2018
 */
public interface ChannelEngine {
  
  public ChannelConfiguration getConfiguration();
  
  public ExecutorService getIOExecutorService();
  
  public ExecutorService getSystemExecutorService();
  
  public ByteBufferPool getByteBufferPool();
  
  public ChannelAsync executeIO(Channel ch, Runnable rn);
  
  public ChannelAsync executeIO(Channel ch, ThrowableTask rn);
  
  public <I> ChannelAsync executeIO(Channel ch, I input, ThrowableConsumer<I> cs);
  
  public <I,O> ChannelAsync<O> executeIO(Channel ch, I input, ThrowableFunction<I,O> fn);
  
  public <O> ChannelAsync<O> executeIO(Channel ch, ThrowableSupplier<O> fn);
  
  public ChannelAsync execute(Channel ch, Runnable rn);
  
  public ChannelAsync execute(Channel ch, ThrowableTask rn);
  
  public <I> ChannelAsync execute(Channel ch, I input, ThrowableConsumer<I> cs);
  
  public <I,O> ChannelAsync<O> execute(Channel ch, I input, ThrowableFunction<I,O> fn);
  
  public <O> ChannelAsync<O> execute(Channel ch, ThrowableSupplier<O> fn);
  
}
