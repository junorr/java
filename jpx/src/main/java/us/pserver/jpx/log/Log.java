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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public interface Log {

  public static enum Level {
    DEBUG("DEBUG"), INFO("INFO "), WARN("WARN "), ERROR("ERROR");
    private Level(String lvl) {
      this.lvl = lvl;
    }
    private final String lvl;
    public String toString() {
      return lvl;
    }
  }
  
  public void log(Level lvl, String str, Object ... args);
  
  public void log(Level lvl, Throwable th, String str, Object ... args);
  
  public void log(Level lvl, Throwable th);
  
  public default void debug(String str, Object ... args) {
    log(Level.DEBUG, str, args);
  }
  
  public default void debug(Throwable th, String str, Object ... args) {
    log(Level.DEBUG, th, str, args);
  }
  
  public default void debug(Throwable th) {
    log(Level.DEBUG, th);
  }
  
  public default void info(String str, Object ... args) {
    log(Level.INFO, str, args);
  }
  
  public default void info(Throwable th, String str, Object ... args) {
    log(Level.INFO, th, str, args);
  }
  
  public default void info(Throwable th) {
    log(Level.INFO, th);
  }
  
  public default void warn(String str, Object ... args) {
    log(Level.WARN, str, args);
  }
  
  public default void warn(Throwable th, String str, Object ... args) {
    log(Level.WARN, th, str, args);
  }
  
  public default void warn(Throwable th) {
    log(Level.WARN, th);
  }
  
  public default void error(String str, Object ... args) {
    log(Level.ERROR, str, args);
  }
  
  public default void error(Throwable th, String str, Object ... args) {
    log(Level.ERROR, th, str, args);
  }
  
  public default void error(Throwable th) {
    log(Level.ERROR, th);
  }
  
}
