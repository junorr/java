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

import java.nio.file.Path;
import java.util.Collections;
import us.pserver.log.impl.SimpleLog;
import java.util.HashMap;
import java.util.Map;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.impl.SLogV2;
import us.pserver.log.output.FileLogOutput;
import us.pserver.log.output.LogOutput;
import us.pserver.log.output.PrintStreamOutput;


public class LogFactory {
  
  /**
   * <code>
   *  ID_STD_OUTPUT = "stdout";
   * </code><br>
   * <code>LogOutput</code> ID for system standard output.
   */
  public static final String ID_STD_OUTPUT = "stdout";
  
  /**
   * <code>
   *  ID_STDERR_OUTPUT = "stdout";
   * </code><br>
   * <code>LogOutput</code> ID for system error output.
   */
  public static final String ID_STDERR_OUTPUT = "stderr";

  /**
   * <code>
   *  ID_FILE_OUTPUT = "stdout";
   * </code><br>
   * <code>LogOutput</code> ID for file output.
   */
  public static final String ID_FILE_OUTPUT = "file";
  

  private final Map<String, LogOutput> outputs;
  
  private OutputFormatter formatter;
  
  private static final Map<String, Log> cache = 
      Collections.synchronizedMap(new HashMap<String, Log>());
  
  
  public LogFactory() {
    outputs = Collections.synchronizedMap(
        new HashMap<String, LogOutput>());
    formatter = OutputFormatterFactory.standardFormatter();
  }
  
  
  public LogFactory formatter(OutputFormatter fmt) {
    if(fmt == null)
      throw new IllegalArgumentException("Invalid null OutputFormatter");
    formatter = fmt;
    return this;
  }
  
  
  public OutputFormatter getOutputFormatter() {
    return formatter;
  }
  
  
  public LogFactory put(String id, LogOutput out) {
    if(id != null && !id.trim().isEmpty() && out != null) {
      outputs.put(id, out);
    }
    return this;
  }
  
  
  public Map<String, LogOutput> outputs() {
    return outputs;
  }
  
  
  public LogOutput get(String id) {
    return outputs.get(id);
  }
  
  
  public boolean containsOutput(String id) {
    return outputs.containsKey(id);
  }
  
  
  public LogOutput remove(String id) {
    return outputs.remove(id);
  }
  
  
  public Log createSimpleLogger(String name) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    Log log = new SimpleLog(name);
    outputs.keySet().forEach(k->log.put(k, outputs.get(k)));
    cache.put(name, log);
    return log;
  }
  
  
  public Log createSLogV2(String name) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    Log log = new SLogV2(name);
    outputs.keySet().forEach(k->log.put(k, outputs.get(k)));
    cache.put(name, log);
    return log;
  }
  
  
  public LogFactory reset() {
    outputs.clear();
    formatter = OutputFormatterFactory.standardFormatter();
    return this;
  }
  
  
  public static LogFactory factory() {
    return new LogFactory();
  }
  
  
  public static Log getSimpleLogger(String name) {
    return getSimpleLogger(name, null);
  }
  
  
  public static Log getSimpleLogger(String name, Path logfile) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    if(!cache.containsKey(name)) {
      LogOutput file = null;
      if(logfile != null) {
        file = new FileLogOutput(logfile);
      }
      LogOutput std = new PrintStreamOutput(()->System.out)
          .setLevelEnabled(LogLevel.ERROR, false);
      LogOutput err = new PrintStreamOutput(()->System.err)
          .setAllLevelsEnabled(false)
          .setLevelEnabled(LogLevel.ERROR, true);
      factory().put(ID_STD_OUTPUT, std)
          .put(ID_STDERR_OUTPUT, err)
          .put(ID_FILE_OUTPUT, file)
          .createSimpleLogger(name);
    }
    return cache.get(name);
  }
  
  
  public static Log getSimpleLogger(Class cls) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return getSimpleLogger(cls.getName());
  }
  
  
  public static Log getSimpleLogger(Class cls, Path logfile) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return getSimpleLogger(cls.getName(), logfile);
  }
  
  
  public static SLogV2 getSLogV2(String name) {
    return getSLogV2(name, null);
  }
  
  
  public static SLogV2 getSLogV2(String name, Path logfile) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    if(!cache.containsKey(name)) {
      LogOutput file = null;
      if(logfile != null) {
        file = new FileLogOutput(logfile);
      }
      LogOutput std = new PrintStreamOutput(()->System.out)
          .setLevelEnabled(LogLevel.ERROR, false);
      LogOutput err = new PrintStreamOutput(()->System.err)
          .setAllLevelsEnabled(false)
          .setLevelEnabled(LogLevel.ERROR, true);
      factory().put(ID_STD_OUTPUT, std)
          .put(ID_STDERR_OUTPUT, err)
          .put(ID_FILE_OUTPUT, file)
          .createSLogV2(name);
    }
    return (SLogV2) cache.get(name);
  }
  
  
  public static SLogV2 getSLogV2(Class cls) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return LogFactory.getSLogV2(cls.getName());
  }
  

  public static Log getSLogV2(Class cls, Path logfile) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return getSLogV2(cls.getName(), logfile);
  }
  
}