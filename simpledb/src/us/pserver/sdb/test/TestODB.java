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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/10/2014
 */
public class TestODB {

  
  public static void main(String[] args) {
    class Server {
      int port;
      String ip;
      String name;
      boolean hasdb;
      public String toString() {
        return "Server{" + "port=" + port + ", ip=" + ip + ", name=" + name + ", hasdb=" + hasdb + '}';
      }
    }
    
    ObjectDB odb = new ObjectDB(new FileEngine("./object.db"));
    try {
      
      Server s102 = new Server();
      s102.hasdb = true;
      s102.ip = "172.29.14.102";
      s102.name = "102";
      s102.port = 22;

      OID id = new OID();
      //OID id = odb.put(s102);
      System.out.println("* odb.put: "+ id);
    
      Server ex = new Server();
      ex.name = "102";
      ex.port = 22;
      System.out.println("* ex: "+ ex);
    
      id = odb.getOne(ex);
      System.out.println("* odb.getOne: "+ id);
    }
    finally {
      odb.close();
    }
    
  }
  
}
