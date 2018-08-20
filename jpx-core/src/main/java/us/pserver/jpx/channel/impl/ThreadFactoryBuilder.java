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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/08/2018
 */
public class ThreadFactoryBuilder {
  
  private String namePrefix = null;
  
  private boolean daemon = false;
  
  private int priority = Thread.NORM_PRIORITY;
  
  private ThreadFactory backingThreadFactory = null;
  
  private UncaughtExceptionHandler uncaughtExceptionHandler = null;
  

  public ThreadFactoryBuilder setNamePrefix(String namePrefix) {
    if (namePrefix == null) {
      throw new NullPointerException();
    }
    this.namePrefix = namePrefix;
    return this;
  }

  public ThreadFactoryBuilder setDaemon(boolean daemon) {
    this.daemon = daemon;
    return this;
  }

  public ThreadFactoryBuilder setPriority(int priority) {
    if (priority < Thread.MIN_PRIORITY) {
      throw new IllegalArgumentException(String.format(
          "Thread priority (%s) must be >= %s", priority,
          Thread.MIN_PRIORITY));
    }

    if (priority > Thread.MAX_PRIORITY) {
      throw new IllegalArgumentException(String.format(
          "Thread priority (%s) must be <= %s", priority,
          Thread.MAX_PRIORITY));
    }

    this.priority = priority;
    return this;
  }

  public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
    if (null == handler) {
      throw new NullPointerException(
          "UncaughtExceptionHandler cannot be null");
    }
    this.uncaughtExceptionHandler = handler;
    return this;
  }

  public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
    if (null == uncaughtExceptionHandler) {
      throw new NullPointerException(
          "BackingThreadFactory cannot be null");
    }
    this.backingThreadFactory = backingThreadFactory;
    return this;
  }

  //public ThreadFactory build() {
    //final ThreadFactory backingThreadFactory = (this.backingThreadFactory != null) 
        //? this.backingThreadFactory
        //: Executors.defaultThreadFactory();
//
    //final AtomicLong count = new AtomicLong(0);
//
    //return (Runnable runnable) -> {
      //Thread thread = backingThreadFactory.newThread(runnable);
      //if (namePrefix != null) {
        //thread.setName(namePrefix + "-" + count.getAndIncrement());
      //}
      //if (daemon != null) {
        //thread.setDaemon(daemon);
      //}
      //if (priority != null) {
        //thread.setPriority(priority);
      //}
      //if (uncaughtExceptionHandler != null) {
        //thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
      //}
      //return thread;
    //};
  //}

}