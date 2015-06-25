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
import java.util.Collection;
import java.util.Collections;
import us.pserver.log.impl.SimpleLog;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.impl.SLogV2;
import us.pserver.log.internal.LogCache;
import us.pserver.log.output.FileLogOutput;
import us.pserver.log.output.LogOutput;
import us.pserver.log.output.PrintStreamOutput;


/**
 * Factory of Log objects with caching.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class LogFactory2 {
  
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
  public LogFactory2() {
    outputs = Collections.synchronizedMap(
        new HashMap<String, LogOutput>());
    formatter = OutputFormatterFactory.standardFormatter();
  }
  
  
  /**
   * Set the log output formatter.
   * @param fmt The log output formatter.
   * @return The modified <code>LogFactory</code> instance.
   */
  public LogFactory2 setOutputFormatter(OutputFormatter fmt) {
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
  public LogFactory2 put(String id, LogOutput out) {
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
  public LogOutput get(String id) {
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
    outputs.entrySet().forEach(e->log.put(e.getKey(), e.getValue()));
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
    outputs.entrySet().forEach(e->log.put(e.getKey(), e.getValue()));
    if(doCache) cache.put(name, log);
    return log;
  }
  

  /**
   * Resets all configurations of this 
   * <code>LogFactory</code> instance.
   * @return This modified <code>LogFactory</code> instance.
   */
  public LogFactory2 reset() {
    outputs.clear();
    formatter = OutputFormatterFactory.standardFormatter();
    return this;
  }
  
  
  /**
   * Create a <code>LogFactory</code> instance.
   * @return 
   */
  public static LogFactory2 factory() {
    return new LogFactory2();
  }
  
  
  public static Log getCachedLog(String name, boolean samePrefix) {
    if(name == null) return null;
    if(samePrefix && !cache.containsWithSamePrefix(name))
      return null;
    else if(!samePrefix && !cache.contains(name))
      return null;
    return (samePrefix 
        ? cache.getWithSamePrefix(name) 
        : cache.get(name));
  }
  
  
  public static SimpleLog createDefaultSimpleLog(String name, boolean doCache) {
    if(name == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.DEBUG, false)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .createSimpleLog(name, doCache);
  }
  
  
  public static SimpleLog createDefaultSimpleLog(String name, String logfile, boolean doCache) {
    if(name == null) return null;
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
  
  
  public static SLogV2 createDefaultSLogV2(String name, boolean doCache) {
    if(name == null) return null;
    return factory()
        .put(ID_STD_OUTPUT, new PrintStreamOutput(System.out)
            .setAllLevelsEnabled(true)
            .setLevelEnabled(LogLevel.DEBUG, false)
            .setLevelEnabled(LogLevel.ERROR, false))
        .put(ID_STDERR_OUTPUT, new PrintStreamOutput(System.err)
            .setAllLevelsEnabled(false)
            .setLevelEnabled(LogLevel.ERROR, true))
        .createSLogV2(name, doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(String name, String logfile, boolean doCache) {
    if(name == null) return null;
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
  
  
  public static SimpleLog createDefaultSimpleLog(Class cls, String logfile, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSimpleLog(cls.getName(), logfile, doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(Class cls, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSLogV2(cls.getName(), doCache);
  }
  
  
  public static SLogV2 createDefaultSLogV2(Class cls, String logfile, boolean doCache) {
    if(cls == null) return null;
    return createDefaultSLogV2(cls.getName(), logfile, doCache);
  }
  
  
  public static Log getOrCreateSimpleLog(String name, boolean samePrefix) {
    if(samePrefix) {
      if(cache.containsWithSamePrefix(name))
        return cache.getWithSamePrefix(name);
      else
        return createDefaultSimpleLog(name, true);
    }
    else {
      if(cache.contains(name))
        return cache.get(name);
      else
        return createDefaultSimpleLog(name, true);
    }
  }
  
  
  /**
   * Get a cached instance of <code>Log</code> with 
   * (or starting with) the given name.
   * For example, suppose we have a cached <code>Log</code>
   * instance cached with the name <code>com.mysite.mypackage</code>
   * and we call this method with the name 
   * <code>com.mysite.mypackage.myclass</code>,
   * the returned <code>Log</code> instance will be configured 
   * in the same way of this 'ancestor'
   * @param name The name of the <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getCached(String name) {
    if(name == null || name.trim().isEmpty())
      return null;
    
    Optional<String> equal = cache.keySet().stream().filter(s->
        name.equalsIgnoreCase(s)).findFirst();
    if(equal.isPresent()) 
      return cache.get(equal.get());
    
    Optional<String> similar = cache.keySet().stream().filter(s->
        name.startsWith(s)).findFirst();
    if(!similar.isPresent()) return null;
      
    Log lsim = cache.get(similar.get());
    Log log = (SLogV2.class.isAssignableFrom(lsim.getClass()) 
        ? new SLogV2(name) : new SimpleLog(name));
    log.clearOutputs();
    lsim.outputsMap().entrySet().forEach(e->
        log.put(e.getKey(), e.getValue()));
    lsim.levelsMap().entrySet().forEach(e->
        log.setLevelEnabled(e.getKey(), e.getValue()));
    cache.put(name, log);
    return log;
  }
  
  
  /**
   * Get a cached instance of <code>Log</code> with 
   * (or starting with) the given class.
   * For example, suppose we have a cached <code>Log</code>
   * instance cached with the name <code>com.mysite.mypackage</code>
   * and we call this method with the class 
   * <code>com.mysite.mypackage.myclass</code>,
   * the returned <code>Log</code> instance will be configured 
   * in the same way of this 'ancestor'
   * @param cls The class identifying the <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getCached(Class cls) {
    if(cls != null) 
      return getCached(cls.getName());
    return null;
  }
  
  
  /**
   * Put a <code>Log</code> instance in the cache with
   * the specified name.
   * @param name The identifying name of the <code>Log</code> instance.
   * @param log The <code>Log</code> instance to be cached.
   */
  public static void putCached(String name, Log log) {
    if(name != null 
        && !name.trim().isEmpty() 
        && log != null) {
      cache.put(name, log);
    }
  }
  
  
  /**
   * Verify if the <code>LogFactory</code> contains a cached 
   * <code>Log</code> instance with (or starting with) the given name.
   * For example, suppose we have a cached <code>Log</code>
   * instance cached with the name <code>com.mysite.mypackage</code>
   * and we call this method with the name 
   * <code>com.mysite.mypackage.myclass</code>, the return will 
   * be <code>true</code>.
   * @param name The name of the <code>Log</code> instance.
   * @return <code>true</code> if the cache has an <code>Log</code>
   * intance with (or starting with) the given name.
   */
  public static boolean isCached(String name) {
    if(name != null && !name.trim().isEmpty()) {
      return cache.keySet().stream().filter(s->
          s.equalsIgnoreCase(name) || name.startsWith(s))
          .findFirst().isPresent();
    }
    return false;
  }
  
  
  /**
   * Verify if the <code>LogFactory</code> contains a cached 
   * <code>Log</code> instance with (or starting with) the given class name.
   * For example, suppose we have a cached <code>Log</code>
   * instance cached with the name <code>com.mysite.mypackage</code>
   * and we call this method with the class name 
   * <code>com.mysite.mypackage.myclass</code>, the return will 
   * be <code>true</code>.
   * @param name The name of the <code>Log</code> instance.
   * @return <code>true</code> if the cache has an <code>Log</code>
   * intance with (or starting with) the given class name.
   */
  public static boolean isCached(Class cls) {
    if(cls == null) return false;
    return isCached(cls.getName());
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given name.
   * @param name The <code>Log</code> name.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getSimpleLog(String name) {
    return LogFactory2.getSimpleLog(name, null);
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given name, configured with the specified file log output.
   * @param name The <code>Log</code> name.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getSimpleLog(String name, Path logfile) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    
    if(isCached(name)) {
      Log log = getCached(name);
      boolean hasFileLog = log.outputs().stream().filter(o->
          FileLogOutput.class.isAssignableFrom(log.getClass()))
          .findFirst().isPresent();
      if(!hasFileLog && logfile != null)
        log.put(ID_FILE_OUTPUT, new FileLogOutput(logfile));
      cache.put(name, log);
    }
    else {
      LogOutput file = null;
      if(logfile != null) {
        file = new FileLogOutput(logfile)
            .setAllLevelsEnabled(true);
      }
      LogOutput std = new PrintStreamOutput(System.out)
          .setLevelEnabled(LogLevel.ERROR, false)
          .setLevelEnabled(LogLevel.DEBUG, false);
      LogOutput err = new PrintStreamOutput(System.err)
          .setAllLevelsEnabled(false)
          .setLevelEnabled(LogLevel.ERROR, true);
      factory().put(ID_STD_OUTPUT, std)
          .put(ID_STDERR_OUTPUT, err)
          .put(ID_FILE_OUTPUT, file)
          .createSimpleLogger(name);
    }
    return cache.get(name);
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given class name.
   * @param name The identifying class for the 
   * <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getSimpleLog(Class cls) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return getSimpleLog(cls.getName());
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given class name, configured with the 
   * specified file log output.
   * @param name The identifying class for the 
   * <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getSimpleLog(Class cls, Path logfile) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return LogFactory2.getSimpleLog(cls.getName(), logfile);
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given name.
   * @param name The <code>Log</code> name.
   * @return The cached or created <code>Log</code> instance.
   */
  public static SLogV2 getSLogV2(String name) {
    return getSLogV2(name, null);
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given name, configured with the specified file log output.
   * @param name The <code>Log</code> name.
   * @return The cached or created <code>Log</code> instance.
   */
  public static SLogV2 getSLogV2(String name, Path logfile) {
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid Logger name: '"+ name+ "'");
    
    if(isCached(name)) {
      Log log = getCached(name);
      if(!SLogV2.class.isAssignableFrom(log.getClass())) {
        SLogV2 slog = new SLogV2(name);
        slog.clearOutputs();
        log.outputsMap().entrySet().forEach(e->
            slog.put(e.getKey(), e.getValue()));
        log.levelsMap().entrySet().forEach(e->
            slog.setLevelEnabled(e.getKey(), e.getValue()));
        cache.put(name, slog);
      }
      boolean hasFileLog = log.outputs().stream().filter(o->
          FileLogOutput.class.isAssignableFrom(o.getClass()))
          .findFirst().isPresent();
      if(!hasFileLog && logfile != null)
        log.put(ID_FILE_OUTPUT, new FileLogOutput(logfile)
            .setAllLevelsEnabled(true));
    }
    else {
      LogOutput file = null;
      if(logfile != null) {
        file = new FileLogOutput(logfile)
            .setAllLevelsEnabled(true);
      }
      LogOutput std = new PrintStreamOutput(System.out)
          .setLevelEnabled(LogLevel.ERROR, false)
          .setLevelEnabled(LogLevel.DEBUG, false);
      LogOutput err = new PrintStreamOutput(System.err)
          .setAllLevelsEnabled(false)
          .setLevelEnabled(LogLevel.ERROR, true);
      factory().put(ID_STD_OUTPUT, std)
          .put(ID_STDERR_OUTPUT, err)
          .put(ID_FILE_OUTPUT, file)
          .createSLogV2(name);
    }
    return (SLogV2) cache.get(name);
  }
  
  
  /**
   * Get a cached or created <code>Log</code> for
   * the given class name.
   * @param name The identifying class for the 
   * <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static SLogV2 getSLogV2(Class cls) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return LogFactory2.getSLogV2(cls.getName());
  }
  

  /**
   * Get a cached or created <code>Log</code> for
   * the given class name, configured with the 
   * specified file log output.
   * @param name The identifying class for the 
   * <code>Log</code> instance.
   * @return The cached or created <code>Log</code> instance.
   */
  public static Log getSLogV2(Class cls, Path logfile) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    return getSLogV2(cls.getName(), logfile);
  }
  
}