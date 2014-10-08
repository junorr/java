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

package us.pserver.sdb.test;

import us.pserver.sdb.Document;
import us.pserver.sdb.Query;
import us.pserver.sdb.SimpleDB;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/10/2014
 */
public class TestSimpleDB2 {
  
  static SimpleDB sdb;

  
  public static void create() {
    Document cred = new Document("credentials")
        .put("user", "username")
        .put("pass", "password");
    
    Document server = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("enabled", true)
        .put("credentials", cred);
    
    System.out.println("* put server: "+ server.toXml());
    
    sdb.put(server);
  }
  
  
  public static void get() {
    Query q = new Query("name")
        .equal("102");
    
    System.out.println("* query: "+ q);
    
    Document server = sdb.getOne("server", q);
    System.out.println("* get server: "+ server);
    System.out.println("* server.credentials: "+ server.get("credentials"));
  }
  
  
  public static void dontDoThis() {
    Query q = new Query("user")
        .equal("username");
    
    System.out.println("* query: "+ q);
    
    Document cred = sdb.getOne("credentials", q);
    System.out.println("* get credentials: "+ cred);
    
    System.out.println("* remove(cred): "+ sdb.remove(cred));
    
    System.out.println("* get...");
    get();
  }
  
  
  public static void put() {
    Document server = new Document("server")
        .put("name", "105")
        .put("ip", "172.29.14.105")
        .put("enabled", false)
        .put("name1", "105")
        .put("ip1", "172.29.14.105")
        .put("enabled1", false)
        .put("name2", "105")
        .put("ip2", "172.29.14.105")
        .put("enabled2", false)
        .put("name3", "105")
        .put("ip3", "172.29.14.105")
        .put("enabled3", false)
        .put("name4", "105")
        .put("ip4", "172.29.14.105")
        .put("enabled4", false)
        .put("name5", "105")
        .put("ip5", "172.29.14.105")
        .put("enabled5", false);
    
    System.out.println("* put server: "+ server.toXml());
    
    sdb.put(server);
  }
  
  
  public static void get2() {
    Query q = new Query("name")
        .equal("105");
    
    System.out.println("* query: "+ q);
    
    Document server = sdb.getOne("server", q);
    System.out.println("* get server: "+ server);
  }
  
  
  public static void main(String[] args) {
    sdb = new SimpleDB("./data.sdb");
    
    try {
      
      create();
      //get();
      //dontDoThis();
      //put();
      //get2();
      
    } catch(Exception e) {
      throw e;
    } finally {
      sdb.close();
    }
  }
  
}
