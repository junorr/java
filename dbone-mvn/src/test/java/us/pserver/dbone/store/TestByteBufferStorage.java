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

package us.pserver.dbone.store;

import us.pserver.dbone.region.Region;
import us.pserver.dbone.util.Log;
import us.pserver.dbone.util.BytesToString;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/05/2018
 */
public class TestByteBufferStorage {

  private final Path storagePath = Paths.get("D:/storage.bin");
  //private final Path storagePath = Paths.get("/storage/storage.bin");
  
  
  @Test
  public void testMappedStorage() throws Exception {
    try {
      ByteBufferStorage fcs = ByteBufferStorage.builder().createMappedStorage(storagePath, 8*1024, 128);
      String helloA = "Hello Storage .............. A";
      String helloB = "Hello Storage ............... B";
      String helloC = "Hello Storage ................ C";
      String helloD = "Hello Storage ................. D";
      String helloE = "Hello Storage .................. E";
      ByteBuffer buf = Charset.forName("UTF-8").encode(helloA);
      //System.out.println(">>> Hello Storage ............ A");
      //new BytesToString(buf).print(4, '|');
      Log.on("Calling storage.put( %s )", buf);
      Region ra = fcs.put(buf);
      Log.on("storage.put(): %s", ra);
      buf = StandardCharsets.UTF_8.encode(helloB);
      Region rb = fcs.put(buf);
      buf = Charset.forName("UTF-8").encode(helloC);
      Region rc = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode(helloD);
      Region rd = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode(helloE);
      Region re = fcs.put(buf);
      fcs.remove(rb);
      fcs.remove(rd);
      fcs.close();

      fcs = ByteBufferStorage.builder().openMappedStorage(storagePath, 8*1024);
      Assertions.assertEquals(helloA,
          StandardCharsets.UTF_8.decode(fcs.get(ra)).toString()
      );
      Assertions.assertEquals(helloC,
          StandardCharsets.UTF_8.decode(fcs.get(rc)).toString()
      );
      Assertions.assertEquals(helloE, 
          StandardCharsets.UTF_8.decode(fcs.get(re)).toString()
      );
      Log.on(BytesToString.of(fcs.get(rb)).toString(4, '-'));
      Log.on(StandardCharsets.UTF_8.decode(fcs.get(rb)).toString());
      Log.on(BytesToString.of(fcs.get(rd)).toString(4, '-'));
      Log.on(StandardCharsets.UTF_8.decode(fcs.get(rd)).toString());
      
      buf = StandardCharsets.UTF_8.encode(helloB);
      rb = fcs.put(buf);
      
      String reserved = "Reserved data .............. 0";
      buf = StandardCharsets.UTF_8.encode(reserved);
      fcs.putReservedData(buf);
      fcs.close();

      fcs = ByteBufferStorage.builder().openMappedStorage(storagePath, 8*1024);
      Assertions.assertEquals(helloB, 
          StandardCharsets.UTF_8.decode(fcs.get(rb)).toString()
      );
      Assertions.assertEquals(reserved, 
          StandardCharsets.UTF_8.decode(fcs.getReservedData()).toString()
      );
      
      Log.on("Getting All Root Blocks...");
      List<Region> roots = fcs.getRootRegions();
      for(Region r : roots) {
        Log.on("root = %s, buf = %s", r, BytesToString.of(fcs.get(r)).toString(4, '|'));
      }
      fcs.close();
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  
  @Test
  public void testMemoryStorage() throws Exception {
    try {
      ByteBufferStorage fcs = ByteBufferStorage.builder().createMemoryStorage(8*1024, 128);
      String helloA = "Hello Storage .............. A";
      String helloB = "Hello Storage ............... B";
      String helloC = "Hello Storage ................ C";
      String helloD = "Hello Storage ................. D";
      String helloE = "Hello Storage .................. E";
      ByteBuffer buf = StandardCharsets.UTF_8.encode(helloA);
      //System.out.println(">>> Hello Storage ............ A");
      //new BytesToString(buf).print(4, '|');
      Log.on("Calling storage.put( %s )", buf);
      Region ra = fcs.put(buf);
      Log.on("storage.put(): %s", ra);
      buf = StandardCharsets.UTF_8.encode(helloB);
      Region rb = fcs.put(buf);
      buf = Charset.forName("UTF-8").encode(helloC);
      Region rc = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode(helloD);
      Region rd = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode(helloE);
      Region re = fcs.put(buf);
      fcs.remove(rb);
      fcs.remove(rd);
      
      buf = StandardCharsets.UTF_8.encode(helloB);
      rb = fcs.put(buf);
      
      String reserved = "Reserved data .............. 0";
      buf = StandardCharsets.UTF_8.encode(reserved);
      fcs.putReservedData(buf);

      Assertions.assertEquals(helloB, 
          StandardCharsets.UTF_8.decode(fcs.get(rb)).toString()
      );
      Assertions.assertEquals(reserved, 
          StandardCharsets.UTF_8.decode(fcs.getReservedData()).toString()
      );
      
      Log.on("Getting All Root Blocks...");
      List<Region> roots = fcs.getRootRegions();
      for(Region r : roots) {
        Log.on("root = %s, buf = %s", r, BytesToString.of(fcs.get(r)).toString(4, '|'));
      }
      fcs.close();
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
