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

package us.pserver.jasdb;

import nl.renarj.jasdb.LocalDBSession;
import nl.renarj.jasdb.api.SimpleEntity;
import nl.renarj.jasdb.api.model.EntityBag;
import nl.renarj.jasdb.api.query.QueryBuilder;
import nl.renarj.jasdb.api.query.QueryExecutor;
import nl.renarj.jasdb.api.query.QueryResult;
import nl.renarj.jasdb.core.SimpleKernel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/09/2014
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      System.out.println("* Starting...");
      SimpleKernel.initializeKernel();
      LocalDBSession sess = new LocalDBSession();
      sess.addAndSwitchInstance("c", "./c.jdb");
      EntityBag bag = sess.createOrGetBag("default");
      
      System.out.println("* Adding entities...");
      SimpleEntity se = new SimpleEntity();
      se.addProperty("label", "Server")
          .addProperty("name", "102")
          .addProperty("ip", "172.29.14.102")
          .addProperty("port", 22);
      bag.addEntity(se);
      System.out.println("* added "+ se.getInternalId());
          
      se = new SimpleEntity();
      se.addProperty("label", "Server")
          .addProperty("name", "103")
          .addProperty("ip", "172.29.14.103")
          .addProperty("port", 22);
      bag.addEntity(se);
      System.out.println("* added "+ se.getInternalId());
      
      System.out.println("* Closing...");
      bag.flush();
      sess.closeSession();
      //SimpleKernel.waitForShutdown();
      SimpleKernel.shutdown();
      
      //////// SEARCH ////////////
      
      System.out.println("* Starting...");
      SimpleKernel.initializeKernel();
      sess = new LocalDBSession();
      sess.switchInstance("c");
      bag = sess.createOrGetBag("default");
      
      System.out.println("* Query by label and name");
      QueryBuilder q = QueryBuilder.createBuilder()
          .field("label").value("Server")
          .field("name").value("102");
      QueryExecutor qe = bag.find(q);
      QueryResult r = qe.execute();
      while(r.hasNext()) {
        System.out.println(r.next());
      }
      
      System.out.println("* Query by port");
      q = QueryBuilder.createBuilder()
          .field("label").value("Server")
          .field("port").value(22);
      qe = bag.find(q);
      r = qe.execute();
      while(r.hasNext()) {
        System.out.println(r.next());
      }
      
      System.out.println("* Closing...");
      bag.flush();
      sess.closeSession();
      //SimpleKernel.waitForShutdown();
      SimpleKernel.shutdown();
      
      ////////////// DELETING ///////////////////
      
      System.out.println("* Starting...");
      SimpleKernel.initializeKernel();
      sess = new LocalDBSession();
      sess.switchInstance("c");
      bag = sess.createOrGetBag("default");
      
      System.out.println("* Query by label");
      q = QueryBuilder.createBuilder()
          .field("label").value("Server");
      qe = bag.find(q);
      r = qe.execute();
      while(r.hasNext()) {
        se = r.next();
        System.out.println("* Deleting "+ se);
        bag.removeEntity(se);
      }
      
      System.out.println("* Closing...");
      bag.flush();
      sess.closeSession();
      //SimpleKernel.waitForShutdown();
      SimpleKernel.shutdown();
    }

}
