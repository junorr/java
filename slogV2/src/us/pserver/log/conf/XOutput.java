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

import us.pserver.log.LogLevel;
import us.pserver.log.impl.LogLevels;
import us.pserver.log.output.LogOutput;
import us.pserver.tools.Reflector;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/08/2015
 */
public class XOutput {
  
  private String name;
  
  private Class type;
  
  private LogLevels levels;
  
  
  public XOutput() {
    name = null;
    type = null;
    levels = new LogLevels();
  }
  
  
  public XOutput(String name, Class<? extends LogOutput> cls) {
    this.name = Valid.off(name).forEmpty().getOrFail();
    this.type = Valid.off(cls).forNull().getOrFail(Class.class);
    levels = new LogLevels();
  }


  public String getName() {
    return name;
  }


  public Class getType() {
    return type;
  }


  public LogLevels getLevels() {
    return levels;
  }
  
  
  public static XOutput from(String name, LogOutput out) {
    XOutput xo = new XOutput(name, out.getClass());
    xo.levels.setLevelEnabled(
        LogLevel.DEBUG, 
        out.isLevelEnabled(LogLevel.DEBUG)
    );
    xo.levels.setLevelEnabled(
        LogLevel.INFO, 
        out.isLevelEnabled(LogLevel.INFO)
    );
    xo.levels.setLevelEnabled(
        LogLevel.WARN, 
        out.isLevelEnabled(LogLevel.WARN)
    );
    xo.levels.setLevelEnabled(
        LogLevel.ERROR, 
        out.isLevelEnabled(LogLevel.ERROR)
    );
    return xo;
  }
  
  
  public LogOutput create() throws Exception {
    Reflector ref = new Reflector();
    LogOutput out = (LogOutput)
        ref.on(type).create();
    if(ref.hasError())
      throw (Exception) ref.getError();
    
    LogLevel lv = LogLevel.DEBUG;
    out.levels().setLevelEnabled(
        lv, levels.isLevelEnabled(lv)
    );
    lv = LogLevel.INFO;
    out.levels().setLevelEnabled(
        lv, levels.isLevelEnabled(lv)
    );
    lv = LogLevel.WARN;
    out.levels().setLevelEnabled(
        lv, levels.isLevelEnabled(lv)
    );
    lv = LogLevel.ERROR;
    out.levels().setLevelEnabled(
        lv, levels.isLevelEnabled(lv)
    );
    return out;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
    hash = 83 * hash + (this.type != null ? this.type.hashCode() : 0);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final XOutput other = (XOutput) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "XOutput{" + "name=" + name + ", type=" + type + ", levels=" + levels + '}';
  }
  
}
