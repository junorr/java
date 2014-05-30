/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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


package com.jpower.sys;

import us.pserver.scronV6.ExecutionContext;
import us.pserver.scronV6.Job;
import us.pserver.scronV6.Schedule;


/**
 *
 * @version 0.0 - 21/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class StopperJob implements Job {
  
  private Schedule s;
  
  private Syscheck syscheck;
  
  
  public StopperJob() {
    s = new Schedule().repeatInSeconds(2);
    syscheck = null;
  }
  
  
  public Schedule getSchedule() {
    return s;
  }


  @Override
  public void execute(ExecutionContext context) {
    syscheck = (Syscheck) context.dataMap()
        .get(Syscheck.SYSCHECK);
    
    int pid = syscheck.getPid();
    if(pid == -1) {
      System.out.println();
      Log.logger().warning("Invalid PID. Exiting...");
      syscheck.stopServices();
    }
  }


  @Override
  public void error(Throwable th) {
    Log.logger().fatal(th.toString());
    System.exit(1);
  }

}
