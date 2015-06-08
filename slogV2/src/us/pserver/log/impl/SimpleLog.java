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
 * @version 1.0 - 14/04/2014
 * @see us.pserver.log.SLogV2
 */
public class SimpleLog extends AbstractLog {
  
  
  /**
   * Construtor padrão e sem argumentos,
   * cria uma instância de <code>SimpleLog</code> com configurações
   * padrão, pronto para ser utilizado.
   */
  public SimpleLog(String name) {
    super(name);
  }
  
  
  public SimpleLog(Class cls) {
    super();
    if(cls == null)
      throw new IllegalArgumentException("Invalid null Class");
    logname = cls.getName();
  }
  
  
  public SimpleLog(String name, OutputFormatter fmt) {
    this(name);
    this.setOutputFormatter(fmt);
  }
  
  
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