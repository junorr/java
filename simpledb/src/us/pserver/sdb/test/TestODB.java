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

import us.pserver.sdb.OID;
import us.pserver.sdb.ObjectDB;
import us.pserver.sdb.engine.FileEngine;
import us.pserver.sdb.query.Query;
import us.pserver.sdb.query.QueryBuilder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/10/2014
 */
public class TestODB {

  static class Creds {
    String user;
    String pass;
    boolean enabled;
    public String toString() {
      return "Creds{" + "user=" + user + ", pass=" + pass + ", enabled=" + enabled + '}';
    }
  }
  
  static class Server {
    int port;
    String ip;
    String name;
    boolean hasdb;
    Creds creds;
    public String toString() {
      return "Server{" + "port=" + port + ", ip=" + ip + ", name=" + name + ", hasdb=" + hasdb + ", creds="+ creds+ '}';
    }
  }
    
  static ObjectDB odb;
  
  
  public static void add() {
    System.out.println("---- ADD ----");
    System.out.println();
    
    Creds creds = new Creds();
    creds.enabled = true;
    creds.pass = "1234";
    creds.user = "juno";
    
    Server s102 = new Server();
    s102.creds = creds;
    s102.hasdb = true;
    s102.ip = "172.29.14.102";
    s102.name = "102";
    s102.port = 22;
    
    OID id = odb.put(s102);
    System.out.println("* odb.put: "+ id);
    
    Server s103 = new Server();
    s103.creds = creds;
    s103.hasdb = false;
    s103.ip = "172.29.14.103";
    s103.name = "103";
    s103.port = 22;
    
    id = odb.put(s103);
    System.out.println("* odb.put: "+ id);
    
    Server s105 = new Server();
    s105.hasdb = true;
    s105.ip = "172.29.14.105";
    s105.name = "105";
    s105.port = 22;
    
    id = odb.put(s105);
    System.out.println("* odb.put: "+ id);
  }
  
  
  public static void get() {
    System.out.println();
    System.out.println("---- GET ----");
    System.out.println();
    
    Server ex = new Server();
    ex.name = "102";
    ex.port = 22;
    System.out.println("* ex: "+ ex);
   
    OID id = odb.getOne(ex);
    System.out.println("* odb.getOne: "+ id);
    System.out.println();
    
    Query q = QueryBuilder.builder(Server.class)
        .field("creds").isEmpty().create();
    
    System.out.println("* query: "+ q);
    
    id = odb.getOne(q);
    System.out.println("* odb.getOne: "+ id);
    System.out.println();
  }
  
  
  public static void get2() {
    System.out.println();
    System.out.println("---- GET2 ----");
    System.out.println();
    
    Query q = QueryBuilder.builder(Server.class)
        .descend("creds")
        .field("user")
        .equalIgnoreCase("juno").create();
    
    System.out.println("* query: "+ q);
    
    OID id = odb.getOne(q);
    System.out.println("* odb.getOne: "+ id);
    System.out.println();
    
    q = QueryBuilder.builder(Server.class)
        .field("hasdb").equal(false).create();
    
    System.out.println("* query: "+ q);
    id = odb.getOne(q);
    System.out.println("* odb.getOne: "+ id);
    System.out.println();
  }
  
  
  public static void main(String[] args) {
    
    odb = new ObjectDB(new FileEngine("./object.db"));
    try {
      
      //add();
      get();
      get2();
      
    }
    finally {
      odb.close();
    }
    
  }
  
}
