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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
   * <code>CONF_PACKAGE = "/us/pserver/log.properties";</code><br>
   * Default log internal properties.
   */
  public static final String CONF_PACKAGE = "/log/log.properties";
  
  /**
   * <code>CONF_PACKAGE_ALT = "/resources/log4j.properties";</code><br>
   * Default log internal properties.
   */
  public static final String CONF_PACKAGE_ALT = "/resources/log.properties";
  
  /**
   * <code>CONF_FILE = "./log.properties";</code><br>
   * Default log file properties.
   */
  public static final String CONF_FILE = "./log.properties";
  
  
	private static boolean CONFIGURED = false;
	
	//private static Properties props = null;
  
  
  /**
   * Configure Logger from a file or internal properties.
   */
  public static void configureLogger() {
		try {
			//props = new Properties();
			InputStream in = selectConfigStream();
			//props.load(in);
			//in.reset();
			LogManager.getLogManager().readConfiguration(in);
			CONFIGURED = true;
		} 
		catch(IOException e) {
			throw new RuntimeException(e);
		}
  }
	
	
	private static ByteArrayInputStream getBytes(InputStream in) throws IOException {
		byte[] bs = new byte[in.available()];
		in.read(bs);
		ByteArrayInputStream bin =  new ByteArrayInputStream(bs);
		bin.mark(bs.length +1);
		return bin;
	}
	
	
	public static InputStream selectConfigStream() throws IOException {
    Path path = Paths.get(CONF_FILE);
		ByteArrayInputStream bin = null;
		if(Files.exists(path)) {
			bin = getBytes(Files.newInputStream(path, 
					StandardOpenOption.READ
			));
		}
		else if(Logging.class.getResource(CONF_PACKAGE_ALT) != null) {
			bin = getBytes(Logging.class
					.getResourceAsStream(CONF_PACKAGE_ALT)
			);
		}
		else if(Logging.class.getResource(CONF_PACKAGE) != null) {
			bin = getBytes(Logging.class
					.getResourceAsStream(CONF_PACKAGE)
			);
		}
		else {
			bin = getBytes(Logging.class
					.getResourceAsStream(CONF_INTERNAL)
			);
		}
		return bin;
	}
	
	
	//public static String getProperty(String name) {
		//if(props == null) return null;
		//return props.getProperty(name);
	//}
	
	
	public static boolean isConfigured() {
		return CONFIGURED;
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
