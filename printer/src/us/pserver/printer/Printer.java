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

package us.pserver.printer;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import us.pserver.conc.Exclusive;
import us.pserver.conc.ExclusiveList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 22/04/2014
 */
public class Printer {
  
  public static final int THREADS = 1;
  
  public static final String LN = 
      (File.separatorChar == '/' ? "\n" : "\r\n");
  
  
  private final Object O = new Object();

  private ExclusiveList<String> list;
  
  private Exclusive<Boolean> run;
  
  private PrintStream output;
  
  private ExecutorService exec;
  
  private int threads;
  
  
  public Printer() {
    list = new ExclusiveList();
    output = System.out;
    threads = THREADS;
    run = new Exclusive();
  }
  
  
  public Printer(PrintStream ps) {
    this();
    output(ps);
  }
  
  
  public Printer output(PrintStream ps) {
    if(isRunning())
      throw new IllegalStateException(
          "Printer is running. Stop it first.");
    if(ps == null)
      throw new IllegalArgumentException(
          "Invalid PrintStream: "+ ps);
    
    output = ps;
    return this;
  }
  
  
  public PrintStream output() {
    return output;
  }
  
  
  public Printer threads(int ths) {
    if(isRunning())
      throw new IllegalStateException(
          "Printer is running. Stop it first.");
    if(ths <= 0 || ths > 100)
      throw new IllegalArgumentException(
          "Available Threads must be in {1 .. 100}");
    threads = ths;
    return this;
  }
  
  
  public int threads() {
    return threads;
  }
  
  
  public synchronized Printer print(String str) {
    if(str != null) {
      list.put(str); 
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  public Printer println(String str) {
    print(str);
    return print(LN);
  }
  
  
  public boolean isRunning() {
    return run.isTrue();
  }
  
  
  public Printer start() {
    run.set(true);
    exec = Executors.newFixedThreadPool(threads);
    exec.submit(()-> {
      while(run.isTrue()) {
        if(list.isEmpty())
          tryit(()->Thread.sleep(50));
        else
          output.print(list.pop());
      }
    });
    return this;
  }
  
  
  private void tryit(RunThrow tr) {
    if(tr == null) return;
    try { tr.run(); }
    catch(Exception e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public Printer stop() {
    run.set(false);
    exec.shutdown();
    list.clear();
    tryit(()->exec.awaitTermination(
        5, TimeUnit.SECONDS));
    return this;
  }
  
  
  @FunctionalInterface
  public static interface RunThrow {
    public void run() throws Exception;
  }
  
}
