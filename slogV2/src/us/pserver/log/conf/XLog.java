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

package us.pserver.log.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import us.pserver.log.Log;
import us.pserver.log.LogLevel;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.PatternOutputFormatter;
import us.pserver.log.impl.LogLevels;
import us.pserver.log.impl.SimpleLog;
import us.pserver.log.output.LogOutput;
import us.pserver.tools.Reflector;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/08/2015
 */
public class XLog {
  
  private static final String default_pattern = 
      "{DATE}  [{LEVEL}]  {NAME}:{LINE} - {MESSAGE}";

  private List<XOutput> outputs;
  
  private boolean enabled;
  
  private Class<? extends Log> type;
  
  private String name;
  
  private String pattern;
  
  private LogLevels levels;
  
  
  public XLog() {
    outputs = new ArrayList<XOutput>();
    enabled = true;
    name = null;
    type = SimpleLog.class;
    pattern = default_pattern;
    levels = new LogLevels();
  }
  
  
  public LogLevels getLevels() {
    return levels;
  }
  
  
  public List<XOutput> getOutputs() {
    return outputs;
  }
  
  
  public boolean isEnabled() {
    return enabled;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public String getPattern() {
    return pattern;
  }
  
  
  public Class<? extends Log> getType() {
    return type;
  }
  
  
  public XLog setType(Class<? extends Log> cls) {
    this.type = Valid.off(cls).forNull().getOrFail(Class.class);
    return this;
  }
  
  
  public XLog setName(String nm) {
    name = Valid.off(nm).forEmpty().getOrFail("Invalid Name: ");
    return this;
  }
  
  
  public XLog setPattern(String ptn) {
    pattern = Valid.off(ptn).forEmpty().getOrFail("Invalid Pattern: ");
    return this;
  }
  
  
  public XLog setEnabled(boolean enb) {
    enabled = enb;
    return this;
  }
  
  
  public static XLog from(Log log) {
    Valid.off(log).forNull().fail(Log.class);
    XLog xl = new XLog();
    xl.setEnabled(true)
        .setName(log.getLogName())
        .setType(log.getClass());
    xl.getLevels().setLevelEnabled(
        LogLevel.DEBUG, 
        log.isLevelEnabled(LogLevel.DEBUG)
    );
    xl.getLevels().setLevelEnabled(
        LogLevel.INFO, 
        log.isLevelEnabled(LogLevel.INFO)
    );
    xl.getLevels().setLevelEnabled(
        LogLevel.WARN, 
        log.isLevelEnabled(LogLevel.WARN)
    );
    xl.getLevels().setLevelEnabled(
        LogLevel.ERROR, 
        log.isLevelEnabled(LogLevel.ERROR)
    );
    for(Map.Entry<String,LogOutput> e : log.outputsMap().entrySet()) {
      xl.outputs.add(XOutput.from(e.getKey(), e.getValue()));
    }
    return xl;
  }
  
  
  public Log create() throws Exception {
    Reflector ref = new Reflector();
    Log log = (Log) ref.on(type)
        .constructor(String.class, OutputFormatter.class)
        .create(this.getName(), 
            new PatternOutputFormatter(this.getPattern())
        );
    if(ref.hasError())
      throw (Exception) ref.getError();
    
    LogLevel lv = LogLevel.DEBUG;
    log.setLevelEnabled(
        lv, this.getLevels().isLevelEnabled(lv)
    );
    lv = LogLevel.INFO;
    log.setLevelEnabled(
        lv, this.getLevels().isLevelEnabled(lv)
    );
    lv = LogLevel.WARN;
    log.setLevelEnabled(
        lv, this.getLevels().isLevelEnabled(lv)
    );
    lv = LogLevel.ERROR;
    log.setLevelEnabled(
        lv, this.getLevels().isLevelEnabled(lv)
    );
    for(XOutput o : outputs) {
      log.put(o.getName(), o.create());
    }
    return log;
  }
  
}
