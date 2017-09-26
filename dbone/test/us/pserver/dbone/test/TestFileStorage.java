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

package us.pserver.dbone.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import us.pserver.dbone.store.Block;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.StorageFactory;
import us.pserver.dbone.store.StorageTransaction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class TestFileStorage {
  
  
  public static void fill(Block blk) {
    int len = (int) (blk.buffer().remaining() * (2.0/3.0));
    for(int i = 0; i < len; i++) {
      blk.buffer().put((byte)(Math.random() * 10000));
    }
  }
  
  
  public static void print15bytes(Block blk) {
    int pos = blk.buffer().position();
    int lim = blk.buffer().limit();
    byte[] bs = new byte[15];
    blk.buffer().position(0);
    blk.buffer().limit(15);
    blk.buffer().get(bs);
    System.out.println(blk+ ", "+ Arrays.toString(bs));
    blk.buffer().limit(lim);
    blk.buffer().position(pos);
  }

  
  public static void main(String[] args) throws IOException {
    Path dbpath = Paths.get("/home/juno/dbone-channel.dat");
    try {
      //Storage fs = StorageFactory.newFactory().setFile("/storage/dbone.dat").create();
      Storage fs = StorageFactory.newFactory().setFile(dbpath).createNoLock();
      //Storage fs = StorageFactory.newFactory().createDirect(32*1024);
      System.out.println("* storage.size(): "+ fs.size());
      Block blk = fs.allocate();
      System.out.println(" 1 allocate: "+ blk);
      blk = fs.allocate();
      System.out.println(" 2 allocate: "+ blk);
      blk = fs.allocate();
      System.out.println(" 3 allocate: "+ blk);

      fill(blk);
      print15bytes(blk);
      blk.buffer().flip();
      fs.put(blk);
      blk = fs.get(blk.region());
      print15bytes(blk);

      StorageTransaction stx = fs.startTransaction();
      blk = stx.allocate();
      fill(blk);
      blk.buffer().flip();
      stx.put(blk);
      System.out.println(blk);
      stx.rollback();

      blk = fs.allocate();
      fill(blk);
      print15bytes(blk);
      blk.buffer().flip();
      fs.put(blk);
      blk = fs.get(blk.region());
      print15bytes(blk);

      fs.close();
    }
    finally {
      Files.delete(dbpath);
    }
  }
  
}
