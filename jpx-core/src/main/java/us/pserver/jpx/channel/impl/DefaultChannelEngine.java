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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAsync;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
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
public class DefaultChannelEngine implements ChannelEngine {
  
  private final ChannelConfiguration cfg;
  
  private final ExecutorService ioexec;
  
  private final ExecutorService sysexec;
  
  private final ByteBufferPool pool;
  
  
  public DefaultChannelEngine(ChannelConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
    pool = new ByteBufferPool(cfg.getBufferPoolConfiguration());
    ioexec = Executors.newFixedThreadPool(cfg.getIOThreadPoolSize(), DefaultChannelEngine::newIOPoolThread);
    sysexec = Executors.newFixedThreadPool(cfg.getComputeThreadPoolSize(), DefaultChannelEngine::newSystemPoolThread);
  }
  
  
  private static Thread newIOPoolThread(Runnable r) {
    return newNamedThread("IOPool", r);
  }
  
  
  private static Thread newSystemPoolThread(Runnable r) {
    return newNamedThread("SystemPool", r);
  }
  
  
  private static Thread newNamedThread(String prefix, Runnable r) {
    Thread t = Executors.defaultThreadFactory().newThread(r);
    String[] split = t.getName().split("-");
    t.setName(String.format("%s-thread-%s", prefix, split[3]));
    return t;
  }
  
  
  @Override
  public ChannelConfiguration getConfiguration() {
    return cfg;
  }
  
  
  @Override
  public ExecutorService getIOExecutorService() {
    return ioexec;
  }


  @Override
  public ExecutorService getComputeExecutorService() {
    return sysexec;
  }


  @Override
  public ByteBufferPool getByteBufferPool() {
    return pool;
  }
  
  
  @Override
  public ChannelAsync executeIO(Channel ch, Runnable rn) {
    ChannelAsyncTask task = new ChannelAsyncTask(ch, rn::run);
    ioexec.submit(task);
    return task;
  }
  
  
  @Override
  public ChannelAsync executeIO(Channel ch, ThrowableTask rn) {
    ChannelAsyncTask task = new ChannelAsyncTask(ch, rn);
    ioexec.submit(task);
    return task;
  }
  
  
  @Override
  public <I> ChannelAsync executeIO(Channel ch, I input, ThrowableConsumer<I> cs) {
    ChannelAsyncConsumer task = new ChannelAsyncConsumer(ch, input, cs);
    ioexec.submit(task);
    return task;
  }
  
  
  @Override
  public <I,O> ChannelAsync executeIO(Channel ch, I input, ThrowableFunction<I,O> fn) {
    ChannelAsyncFunction task = new ChannelAsyncFunction(ch, input, fn);
    ioexec.submit(task);
    return task;
  }
  
  
  @Override
  public <O> ChannelAsync<O> executeIO(Channel ch, ThrowableSupplier<O> fn) {
    ChannelAsyncSupplier<O> task = new ChannelAsyncSupplier(ch, fn);
    ioexec.submit(task);
    return task;
  }
  
  
  @Override
  public ChannelAsync execute(Channel ch, Runnable rn) {
    ChannelAsyncTask task = new ChannelAsyncTask(ch, rn::run);
    sysexec.submit(task);
    return task;
  }
  
  
  @Override
  public ChannelAsync execute(Channel ch, ThrowableTask rn) {
    ChannelAsyncTask task = new ChannelAsyncTask(ch, rn);
    sysexec.submit(task);
    return task;
  }
  
  
  @Override
  public <I> ChannelAsync execute(Channel ch, I input, ThrowableConsumer<I> cs) {
    ChannelAsyncConsumer task = new ChannelAsyncConsumer(ch, input, cs);
    sysexec.submit(task);
    return task;
  }
  
  
  @Override
  public <I,O> ChannelAsync<O> execute(Channel ch, I input, ThrowableFunction<I,O> fn) {
    ChannelAsyncFunction task = new ChannelAsyncFunction(ch, input, fn);
    sysexec.submit(task);
    return task;
  }
  
  
  @Override
  public <O> ChannelAsync<O> execute(Channel ch, ThrowableSupplier<O> fn) {
    ChannelAsyncSupplier<O> task = new ChannelAsyncSupplier(ch, fn);
    sysexec.submit(task);
    return task;
  }
  
}
