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

import java.util.Arrays;
import java.util.Date;
import us.pserver.log.LogLevel;
import us.pserver.log.Log;
import us.pserver.log.format.OutputFormatter;

/**
 * Single threaded log system implementaion.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class SimpleLog extends AbstractLog {
  
  
  /**
   * Default constructor, receives this log instance name.
   * @param name This log instance name.
   */
  public SimpleLog(String name) {
    super(name);
  }
  
  
  /**
   * Constructor which receives this log instance class name.
   * @param cls This log instance class name.
   */
  public SimpleLog(Class cls) {
    super();
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    logname = cls.getName();
  }
  
  
  /**
   * Constructor which receives the log instance name and 
   * the <code>OutputFormatter</code> object.
   * @param name This Log instance name.
   * @param fmt The <code>OutputFormatter</code> object.
   * @see us.pserver.log.format.OutputFormatter
   */
  public SimpleLog(String name, OutputFormatter fmt) {
    this(name);
    this.setOutputFormatter(fmt);
  }
  
  
  /**
   * Constructor which receives this log instance class 
   * name and the <code>OutputFormatter</code> object.
   * @param cls This log instance class name.
   * @param fmt The <code>OutputFormatter</code> object.
   */
  public SimpleLog(Class cls, OutputFormatter fmt) {
    this(cls);
    this.setOutputFormatter(fmt);
  }
  
  
  @Override
  public Log log(LogLevel lvl, String msg) {
    if(lvl != null && msg != null && levels.isLevelEnabled(lvl)) {
      outputs.values().forEach(o->
          o.log(lvl, formatter.format(lvl, new Date(), logname, msg)));
    }
    return this;
  }
  
  
  @Override
  public Log log(LogLevel lvl, Throwable th, boolean logStackTrace) {
    if(lvl != null && th != null && levels.isLevelEnabled(lvl)) {
      Date dt = new Date();
      if(logStackTrace) {
        outputs.values().forEach(o->
            o.log(lvl, formatter.format(
                lvl, dt, th.getClass().getName(), 
                th.getLocalizedMessage())));
      } 
      else {
        String trace = th.getStackTrace()[0].toString();
        outputs.values().forEach(o->
            o.log(lvl, formatter.format(lvl, dt, trace, 
                th.getLocalizedMessage())));
      }
      if(logStackTrace) {
        Arrays.asList(th.getStackTrace()).forEach(e->
            outputs.values().forEach(o->
                o.log(lvl, continueFormat.format(
                    lvl, dt, logname, e.toString()))));
      }
    }
    return this;
  }
  
}