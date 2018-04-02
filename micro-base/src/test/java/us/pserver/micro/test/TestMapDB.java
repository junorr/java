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

package us.pserver.micro.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import us.pserver.micro.util.ClassJsonSerializer;
import us.pserver.micro.util.JsonElementSerializer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/03/2018
 */
public class TestMapDB {
  
  private static final Path dbfile = Paths.get("map.db");
  
  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Class.class, new ClassJsonSerializer())
      .create();
  
  private DB getDB() {
    System.out.println("* DBFile: "+ dbfile.toAbsolutePath().toString());
    return DBMaker.fileDB(dbfile.toFile()).fileDeleteAfterClose().fileMmapEnable().make();
  }
  
  private HTreeMap<String,JsonElement> getMap(DB db) {
    return db.hashMap("json", 
        Serializer.STRING, 
        new JsonElementSerializer(gson)
    ).createOrOpen();
  }
  
  @Test
  public void testMapDbMemory() throws Exception {
    DB db = getDB();
    HTreeMap<String,JsonElement> map = getMap(db);
    JsonObject obj = new JsonObject();
    obj.addProperty("hello", "world");
    obj.addProperty("hidden", true);
    obj.addProperty("magic", 43);
    map.put("json", obj);
    db.commit();
    db.close();
    
    db = getDB();
    map = getMap(db);
    obj = map.get("json").getAsJsonObject();
    Assertions.assertEquals("world", obj.get("hello").getAsString());
    Assertions.assertEquals(true, obj.get("hidden").getAsBoolean());
    Assertions.assertEquals(43, obj.get("magic").getAsInt());
    db.close();
  }
  
}
