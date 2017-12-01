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

import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.FileStorage;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/11/2017
 */
public class FileStorageTest {

  //private static final Path path = Paths.get("/home/juno/dbone/");
  private static final Path path = Paths.get("D:/dbone/");
  
  private static final Storage store = new FileStorage(path, 30, ByteBuffer::allocateDirect);
  
  
  private ByteBuffer createSequenceBuffer(int size) {
    ByteBuffer buf = ByteBuffer.allocate(25);
    for(int i = 0; i < 25; i++) {
      buf.put((byte) i);
    }
    buf.flip();
    return buf;
  }
  
  
  private int[] bytesToIntArray(ByteBuffer buf) {
    int[] ints = new int[buf.remaining()];
    int idx = 0;
    while(buf.hasRemaining()) {
      ints[idx++] = buf.get();
    }
    return ints;
  }
  
  
  @Test
  public void bytesToIntArraySequencePreservation() {
    ByteBuffer buf = createSequenceBuffer(25);
    int[] ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    Assert.assertArrayEquals(ints, bytesToIntArray(buf));
  }
  
  
  @Test
  public void storageByteSequencePreservation() {
    ByteBuffer put = createSequenceBuffer(25);
    int[] sequence = bytesToIntArray(put);
    put.flip();
    Region reg = store.put(put);
    System.out.printf("* storageByteSequencePreservation.put: %s%n", reg);
    ByteBuffer get = store.get(reg);
    Assert.assertArrayEquals(sequence, bytesToIntArray(get));
  }
  
  @AfterClass
  public static void closeStorage() {
    store.close();
  }
  
}
