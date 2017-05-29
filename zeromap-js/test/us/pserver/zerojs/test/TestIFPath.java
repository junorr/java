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

package us.pserver.zerojs.test;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import oodb.tests.beans.FTime;
import oodb.tests.beans.IFPath;
import oodb.tests.beans.IFTime;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2016
 */
public class TestIFPath {

  
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("/home/juno/Downloads/stunnel4_4.53-1.1ubuntu1_amd64.deb");
    IFPath fpath = IFPath.from(path);
    System.out.println(fpath);
    MapperFactory fact = MapperFactory.factory();
    Mapper<IFPath> mpr = fact.mapper(IFPath.class);
    Node node = mpr.map(fpath);
    /*
    System.out.println(node);
    fpath = mpr.unmap(node, IFPath.class);
    System.out.println(fpath);
    */
    Instant now = Instant.now();
    System.out.println(now);
    String json = JsonWriter.objectToJson(now);
    System.out.println(json);
    now = (Instant) JsonReader.jsonToJava(json);
    System.out.println(now);
    
    IFTime time = new FTime(now, now.plusSeconds(100), now.plusSeconds(300));
    System.out.println(time);
    json = JsonWriter.objectToJson(time);
    System.out.println(json);
    time = null;
    time = (IFTime) JsonReader.jsonToJava(json);
    System.out.println(time);
    
    System.out.println(JsonWriter.objectToJson(fpath));
    System.out.println(JsonReader.jsonToJava(JsonWriter.objectToJson(fpath)));
  }
  
}
