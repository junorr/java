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

package us.pserver.scronv6.test;

import us.pserver.date.SimpleDate;
import us.pserver.log.Log;
import us.pserver.log.LogProvider;
import us.pserver.scron.Schedule;
import us.pserver.scronv6.SCronV6;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/10/2014
 */
public class TestWainting {

  
  
  public static void main(String[] args) throws InterruptedException {
    Log log = LogProvider.getSLogV2("TestWaiting.log");
    SCronV6 cron = new SCronV6(log);
    cron.put(new Schedule().startNow().repeatInMinutes(1), 
        ()->log.info("* Minute job running"));
    cron.put(new Schedule().startDelayed(500).repeatInSeconds(30), 
        ()->log.info("* 30 Seconds job running"));
    
    log.info("Wanting for 1 min...");
    Thread.sleep(2000*60);
    cron.put(new Schedule().startAt(new SimpleDate().addMinute(8)), 
        ()->{log.info("*** 8 Minutes job running ***"); System.exit(0);});
  }
  
  
}
