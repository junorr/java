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
import com.jpower.sys.DB;
import com.jpower.sys.LastSnapshot;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/09/2012
 */
public class LastSnapshotPoll implements Serializable {
  
  private LastSnapshot last;
  
  private ObjectContainer db;
  
  
  public LastSnapshotPoll() {
    db = DB.openSocketClient();
  }
  
  
  public LastSnapshot poll() {
    Query q = db.query();
    q.constrain(LastSnapshot.class);
    List<LastSnapshot> list = DB.query(q, db);
    if(list == null || list.isEmpty()) return null;
    last = list.get(0);
    return last;
 }
  
  
  public LastSnapshot getLastSnapshot() {
    if(last == null) return poll();
    return last;
  }
  
  
  public static void main(String[] args) {
    LastSnapshotPoll cp = new LastSnapshotPoll();
    System.out.println(cp.poll().getMem());
  }
  
}
