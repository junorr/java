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

package com.jpower.worker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler
 * @version 0.0 - 09/12/2012
 */
public class Worker implements Runnable {
  
  private final List<Job> jobs;
  
  private Thread thread;
  
  private boolean running, waiting;
  
  private static int instnum = 1;
  
  private JobContext context;
  
  
  public Worker(String name) {
    jobs = Collections.synchronizedList(new LinkedList<Job>());
    running = false;
    waiting = false;
    if(name == null) name = "Worker"+ instnum;
    thread = new Thread(this, name);
    context = new JobContext(this);
  }
  
  
  public boolean add(Job job) {
    if(job == null || jobs.contains(job))
      return false;
    
    synchronized(jobs) {
      if(jobs.add(job)) {
        if(waiting) this.wakeup();
        return true;
        
      } else {
        return false;
      }
    }
  }
  
  
  public void clearJobs() {
    synchronized(jobs) {
      jobs.clear();
    }
  }


  public JobContext getContext() {
    return context;
  }


  public Worker setContext(JobContext context) {
    if(context != null)
      this.context = context;
    return this;
  }
  
  
  public Job poll() {
    synchronized(jobs) {
      if(this.isEmpty())
        return null;
      return jobs.remove(0);
    }
  }
  
  
  public boolean isEmpty() {
    synchronized(jobs) {
      return jobs.isEmpty();
    }
  }
  
  
  public boolean isRunning() {
    return running;
  }
  
  
  public boolean isWaiting() {
    return waiting;
  }
  
  
  public Thread thread() {
    return thread;
  }
  
  
  public void start() {
    waiting = false;
    running = true;
    thread.start();
  }
  
  
  public void stop() {
    waiting = false;
    running = false;
  }
  
  
  private void sleep() {
    synchronized(jobs) {
      waiting = true;
      try { jobs.wait(); }
      catch(InterruptedException ex) {}
    }
  }
  
  
  private void wakeup() {
    synchronized(jobs) {
      jobs.notify();
      waiting = false;
    }
  }


  @Override
  public void run() {
    while(running) {
      if(this.isEmpty()) {
        this.sleep();
      }
      
      Job job = this.poll();
      if(job == null) continue;
      
      try {
        job.execute(context);
      } catch(Exception ex) {
        job.error(context, ex);
      }
    }
  }

}
