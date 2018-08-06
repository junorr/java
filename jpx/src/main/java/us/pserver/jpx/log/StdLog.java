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

package us.pserver.jpx.log;

import java.io.PrintStream;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class StdLog implements Log {
  
  public static final String DEFAULT_FORMAT = "@LVL [@INS] @CLS.@MTH: @MSG";
  
  public static final LogFormat DEFAULT_LOG_FORMAT = new LogFormat(DEFAULT_FORMAT);
  
  
  public static final StdLog STDOUT = new StdLog(DEFAULT_LOG_FORMAT, System.out);
  
  public static final StdLog STDERR = new StdLog(DEFAULT_LOG_FORMAT, System.out);
  
  
  private final LogFormat lgf;
  
  private final PrintStream std;
  
  private final Level stl;
  
  public StdLog(LogFormat lf, PrintStream ps, Level startLevel) {
    std = ps;
    lgf = lf;
    stl = startLevel;
  }
  
  public StdLog(LogFormat lf, PrintStream ps) {
    this(lf, ps, null);
  }
  
  public void print(String str, Object ... args) {
    std.printf(Objects.requireNonNull(str), args);
  }
  
  public void println(String str, Object ... args) {
    std.printf(Objects.requireNonNull(str).concat("%n"), args);
  }
  
  public void print(Object obj) {
    std.print(Objects.toString(obj));
  }
  
  public void println(Object obj) {
    std.println(Objects.toString(obj));
  }
  
  @Override
  public void log(Level lvl, String str, Object ... args) {
    if(stl != null && lvl.ordinal() < stl.ordinal()) return;
    StackTraceElement elm = findPreviousElement(Thread.currentThread().getStackTrace());
    println(lgf.format(lvl,
        elm.getClassName(), 
        elm.getMethodName(), 
        elm.getLineNumber(), 
        args != null && args.length > 0 ? String.format(str, args) : str
    ));
  }
  
  @Override
  public void log(Level lvl, Throwable th, String str, Object ... args) {
    if(stl != null && lvl.ordinal() < stl.ordinal()) return;
    log(lvl, str, args);
    log(lvl, th);
  }
  
  @Override
  public void log(Level lvl, Throwable th) {
    if(stl != null && lvl.ordinal() < stl.ordinal()) return;
    log(lvl, th.toString());
    Throwable last = th;
    Throwable cause = th.getCause();
    while(cause != null) {
      log(lvl, "Root Cause: ", cause.toString());
      last = cause;
      cause = cause.getCause();
    }
    StackTraceElement[] els = last.getStackTrace();
    for(StackTraceElement e : els) {
      log(lvl, "  - %s", e.toString());
    }
  }
  
  private StackTraceElement findPreviousElement(StackTraceElement[] els) {
    int ic = -1;
    for(int i = 0; i < els.length; i++) {
      if(els[i].getClassName().equals(Logger.class.getName()) 
          || els[i].getClassName().equals(this.getClass().getName())) {
        ic = i+1;
      }
    }
    return ic >= 0 && ic < els.length ? els[ic] : null;
  }
  
}
