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

package us.pserver.log.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import us.pserver.log.Log;
import static us.pserver.log.Log.interpolate;
import us.pserver.log.LogLevel;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.format.PatternOutputFormatter;
import us.pserver.log.internal.LogLevelManager;
import us.pserver.log.output.LogOutput;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/06/2015
 */
public abstract class AbstractLog implements Log {
  
  /**
   * <code>
   *  CONTINUE_LOG_PATTERN = "{DATE}        |- {MESSAGE}";
   * </code><br>
   * The continue log string pattern.
   */
  public static final String CONTINUE_LOG_PATTERN = "{DATE}        |- {MESSAGE}";
  
  
  Map<String, LogOutput> outputs;
  
  LogLevelManager levels;
  
  String logname;
  
  OutputFormatter formatter;
  
  OutputFormatter continueFormat;
  
  
  AbstractLog() {
    outputs = Collections.synchronizedMap(
        new HashMap<String, LogOutput>());
    levels = new LogLevelManager();
    formatter = OutputFormatterFactory.standardFormatter();
    continueFormat = new PatternOutputFormatter(CONTINUE_LOG_PATTERN);
    logname = null;
  }
  
  
  AbstractLog(String name) {
    this();
    if(name == null || name.trim().isEmpty())
      throw new IllegalArgumentException("Invalid log name: '"+ name+ "'");
    logname = name;
  }


  @Override
  public Log put(String id, LogOutput out) {
    if(id != null 
        && !id.trim().isEmpty()
        && out != null) {
      outputs.put(id, out);
    }
    return this;
  }


  @Override
  public Log clearOutputs() {
    outputs.values().forEach(o->o.close());
    outputs.clear();
    return this;
  }


  @Override
  public Map<String, LogOutput> outputsMap() {
    return outputs;
  }
  
  
  @Override
  public Collection<LogOutput> outputs() {
    Collection<LogOutput> outs = Collections.synchronizedCollection(
        Collections.unmodifiableCollection(outputs.values()));
    return outs;
  }


  @Override
  public LogOutput get(String id) {
    return outputs.get(id);
  }


  @Override
  public LogOutput remove(String id) {
    return outputs.remove(id);
  }


  @Override
  public boolean contains(String id) {
    return outputs.containsKey(id);
  }
  
  
  @Override
  public Map<LogLevel, Boolean> levelsMap() {
    return levels.levels();
  }


  @Override
  public String getLogName() {
    return logname;
  }


  @Override
  public Log setOutputFormatter(OutputFormatter fmt) {
    if(fmt == null)
      throw new IllegalArgumentException("Invalid null OutputFormatter");
    formatter = fmt;
    return this;
  }


  @Override
  public OutputFormatter getOutputFormatter() {
    return formatter;
  }


  @Override
  public Log setLevelEnabled(LogLevel lvl, boolean enabled) {
    if(lvl != null) {
      levels.setLevelEnabled(lvl, enabled);
    }
    return this;
  }


  @Override
  public Log setAllLevelsEnabled(boolean enabled) {
    levels.setAllLevelsEnabled(enabled);
    return this;
  }


  @Override
  public Log debug(String msg) {
    return log(LogLevel.DEBUG, msg);
  }


  @Override
  public Log debug(String msg, Object... args) {
    return log(LogLevel.DEBUG, msg, args);
  }


  @Override
  public Log debug(Throwable th, boolean logStackTrace) {
    return log(LogLevel.DEBUG, th, logStackTrace);
  }


  @Override
  public Log info(String msg) {
    return log(LogLevel.INFO, msg);
  }


  @Override
  public Log info(String msg, Object... args) {
    return log(LogLevel.INFO, msg, args);
  }


  @Override
  public Log info(Throwable th, boolean logStackTrace) {
    return log(LogLevel.INFO, th, logStackTrace);
  }


  @Override
  public Log warn(String msg) {
    return log(LogLevel.WARN, msg);
  }


  @Override
  public Log warn(String msg, Object... args) {
    return log(LogLevel.WARN, msg, args);
  }


  @Override
  public Log warn(Throwable th, boolean logStackTrace) {
    return log(LogLevel.WARN, th, logStackTrace);
  }


  @Override
  public Log error(String msg) {
    return log(LogLevel.ERROR, msg);
  }


  @Override
  public Log error(String msg, Object... args) {
    return log(LogLevel.ERROR, msg, args);
  }


  @Override
  public Log error(Throwable th, boolean logStackTrace) {
    return log(LogLevel.ERROR, th, logStackTrace);
  }


  @Override
  public Log log(LogLevel lvl, String msg, Object ... args) {
    if(levels.isLevelEnabled(lvl)) {
      String imsg = interpolate(msg, args);
      log(lvl, imsg);
    }
    return this;
  }
  
  
  @Override
  public Log close() {
    outputs.values().forEach(o->o.close());
    outputs.clear();
    return this;
  }

}
