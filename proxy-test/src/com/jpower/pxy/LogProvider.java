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

package com.jpower.pxy;

import com.jpower.log.LogFile;
import com.jpower.log.LogFormatter;
import com.jpower.log.LogFormatterImpl;
import com.jpower.log.LogLevel;
import com.jpower.log.LogPrinter;
import com.jpower.log.Logger;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/07/2013
 */
public class LogProvider {
  
  public static final String LOG_FILE = "./pxy.log";
  
  private static Logger logger;
  
  
  public static void init() {
    if(logger == null) {
      logger = new Logger();
      LogFormatter fmt = new LogFormatterImpl("# (DATE) [LEVEL]: MESSAGE");
      LogPrinter err = new LogPrinter(System.err, fmt);
      LogFile df = new LogFile(LOG_FILE);
      LogPrinter dp = new LogPrinter(System.out); 
      logger.add(df, LogLevel.INFO);
      logger.add(dp, LogLevel.INFO);
      logger.add(err, LogLevel.ERROR, LogLevel.FATAL, LogLevel.WARN);
    }
  }
  
  
  public static Logger getLogger() {
    init();
    return logger;
  }

}
