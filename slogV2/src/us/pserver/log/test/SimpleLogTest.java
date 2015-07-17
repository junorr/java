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

package us.pserver.log.test;

import java.io.IOException;
import us.pserver.log.Log;
import us.pserver.log.LogFactory;
import us.pserver.log.LogLevel;
import static us.pserver.log.test.SLogV2Test.log;
import static us.pserver.log.test.SLogV2Test.repeat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2014
 */
public class SimpleLogTest {
  
  public static Throwable newIOException() {
    return new IOException("Some IO error");
  }

  public static void runFor(int times, Runnable r) {
    for(int i = 0; i < times; i++) {
      r.run();
    }
  }
   
  public static Runnable log(final Log log, final LogLevel lvl, final String msg) {
    return new Runnable() {
      public void run() {
        log.log(lvl, msg);
      }
    };
  }
   
  public static void main(String[] args) throws InterruptedException {
    Log log = LogFactory.createDefaultSimpleLog(SimpleLogTest.class, false);
    log.error(newIOException(), false);
    repeat(5, log(log, LogLevel.DEBUG, "This is a "+ LogLevel.DEBUG+ " Message."));
    repeat(5, log(log, LogLevel.INFO, "This is a "+ LogLevel.INFO+ " Message."));
    repeat(5, log(log, LogLevel.WARN, "This is a "+ LogLevel.WARN+ " Message."));
    repeat(5, log(log, LogLevel.ERROR, "This is a "+ LogLevel.ERROR+ " Message."));
    Thread.sleep(200);
    log.error(newIOException(), true);
  }
  
}