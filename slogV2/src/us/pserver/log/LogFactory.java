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

import java.util.Collection;
import java.util.Collections;
import us.pserver.log.impl.SimpleLog;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.impl.SLogV2;
import us.pserver.log.impl.LogCache;
import us.pserver.log.output.FileLogOutput;
import us.pserver.log.output.LogOutput;
import us.pserver.log.output.PrintStreamOutput;


/**
 * Factory of Log objects with caching.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
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
  
  private static final LogCache cache = new LogCache();
  
  
  /**
   * Defaultconstructor without arguments.
   */
  public LogFactory() {
    outputs = Collections.synchronizedMap(
        new HashMap<String, LogOutput>());
    formatter = OutputFormatterFactory.standardFormatter();
  }
  
  
  /**
   * Set the log output formatter.
   * @param fmt The log output formatter.
   * @return The modified <code>LogFactory</code> instance.
   */
  public LogFactory setOutputFormatter(OutputFormatter fmt) {
    if(fmt == null)
      throw new IllegalArgumentException("Invalid null OutputFormatter");
    formatter = fmt;
    return this;
  }
  
  
  /**
   * Get the log output formatter.
   * @return The log output formatter.
   */
  public OutputFormatter getOutputFormatter() {
    return formatter;
  }
  
  
  /**
   * Put a <code>LogOutput</code> for the creating log.
   * @param id Identification of the <code>LogOutput</code>.
   * @param out The <code>LogOutput</code> object.
   * @return The modified <code>LogFactory</code> instance.
   */
  public LogFactory put(String id, LogOutput out) {
    if(id != null && !id.trim().isEmpty() && out != null) {
      outputs.put(id, out);
    }
    return this;
  }
  
  
  /**
   * Get an immutable collection with all configured 
   * <code>LogOutput</code> objects.
   * @return An immutable collection with all configured 
   * <code>LogOutput</code> objects.
   */
  public Collection<LogOutput> outputs() {
    return Collections.synchronizedCollection(
        Collections.unmodifiableCollection(outputs.values()));
  }
  
  
  /**
   * Get a map with all configured 
   * <code>LogOutput</code> objects.
   * @return A map with all configured 
   * <code>LogOutput</code> objects.
   */
  public Map<String, LogOutput> outputsMap() {
    return outputs;
  }
  
  
  /**
   * Get the <code>LogOutput</code> identified by the given id.
   * @param id The <code>LogOutput</code> id.
   * @return The <code>LogOutput</code> or null.
   */
  public LogOutput getOutput(String id) {
    return outputs.get(id);
  }
  
  
  /**
   * Verify if this <code>LogFactory</code> instance has 
   * an output configured with the given id.
   * @param id The <code>LogOutput</code> id to be verified.
   * @return <code>true</code> if this <code>LogFactory</code> 
   * instance has an output configured with the given id, 
   * <code>false</code> otherwise.
   */
  public boolean containsOutput(String id) {
    return outputs.containsKey(id);
  }
  
  
  /**
   * Remove and return the <code>LogOutput</code> 
   * identified by the given id.
   * @param id The <code>LogOutput</code> id.
   * @return The <code>LogOutput</code> identified 
   * by the given id.
   */
  public LogOutput removeOutput(String id) {
    return outputs.remove(id);
  }
  
  
  /**
   * Create and cache a <code>Log</code> instance 
   * with the specified name.
   * @param name The <code>Log</code> name.
   * @return The created <code>Log</code> object.
   */
  public SimpleLog createSimpleLog(String name, boolean doCache) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Log name: '"+ name+ "'");
    SimpleLog log = new SimpleLog(name);
    outputs.keySet().forEach(k->log.put(k, outputs.get(k)));
    if(doCache) cache.put(name, log);
    return log;
  }
  
  
  /**
   * Create and cache a <code>Log</code> instance 
   * with the specified name.
   * @param name The <code>Log</code> name.
   * @return The created <code>Log</code> object.
   */
  public SLogV2 createSLogV2(String name, boolean doCache) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Log name: '"+ name+ "'");
    SLogV2 log = new SLogV2(name);
    outputs.keySet().forEach(k->log.put(k, outputs.get(k)));
    if(doCache) cache.put(name, log);
    return log;
  }
  

  /**
   * Resets all configurations of this 
   * <code>LogFactory</code> instance.
   * @return This modified <code>LogFactory</code> instance.
   */
  public LogFactory reset() {
    outputs.clear();
    formatter = OutputFormatterFactory.standardFormatter();
    return this;
  }
  
  
  /**
   * Create a <code>LogFactory</code> instance.
   * @return 
   */
  public static LogFactory factory() {
    return new LogFactory();
  }
  
  
  public static Log getCached(String name, boolean samePrefix) {
    if(name == null) return null;
    return (samePrefix
        ? cache.getWithSamePrefix(name)
        : cache.get(name));
  }
  
  
  public static boolean isCached(String name, boolean samePrefix) {
    if(name == null) return false;
    return (samePrefix 
        ? cache.containsWithSamePrefix(name)
        : cache.contains(name));
  }
  
  
  public static void putCached(String name, Log log) {
    cache.put(name, log);
  }
  
  
  public static Log getCached(Class cls, boolean samePrefix) {
    if(cls == null) return null;
    return (samePrefix
        ? cache.getWithSamePrefix(cls.getName())
        : cache.get(cls.getName()));
  }
  
  
  public static boolean isCached(Class cls, boolean samePrefix) {
    if(cls == null) return false;
    return (samePrefix 
        ? cache.containsWithSamePrefix(cls.getName())
        : cache.contains(cls.getName()));
  }
  
  
  public static void putCached(Class cls, Log log) {
    if(cls == null) return;
    cache.put(cls.getName(), log);
  }
  
  
  public static SimpleLog createDefaultSimpleLog(String name, boolean doCache) {
    if(name == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .createSimpleLog(name, doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(String name, boolean doCache) {
    if(name == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .createSLogV2(name, doCache);
  }
  
  
  public static SimpleLog createDefaultSimpleLog(String name, String logfile, boolean doCache) {
    if(name == null || logfile == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.DEBUG, false)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .put(ID_FILE_OUTPUT, new FileLogOutput(logfile)
            .setAllLevelsEnabled(true))
        .createSimpleLog(name, doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(String name, String logfile, boolean doCache) {
    if(name == null || logfile == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.DEBUG, false)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .put(ID_FILE_OUTPUT, new FileLogOutput(logfile)
            .setAllLevelsEnabled(true))
        .createSLogV2(name, doCache);
  }
  
  
  public static SimpleLog createDefaultSimpleLog(Class cls, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSimpleLog(cls.getName(), doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(Class cls, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSLogV2(cls.getName(), doCache);
  }
  
  
  public static SimpleLog createDefaultSimpleLog(Class cls, String logfile, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSimpleLog(cls.getName(), logfile, doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(Class cls, String logfile, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSLogV2(cls.getName(), logfile, doCache);
  }
  
  
  public static Log getOrCreateSimpleLog(String name, boolean samePrefix) {
    if(name == null) return null;
    if(cache.contains(name)) {
      return cache.get(name);
    }
    if(samePrefix && cache.containsWithSamePrefix(name)) {
      return cache.copyOutputsForSamePrefix(name, new SimpleLog(name));
    }
    else {
      return createDefaultSimpleLog(name, true);
    }
  }
  
  
  public static Log getOrCreateSLogV2(String name, boolean samePrefix) {
    if(name == null) return null;
    if(cache.contains(name)) {
      return cache.get(name);
    }
    if(samePrefix && cache.containsWithSamePrefix(name)) {
      return cache.copyOutputsForSamePrefix(name, new SLogV2(name));
    }
    else {
      return createDefaultSLogV2(name, true);
    }
  }
  
  
  public static Log getOrCreateSimpleLog(String name, String logfile, boolean samePrefix) {
    if(name == null || logfile == null) return null;
    if(cache.contains(name)) {
      return cache.get(name);
    }
    if(samePrefix && cache.containsWithSamePrefix(name)) {
      Log log = cache.copyOutputsForSamePrefix(name, new SimpleLog(name));
      Optional<LogOutput> opt = log.outputs().stream().filter(
          o->(o instanceof FileLogOutput)).findFirst();
      if(!opt.isPresent()) {
        log.put(ID_FILE_OUTPUT, new FileLogOutput(logfile)
            .setAllLevelsEnabled(true));
      }
      return log;
    }
    else {
      return createDefaultSimpleLog(name, logfile, true);
    }
  }
  
  
  public static Log getOrCreateSLogV2(String name, String logfile, boolean samePrefix) {
    if(name == null || logfile == null) return null;
    if(cache.contains(name)) {
      return cache.get(name);
    }
    if(samePrefix && cache.containsWithSamePrefix(name)) {
      Log log = cache.copyOutputsForSamePrefix(name, new SLogV2(name));
      Optional<LogOutput> opt = log.outputs().stream().filter(
          o->(o instanceof FileLogOutput)).findFirst();
      if(!opt.isPresent()) {
        log.put(ID_FILE_OUTPUT, new FileLogOutput(logfile)
            .setAllLevelsEnabled(true));
      }
      return log;
    }
    else {
      return createDefaultSLogV2(name, logfile, true);
    }
  }
  

  public static Log getOrCreateSimpleLog(Class cls, boolean samePrefix) {
    if(cls == null) return null;
    return getOrCreateSimpleLog(cls.getName(), samePrefix);
  }
  
  
  public static Log getOrCreateSLogV2(Class cls, boolean samePrefix) {
    if(cls == null) return null;
    return getOrCreateSLogV2(cls.getName(), samePrefix);
  }
  
  
  public static Log getOrCreateSimpleLog(Class cls, String logfile, boolean samePrefix) {
    if(cls == null) return null;
    return getOrCreateSimpleLog(cls.getName(), logfile, samePrefix);
  }
  
  
  public static Log getOrCreateSLogV2(Class cls, String logfile, boolean samePrefix) {
    if(cls == null) return null;
    return getOrCreateSLogV2(cls.getName(), logfile, samePrefix);
  }
  
}