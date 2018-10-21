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

package us.pserver.tools.log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class Logger {

  public static final List<Log> LOGS = new CopyOnWriteArrayList<>(Arrays.asList(StdLog.STDOUT, StdLog.STDERR));
  
  public static final List<Log.Level> LEVELS = new CopyOnWriteArrayList<>(Arrays.asList(
      Log.Level.DEBUG, 
      Log.Level.INFO, 
      Log.Level.WARN, 
      Log.Level.ERROR)
  );
  
  public static void log(Log.Level lvl, String str, Object ... args) {
    if(LEVELS.contains(lvl)) {
      LOGS.forEach(l->l.log(lvl, str, args));
    }
  }
  
  public static void log(Log.Level lvl, Throwable th, String str, Object ... args) {
    if(LEVELS.contains(lvl)) {
      LOGS.forEach(l->l.log(lvl, th, str, args));
    }
  }
  
  public static void log(Log.Level lvl, Throwable th) {
    if(LEVELS.contains(lvl)) {
      LOGS.forEach(l->l.log(lvl, th));
    }
  }
  
  public static void debug(String str, Object ... args) {
    log(Log.Level.DEBUG,str, args);
  }
  
  public static void debug(Throwable th, String str, Object ... args) {
    log(Log.Level.DEBUG, th, str, args);
  }
  
  public static void debug(Throwable th) {
    log(Log.Level.DEBUG, th);
  }
  
  public static void info(String str, Object ... args) {
    log(Log.Level.INFO, str, args);
  }
  
  public static void info(Throwable th, String str, Object ... args) {
    log(Log.Level.INFO, th, str, args);
  }
  
  public static void info(Throwable th) {
    log(Log.Level.INFO, th);
  }
  
  public static void warn(String str, Object ... args) {
    log(Log.Level.WARN, str, args);
  }
  
  public static void warn(Throwable th) {
    log(Log.Level.WARN, th);
  }
  
  public static void warn(Throwable th, String str, Object ... args) {
    log(Log.Level.WARN, th, str, args);
  }
  
  public static void error(String str, Object ... args) {
    log(Log.Level.ERROR, str, args);
  }
  
  public static void error(Throwable th, String str, Object ... args) {
    log(Log.Level.ERROR, th, str, args);
  }
  
  public static void error(Throwable th) {
    log(Log.Level.ERROR, th);
  }
  
}
