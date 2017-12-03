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

package us.pserver.dbone.internal;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/11/2017
 */
public class FileRegionsTest {
  
  private final Path storeFile = Paths.get("/home/juno/dbone/fileRegionsStorageTest.dat");
  
  private final Path regionsFile = Paths.get("/home/juno/dbone/fileRegionsTest.dat");
  
  private final long startPos = 0;
  
  private final int blockSize = 1024;
  
  private final int minRegions = 2;
  
  private final int maxRegions = 20;
  

  @Test
  public void calculateFileRegions() {
    FileSizeRegionControl fr = new FileSizeRegionControl(storeFile, startPos, blockSize, minRegions, maxRegions);
    long pos = startPos;
    String msg = "[%d] Region(%s - %s)";
    for(int i = 0; i < maxRegions; i++) {
      Region r = fr.allocate();
      Assert.assertEquals(String.format(msg, i, pos, blockSize), Region.of(pos, blockSize), r);
      pos += blockSize;
    }
  }
  
  @Test
  public void allocateAllAndOfferFirst() {
    FileSizeRegionControl fr = new FileSizeRegionControl(storeFile, startPos, blockSize, minRegions, maxRegions);
    Region first = fr.allocate();
    Assert.assertEquals(Region.of(0, 1024), first);
    Assert.assertTrue(fr.offer(first));
    long pos = blockSize;
    String msg = "[%d] Region(%s - %s)";
    for(int i = 0; i < maxRegions-1; i++) {
      Region r = fr.allocate();
      Assert.assertEquals(String.format(msg, i, pos, blockSize), Region.of(pos, blockSize), r);
      pos += blockSize;
    }
    //should allocate first region again
    Assert.assertEquals(first, fr.allocate());
  }
  
  @Test
  public void discardFirstAndAllocateAll() {
    FileSizeRegionControl fr = new FileSizeRegionControl(storeFile, startPos, blockSize, minRegions, maxRegions);
    Region first = Region.of(0, 1024);
    Assert.assertTrue(fr.discard(first));
    long pos = blockSize;
    String msg = "[%d] Region(%s - %s)";
    for(int i = 0; i < maxRegions-1; i++) {
      Region r = fr.allocate();
      Assert.assertEquals(String.format(msg, i, pos, blockSize), Region.of(pos, blockSize), r);
      pos += blockSize;
    }
  }
  
  @Test
  public void fileWriteReadConsistency() throws IOException {
    FileSizeRegionControl fr = new FileSizeRegionControl(storeFile, startPos, blockSize, minRegions, maxRegions);
    //discard first
    fr.allocate();
    fr.writeTo(regionsFile);
    fr.readFrom(regionsFile);
    Region expected = Region.of(blockSize, blockSize);
    Assert.assertEquals(expected, fr.allocate());
  }
  
}
