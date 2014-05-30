/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.db4o_test;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.jpower.sys.DB;
import com.jpower.sys.LastSnapshot;
import java.util.List;



/**
 *
 * @author juno
 */
public class Test {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ObjectContainer db = DB.openSocketClient();
    while(true) {
      Query q = db.query();
      q.constrain(LastSnapshot.class);
      List<LastSnapshot> list = DB.query(q, db);
      System.out.println(">> list: "+ list);
      System.out.println(">> list.size(): "+ (list != null ? list.size() : "-"));
      for(LastSnapshot s : list) {
        System.out.println(">> snap: "+ s);
      }
      System.out.println("---------------------------");
      try { Thread.sleep(3000); }
      catch(InterruptedException e) {}
    }
  }
  
}
