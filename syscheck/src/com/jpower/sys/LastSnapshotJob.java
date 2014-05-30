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
 * @version 0.0 - 17/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class LastSnapshotJob implements Job {

  private Schedule schedule;
  
  private int interval;
  
  private SnapshotTaker taker;
  
  private ObjectContainer db;
  
  
  public LastSnapshotJob() {
    setInterval(SysConfig.DEFAULT_INTERFACE_UPDATE_INTERVAL);
    taker = new SnapshotTaker();
    db = DB.openLocalClient();
  }
  
  
  public Schedule getSchedule() {
    return schedule;
  }
  
  
  public int getInterval() {
    return interval;
  }


  public void setInterval(int interval) {
    if(interval > 0) {
      this.interval = interval;
      schedule = new Schedule()
          .repeatInSeconds(interval)
          .startNow();
    }
  }
  

  @Override
  public void execute(ExecutionContext context) {
    LastSnapshot last = this.retrieve();
    if(last == null) last = new LastSnapshot();
    last.addID();
    Snapshot s = taker.take();
    this.copyValues(s, last);
    db.store(last);
    db.commit();
  }
  
  
  private void copyValues(Snapshot s1, Snapshot s2) {
    if(s1 == null || s2 == null) return;
    s2.setCpu(s1.getCpu())
        .setProcesses(s1.getProcesses())
        .setMem(s1.getMem())
        .setDisk(s1.getDisks())
        .setInterfaces(s1.getInterfaces())
        .setTime(s1.getTime());
  }
  
  
  public LastSnapshot retrieve() {
    Query q = db.query();
    q.constrain(LastSnapshot.class);
    List<LastSnapshot> snap = q.execute();
    if(snap == null || snap.isEmpty())
      return null;
    LastSnapshot ls = snap.get(0);
    if(ls != null)
      db.ext().refresh(ls, 10);
    return ls;
  }


  @Override
  public void error(Throwable th) {
    Log.logger().error("Error in LastSnapshotJob:");
    Log.logger().error(th.toString());
    Log.logger().error(th.toString());
  }

}
