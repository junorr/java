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
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.dbone.store.FileStorage;
import us.pserver.dbone.internal.Region;
import us.pserver.dbone.store.StorageFactory;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/10/2017
 */
public class TestNFileStorage {

  
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("/home/juno/dbone/");
    Path dbfile = path.resolve("storage.dat");
    try (
        FileStorage store = new FileStorage(path, 30, StorageFactory.ALLOC_POLICY_HEAP);
        ) {
      ByteBuffer buf = ByteBuffer.allocate(25);
      for(int i = 0; i < 25; i++) {
        buf.put((byte) i);
      }
      buf.flip();
      Region r = store.put(buf);
      System.out.println("* store.put(buf): "+ r);
      r = Region.of(r.offset(), 2);
      buf = store.get(r);
      System.out.print("* store.get(r): ");
      while(buf.hasRemaining()) {
        System.out.printf(" %d", buf.get());
      }
      System.out.println();
    } 
    finally {
      //Files.delete(dbfile);
    }
  }
  
}
