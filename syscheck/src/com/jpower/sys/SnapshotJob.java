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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.date.SerialDate;
import us.pserver.date.SimpleDate;
import us.pserver.scronV6.ExecutionContext;
import us.pserver.scronV6.Job;
import us.pserver.scronV6.Schedule;


/**
 *
 * @version 0.0 - 17/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class SnapshotJob implements Job {
  
  private Schedule schedule;
  
  private int interval;
  
  private SnapshotTaker taker;
  
  private ObjectContainer db;
  
  
  public SnapshotJob() {
    setInterval(SysConfig.DEFAULT_SYSCHECK_INTERVAL);
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
    if(DB.isDBMaxSizeReached())
      this.removeOlderSnapshot();
    Snapshot sn = taker.take();
    db.store(sn);
    db.commit();
  }
  
  
  public SerialDate getOlderSerialDate() {
    Query q = db.query();
    q.constrain(SerialDate.class);
    q.descend("time");
    SimpleDate sd = new SimpleDate();
    sd.subDay(1);
    q.constrain(sd.getTime()).smaller();
    Comparator<SerialDate> cp = new Comparator<SerialDate>() {
      @Override
      public int compare(SerialDate o1, SerialDate o2) {
        if(o1 == null || o2 == null)
          return 0;
        if(o1.getDate().after(o2.getDate()))
          return 1;
        else 
          return -1;
      }
    };
    q.sortBy(cp);
    List<SerialDate> ls = DB.query(q, db);
    if(ls == null || ls.isEmpty())
      return null;
    return ls.get(0);
  }
  
  
  public void removeOlderSnapshot() {
    SerialDate sd = this.getOlderSerialDate();
    Query q = db.query();
    q.constrain(Snapshot.class);
    q.descend("time").constrain(sd);
    List<Snapshot> l = DB.query(q, db);
    if(l != null && l.isEmpty()) {
      for(Snapshot s : l) {
        db.delete(s);
        Log.logger().info("Remove Snapshot: "+ s);
      }
      db.commit();
    }
  }


  @Override public void error(Throwable th) {
    Log.logger().error(th.toString());
  }

  
  public static void main(String[] args) {
    Comparator<SerialDate> cp = new Comparator<SerialDate>() {
      @Override
      public int compare(SerialDate o1, SerialDate o2) {
        if(o1 == null || o2 == null)
          return 0;
        if(o1.getDate().after(o2.getDate()))
          return 1;
        else 
          return -1;
      }
    };
    List<SerialDate> lsd = new LinkedList<>();
    SimpleDate sd = new SimpleDate().addDay(1);
    lsd.add(new SerialDate(sd));
    sd = new SimpleDate().subDay(1);
    lsd.add(new SerialDate(sd));
    Collections.sort(lsd, cp);
    System.out.println(lsd.get(0).getDate());
  }
  
}
