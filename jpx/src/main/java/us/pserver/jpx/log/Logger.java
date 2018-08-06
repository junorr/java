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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class Logger {

  private static AtomicReference<Log> logger = new AtomicReference<>(StdLog.STDOUT);
  
  
  public static void setLog(Log log) {
    logger.set(Objects.requireNonNull(log));
  }
  
  public static Log getLog() {
    return logger.get();
  }
  
  public static void log(Log.Level lvl, String str, Object ... args) {
    logger.get().log(lvl, str, args);
  }
  
  public static void debug(String str, Object ... args) {
    logger.get().debug(str, args);
  }
  
  public static void info(String str, Object ... args) {
    logger.get().info(str, args);
  }
  
  public static void warn(String str, Object ... args) {
    logger.get().warn(str, args);
  }
  
  public static void error(String str, Object ... args) {
    logger.get().error(str, args);
  }
  
}
