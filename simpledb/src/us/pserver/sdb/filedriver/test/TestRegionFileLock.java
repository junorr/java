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

package us.pserver.sdb.filedriver.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.sdb.filedriver.Region;
import us.pserver.sdb.filedriver.Region.DefRegion;
import us.pserver.sdb.filedriver.RegionFileLock;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public class TestRegionFileLock {
  
  public static final int MAX_THREADS = 10;
  
  public static final List<String> tnames = new ArrayList<>();
  
  
  public static void locker(RegionFileLock lock, Timer tm, ExecutorService serv) {
    String tn = Thread.currentThread().getName();
    try {
      Region r = new DefRegion(100, 200);
      System.out.println("* ["+tn+"] r: "+ r);

      System.out.println("* ["+tn+"] rl.lock(r): "+ lock.lock(r, false));
      System.out.println("* ["+tn+"] "+ lock);

      Region r2 = new DefRegion(300, 200);
      System.out.println("* ["+tn+"] r2: "+ r2);

      System.out.println("* ["+tn+"] rl.lock(r2): "+ lock.lock(r2, true));
      System.out.println("* ["+tn+"] "+ lock);

      Region r3 = new DefRegion(150, 30);
      System.out.println("* ["+tn+"] r3: "+ r3);
      System.out.println("* ["+tn+"] rl.isLocked(r3): "+ lock.isLocked(r3));

      System.out.println("* ["+tn+"] rl.unlock(r);");
      lock.unlock(r);
      System.out.println("* ["+tn+"] rl.isLocked(r3): "+ lock.isLocked(r3));
      System.out.println("* ["+tn+"] rl.lock(r3): "+ lock.lock(r3, false));
      System.out.println("* ["+tn+"] "+ lock);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
    tnames.add(tn);
    if(tnames.size() == MAX_THREADS) {
      System.out.println("* Terminating All Lockers...");
      System.out.println("* "+ tm.stop());
      serv.shutdownNow();
    }
  }

  
  public static void main(String[] args) throws InterruptedException, IOException {
    Path path = Paths.get("d:/test.lok");
    RegionFileLock lock = RegionFileLock.of(path);
    ExecutorService serv = Executors.newFixedThreadPool(MAX_THREADS);
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < MAX_THREADS; i++) {
      serv.submit(()->locker(lock, tm, serv));
    }
  }
  
}
