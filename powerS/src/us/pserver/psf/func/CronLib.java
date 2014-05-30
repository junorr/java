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

package us.pserver.psf.func;

import java.io.IOException;
import java.util.ArrayList;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;
import us.pserver.date.SimpleDate;
import us.pserver.scronv6.SCronV6;
import us.pserver.scron.Schedule;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/02/2014
 */
public class CronLib implements FSFunctionExtension {
  
  public static final String
      SCHEDULE = "schedule";
  
  
  private BasicIO script;
  
  private SCronV6 cron;
  
  
  public CronLib(BasicIO bio) {
    if(bio == null)
      throw new IllegalArgumentException(
          "Invalid BasicIO ["+ bio+ "]");
    script = bio;
    cron = new SCronV6();
  }
  
  
  public CronLib(BasicIO bio, SCronV6 cron) {
    if(bio == null)
      throw new IllegalArgumentException(
          "Invalid BasicIO ["+ bio+ "]");
    this.setCron(cron);
    script = bio;
  }
  
  
  public SCronV6 getCron() {
    return cron;
  }
  
  
  public CronLib setCron(SCronV6 cron) {
    if(cron == null)
      throw new IllegalArgumentException(
          "Invalid SCronV6 ["+ cron+ "]");
    this.cron = cron;
    return this;
  }
  
  
  public void schedule(SimpleDate date, final String function) throws FSException {
    if(date.isPast())
      throw new FSException("Schedule date is in past ["+ date+ "]");
    
    Schedule sc = new Schedule().startAt(date);
    Runnable rn = new Runnable() {
      public void run() {
        try {
          script.callScriptFunction(function, new ArrayList(1));
        } catch(FSException | IOException e) {}
      }
    };
    cron.put(sc, rn);
  }


  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case SCHEDULE:
        FUtils.checkLen(al, 2);
        schedule(FUtils.date(al.get(0)), 
            FUtils.str(al, 1));
        break;
      default:
        throw new FSUnsupportedException();
    }
    return null;
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(SCHEDULE, this);
  }

}
