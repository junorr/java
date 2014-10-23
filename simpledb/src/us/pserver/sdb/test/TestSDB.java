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
import us.pserver.sdb.Result;
import us.pserver.sdb.SimpleDB;
import us.pserver.sdb.engine.FileEngine;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/10/2014
 */
public class TestSDB {

  static SimpleDB sdb;
  
  
  public static void add() {
    System.out.println("---- ADD ----");
    System.out.println();
    
    Document cred = new Document("credentials")
        .put("user", "username")
        .put("pass", "password");
    sdb.put(cred);
    
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("enabled", true)
        .put("apps", 2)
        .put("db", true)
        .put("creds", cred);
    sdb.put(doc);
    
    doc = new Document("server")
        .put("name", "103")
        .put("ip", "172.29.14.103")
        .put("enabled", true)
        .put("apps", 7)
        .put("db", true)
        .put("creds", cred);
    sdb.put(doc);
    
    doc = new Document("server")
        .put("name", "104")
        .put("ip", "172.29.14.104")
        .put("enabled", false)
        .put("apps", 0)
        .put("db", true)
        .put("creds", cred);
    sdb.put(doc);
    
    doc = new Document("server")
        .put("name", "105")
        .put("ip", "172.29.14.105")
        .put("enabled", true)
        .put("apps", 4)
        .put("db", false)
        .put("creds", cred);
    sdb.put(doc);
    
    doc = new Document("server")
        .put("name", "100")
        .put("ip", "172.29.14.100")
        .put("enabled", true)
        .put("apps", 10)
        .put("db", false)
        .put("creds", cred);
    sdb.put(doc);
  }
  
  
  public static void add2() {
    System.out.println("---- ADD2 ----");
    System.out.println();
    
    Document cred = new Document("credentials")
        .put("user", "username")
        .put("pass", "password")
        .put("email", "user@example.com");
    sdb.put(cred);
    
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("enabled", true)
        .put("apps", 2)
        .put("db", true)
        .put("creds", cred);
    sdb.put(doc);
  }
  
  
  public static void get() {
    System.out.println();
    System.out.println("---- GET ----");
    System.out.println();
    
    Query q = new Query("server")
        .field("name").equal("102");
    System.out.println("-> query: "+ q);
    
    Document doc = sdb.getOne(q);
    System.out.println("-> doc: "+ doc);
  }
  
  
  public static void get2() {
    System.out.println();
    System.out.println("---- GET2 ----");
    System.out.println();
    
    Query q = new Query("server")
        .field("name").not().equal("102")
        .and("apps").greaterEquals(4);
    System.out.println("-> query: "+ q);
    
    Result rs = sdb.get(q);
    System.out.println("-> result: "+ rs.size());
    rs.orderBy("name").asc();
    while(rs.hasNext()) {
      System.out.println("  -> item: "+ rs.next());
    }
  }

  
  public static void get3() {
    System.out.println();
    System.out.println("---- GET3 ----");
    System.out.println();
    
    Query q = new Query("server")
        .descend("creds")
        .field("user").equal("username")
        .and("name").not().contains("2");
    System.out.println("-> query: "+ q);
    
    Result rs = sdb.get(q);
    System.out.println("-> result: "+ rs.size());
    rs.orderBy("name").asc();
    while(rs.hasNext()) {
      System.out.println("  -> item: "+ rs.next());
    }
  }

  
  public static void rm() {
    System.out.println();
    System.out.println("---- RM ----");
    System.out.println();
    
    System.out.println("* sdb.remove: "+ sdb.remove(new Document("server").put("name", "102")));
  }    
  
  
  public static void main(String[] args) {
    sdb = new SimpleDB(new FileEngine("./simple.db"));
    
    try 
    {
      
      //add();
      //add2();
      get();
      get2();
      get3();
      //rm();
      
    }
    finally {
      sdb.close();
    }
  }
  
  
}
