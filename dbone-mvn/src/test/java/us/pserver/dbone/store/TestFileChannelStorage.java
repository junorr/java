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
import java.nio.file.Files;
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
public class TestFileChannelStorage {

  private final Path storagePath = Paths.get("D:/storage.bin");
  //private final Path storagePath = Paths.get("/storage/storage.bin");
  
  
  @Test
  public void testStoragePutString() throws Exception {
    try {
      FileChannelStorage fcs = FileChannelStorage.builder().create(storagePath, 40);
      ByteBuffer buf = Charset.forName("UTF-8").encode("Hello Storage ............ A");
      //System.out.println(">>> Hello Storage ............ A");
      //new BytesToString(buf).print(4, '|');
      Region ra = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode("Hello Storage ............ B");
      Region rb = fcs.put(buf);
      buf = Charset.forName("UTF-8").encode("Hello Storage ............ C");
      Region rc = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode("Hello Storage ............ D");
      Region rd = fcs.put(buf);
      buf = StandardCharsets.UTF_8.encode("Hello Storage ............ E");
      Region re = fcs.put(buf);
      fcs.remove(rb);
      fcs.remove(rd);
      fcs.close();

      fcs = FileChannelStorage.builder().open(storagePath);
      Assertions.assertEquals(
          "Hello Storage ............ A",
          StandardCharsets.UTF_8.decode(fcs.get(ra)).toString()
      );
      Assertions.assertEquals(
          "Hello Storage ............ C",
          StandardCharsets.UTF_8.decode(fcs.get(rc)).toString()
      );
      Assertions.assertEquals(
          "Hello Storage ............ E",
          StandardCharsets.UTF_8.decode(fcs.get(re)).toString()
      );
      //Log.on(BytesToString.of(fcs.get(rb)).toString(4, '-'));
      //Log.on(StandardCharsets.UTF_8.decode(fcs.get(rb)).toString());
      //Log.on(BytesToString.of(fcs.get(rd)).toString(4, '-'));
      //Log.on(StandardCharsets.UTF_8.decode(fcs.get(rd)).toString());
      
      buf = StandardCharsets.UTF_8.encode("Hello Storage ............ B");
      rb = fcs.put(buf);
      
      buf = StandardCharsets.UTF_8.encode("Reserved data ............ 0");
      fcs.putReservedData(buf);
      fcs.close();

      fcs = FileChannelStorage.builder().open(storagePath);
      Assertions.assertEquals(
          "Hello Storage ............ B",
          StandardCharsets.UTF_8.decode(fcs.get(rb)).toString()
      );
      Assertions.assertEquals(
          "Reserved data ............ 0",
          StandardCharsets.UTF_8.decode(fcs.getReservedData()).toString()
      );
      
      Log.on("Getting All Root Blocks...");
      List<Region> roots = fcs.getRootRegions();
      for(Region r : roots) {
        Log.on("root = %s, buf = %s", r, BytesToString.of(fcs.get(r)).toString(4, '|'));
      }
      fcs.close();
      storagePath.toFile().deleteOnExit();
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
