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

package com.jpower.pstat;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.jpower.date.SimpleDate;
import com.jpower.sys.DB;
import com.jpower.sys.Snapshot;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/09/2012
 */
public class SnapshotPoll implements Serializable {
  
  private ObjectContainer db;
  
  private SimpleDate oldest;
  
  private SimpleDate newest;
  
  private SimpleDate startDate;
  
  private SimpleDate endDate;
  
  
  public SnapshotPoll() {
    db = DB.openSocketClient();
    this.findDates();
  }
  
  
  public void findDates() {
    oldest = new SimpleDate();
    Query q = db.query();
    q.constrain(Snapshot.class);
    List<Snapshot> snaps = DB.query(q, db);
    for(Snapshot s : snaps) {
      SimpleDate d = s.getTime();
      if(d != null && d.before(oldest))
        oldest = d;
    }
    newest = new SimpleDate(oldest);
    for(Snapshot s : snaps) {
      SimpleDate d = s.getTime();
      if(d != null && d.after(newest))
        newest = d;
    }
  }
  
  
  public List<Snapshot> getInterval() {
    if(startDate == null || endDate == null)
      return null;
    
    long startTime = startDate.getTime();
    long endTime = endDate.getTime();
    
    Query q = db.query();
    q.constrain(Snapshot.class);
    
    Query tq = q.descend("time").descend("time");
    
    tq.constrain(startTime).greater().equal()
        .and(tq.constrain(endTime).smaller().equal());
    
    return DB.query(q, db);
  }
  
  
  public List<Snapshot> getOnEndDate() {
    if(endDate == null)
      return null;
    
    long smallend = endDate.getTime() -1000;
    long greatend = endDate.getTime() +1000;
    
    Query q = db.query();
    q.constrain(Snapshot.class);
    
    Query tq = q.descend("time").descend("time");
    
    tq.constrain(smallend).greater()
        .and(tq.constrain(greatend).smaller());
    
    return DB.query(q, db);
  }


  public SimpleDate getOldest() {
    return oldest;
  }


  public SimpleDate getNewest() {
    return newest;
  }


  public SimpleDate getStartDate() {
    return startDate;
  }


  public void setStartDate(SimpleDate startDate) {
    this.startDate = startDate;
  }


  public SimpleDate getEndDate() {
    return endDate;
  }


  public void setEndDate(SimpleDate endDate) {
    this.endDate = endDate;
  }
  
  
  public static void main(String[] args) {
    SnapshotPoll cp = new SnapshotPoll();
    cp.findDates();
    System.out.println("oldest: "+ cp.getOldest());
    System.out.println("newest: "+ cp.getNewest());
    System.out.print("get newest: ");
    cp.setEndDate(cp.getNewest());
    List<Snapshot> l = cp.getOnEndDate();
    System.out.println((l == null || l.isEmpty() ? "null" : l.get(0)));
  }
  
}
