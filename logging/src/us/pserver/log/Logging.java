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

package us.pserver.log;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utility class for configuring and get Logger.
 * @author Juno Roesler - juno@pserver.us
 */
public class Logging {

  /**
   * <code>CONF_INTERNAL = "/us/pserver/log/log.properties";</code><br>
   * Default log internal properties.
   */
  public static final String CONF_INTERNAL = "/us/pserver/log/log.properties";
  
  /**
   * <code>CONF_FILE = "./log.properties";</code><br>
   * Default log file properties.
   */
  public static final String CONF_FILE = "./log.properties";
  
  private static boolean CONFIGURED = false;
  
  
  /**
   * Configure Logger from a file or internal properties.
   */
  public static void configureLogger() {
    File f = new File(CONF_FILE);
    if(f.exists()) {
      PropertyConfigurator.configure(f.getAbsolutePath());
    }
    else {
      PropertyConfigurator.configure(Logging.class.getResourceAsStream(CONF_INTERNAL)
      );
    }
    CONFIGURED = true;
  }
  
  
  /**
   * Configure (if not) and get Logger for a given class.
   * @param cls The Logger class.
   * @return New LogHelper instance.
   */
  public static LogHelper getConfigured(Class cls) {
    if(!CONFIGURED) configureLogger();
    return logFor(cls);
  }
  
  
  /**
   * Configure (if not) and get Logger for a given name.
   * @param name The Logger name.
   * @return New LogHelper instance.
   */
  public static LogHelper getConfigured(String name) {
    if(!CONFIGURED) configureLogger();
    return logFor(name);
  }
  
  
  /**
   * Get a Logger for a given class.
   * @param cls The Logger class.
   * @return New LogHelper instance.
   */
  public static LogHelper logFor(Class cls) {
    return LogHelper.off(cls);
  }
  
  
  /**
   * Get a Logger for a given name.
   * @param name The Logger name.
   * @return New LogHelper instance.
   */
  public static LogHelper logFor(String name) {
    return LogHelper.off(name);
  }
  
}
