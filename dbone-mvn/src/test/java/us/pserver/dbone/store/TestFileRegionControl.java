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

import us.pserver.dbone.region.RegionControl;
import us.pserver.dbone.region.FileRegionControl;
import us.pserver.dbone.region.Region;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.dbone.util.Log;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/05/2018
 */
public class TestFileRegionControl {

  private final Path storagePath = Paths.get("D:/gfetch.sh");
  
  //private final Path storagePath = Paths.get("/storage/gfetch.sh");
  
  private final Path rgcPath = Paths.get("D:/rgc.bin");
  
  //private final Path rgcPath = Paths.get("/storage/rgc.bin");
  
  private final RegionControl rgc = new FileRegionControl(openStorageFileChannel(), 1024, new LinkedBlockingDeque<>());
  
  
  private FileChannel openStorageFileChannel() {
    try {
      return FileChannel.open(storagePath, StandardOpenOption.READ);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  //@Test
  public void testAllocate() {
    Region r = rgc.allocate();
    System.out.println(r);
    Assertions.assertEquals(Region.of(87, 1024), r);
  }
  
  
  private ByteBuffer readRgcPath() throws IOException {
    try (FileChannel ch = FileChannel.open(rgcPath, StandardOpenOption.READ)) {
      ByteBuffer buf = ByteBuffer.allocate((int) ch.size());
      ch.read(buf);
      buf.flip();
      return buf;
    }
  }
  
  
  private List<Region> toRegionList(ByteBuffer buf) {
    List<Region> lst = new LinkedList<>();
    while(buf.remaining() >= Region.BYTES) {
      lst.add(Region.of(buf));
    }
    return lst;
  }
  
  
  private void print(byte[] bs) {
    System.out.print("[ ");
    for(int i = 0; i < bs.length; i++) {
      System.out.print(bs[i]);
      if(i < bs.length -1) {
        if(i > 0 && (i+1) % 8 == 0) {
          System.out.print(" | ");
        }
        else {
          System.out.print(", ");
        }
      }
    }
    System.out.println(" ]");
  }
  
  
  //@Test
  public void testWriteToFile() throws IOException {
    Region r1 = Region.of(87, 1024);
    Region r2 = Region.of(1111, 1024);
    rgc.offer(r1);
    rgc.offer(r2);
    try (
        FileChannel ch = FileChannel.open(rgcPath, 
            StandardOpenOption.READ, 
            StandardOpenOption.WRITE, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING
        )
    ) {
      rgc.writeTo(ch, ByteBuffer::allocate);
    }
    ByteBuffer buf = readRgcPath();
    Log.on("readRgcPath: %s", buf);
    List<Region> lst = toRegionList(buf);
    print(buf.array());
    System.out.println(lst);
    Assertions.assertEquals(r1, lst.get(0));
    Assertions.assertEquals(r2, lst.get(1));
  }
  
}
