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

import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.sdb.Document;
import us.pserver.sdb.engine.CryptSerialEngine;
import us.pserver.sdb.engine.JsonSerialEngine;
import static us.pserver.sdb.test.TestSDB.sdb;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 26/12/2014
 */
public class TestCryptEngine {

  
  public static void main(String[] args) {
    CryptKey key = new CryptKey("123456", CryptAlgorithm.AES_CBC_PKCS5);
    JsonSerialEngine js = new JsonSerialEngine();
    CryptSerialEngine serial = new CryptSerialEngine(js, key);
    
    Document cred = new Document("credentials")
        .put("user", "username")
        .put("pass", "password");
    System.out.println("-> doc: "+ cred);
    byte[] bs = serial.serialize(cred);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(cred).length);
    System.out.println("  json  = "+ new String(js.serialize(cred)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
    
    Document doc = new Document("server")
        .put("name", "102")
        .put("ip", "172.29.14.102")
        .put("enabled", true)
        .put("apps", 2)
        .put("db", true)
        .put("creds", cred);
    System.out.println("-> doc: "+ doc);
    bs = serial.serialize(doc);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(doc).length);
    System.out.println("  json  = "+ new String(js.serialize(doc)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
    
    doc = new Document("server")
        .put("name", "103")
        .put("ip", "172.29.14.103")
        .put("enabled", true)
        .put("apps", 7)
        .put("db", true)
        .put("creds", cred);
    System.out.println("-> doc: "+ doc);
    bs = serial.serialize(doc);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(doc).length);
    System.out.println("  json  = "+ new String(js.serialize(doc)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
    
    doc = new Document("server")
        .put("name", "104")
        .put("ip", "172.29.14.104")
        .put("enabled", false)
        .put("apps", 0)
        .put("db", true)
        .put("creds", cred);
    System.out.println("-> doc: "+ doc);
    bs = serial.serialize(doc);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(doc).length);
    System.out.println("  json  = "+ new String(js.serialize(doc)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
    
    doc = new Document("server")
        .put("name", "105")
        .put("ip", "172.29.14.105")
        .put("enabled", true)
        .put("apps", 4)
        .put("db", false)
        .put("creds", cred);
    System.out.println("-> doc: "+ doc);
    bs = serial.serialize(doc);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(doc).length);
    System.out.println("  json  = "+ new String(js.serialize(doc)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
    
    doc = new Document("server")
        .put("name", "100")
        .put("ip", "172.29.14.100")
        .put("enabled", true)
        .put("apps", 10)
        .put("db", false)
        .put("creds", cred);
    System.out.println("-> doc: "+ doc);
    bs = serial.serialize(doc);
    System.out.println("  bytes = "+ bs.length);
    System.out.println("  json  = "+ js.serialize(doc).length);
    System.out.println("  json  = "+ new String(js.serialize(doc)));
    System.out.println("  dcrpt = "+ serial.deserialize(bs));
  }
  
}
