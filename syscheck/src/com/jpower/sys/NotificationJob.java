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

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import java.util.List;
import us.pserver.scronV6.ExecutionContext;
import us.pserver.scronV6.Job;
import us.pserver.scronV6.Schedule;

/**
 *
 * @version 0.0 - 18/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class NotificationJob implements Job {

  public static final int MINUTE = 1;
  
  private ObjectContainer db;
  
  private NotificationConf ndata;
  
  private Schedule schedule;
  
  private Email email;
  
  
  public NotificationJob() {
    db = DB.openLocalClient();
    ndata = new NotificationConf();
    schedule = new Schedule()
        .repeatInMinutes(MINUTE);
    email = new Email();
  }
  
  
  public Schedule getSchedule() {
    return schedule;
  }
  
  
  public LastSnapshot retrieveLastSnapshot() {
    Query q = db.query();
    q.constrain(LastSnapshot.class);
    List<LastSnapshot> list = q.execute();
    if(list == null || list.isEmpty())
      return null;
    LastSnapshot ls = list.get(0);
    if(ls != null)
      db.ext().refresh(ls, 5);
    return ls;
  }
  
  
  public void print(LastSnapshot s) {
    if(s == null) return;
    Log.logger().info("Snapshot: ["+ s.getTime() + "], ID: "+ s.getID());
    Log.logger().info("- "+ s.getCpu());
    Log.logger().info("- "+ s.getMem());
    List<Disk> disks = s.getDisks();
    for(Disk d : disks) {
      Log.logger().info("- "+ d);
    }
    List<IFNetwork> ifs = s.getInterfaces();
    for(IFNetwork n : ifs) {
      Log.logger().info("- "+ n);
      Log.logger().info("- "+ n.getLoad());
    }
  }


  @Override
  public void execute(ExecutionContext context) {
    ndata.load();
    LastSnapshot snap = this.retrieveLastSnapshot();
    if(snap == null) return;
    if(!ndata.match(snap)) return;
    
    email.setConf(ndata);
    email.setSnapshot(snap);
    email.send();
  }


  @Override
  public void error(Throwable th) {
    th.printStackTrace();
  }
  
}
