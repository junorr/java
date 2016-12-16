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

import java.nio.ByteBuffer;
import us.pserver.sdb.filedriver.Region;
import us.pserver.sdb.filedriver.Region.DefRegion;
import us.pserver.sdb.filedriver.RegionLock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public class TestRegionLock {
  
  
  public static void locker(RegionLock lock) {
    String tn = Thread.currentThread().getName();
    Region r = new DefRegion(100, 200);
    System.out.println("* ["+tn+"] r: "+ r);
    
    System.out.println("* ["+tn+"] rl.tryLock(r): "+ lock.lock(r));
    System.out.println("* ["+tn+"] "+ lock.forceUpdate());
    
    Region r2 = new DefRegion(300, 200);
    System.out.println("* ["+tn+"] r2: "+ r2);
    
    System.out.println("* ["+tn+"] rl.tryLock(r2): "+ lock.lock(r2));
    System.out.println("* ["+tn+"] "+ lock.forceUpdate());
    
    Region r3 = new DefRegion(150, 30);
    System.out.println("* ["+tn+"] r3: "+ r3);
    System.out.println("* ["+tn+"] rl.isLocked(r3): "+ lock.isLocked(r3));
    
    System.out.println("* ["+tn+"] rl.unlock(r);");
    lock.unlock(r);
    System.out.println("* ["+tn+"] rl.isLocked(r3): "+ lock.isLocked(r3));
    System.out.println("* ["+tn+"] rl.tryLock(r3): "+ lock.lock(r3));
    System.out.println("* ["+tn+"] "+ lock.forceUpdate());
  }

  
  public static void main(String[] args) {
    ByteBuffer buf = ByteBuffer.allocate(256);
    
    RegionLock lock = RegionLock.of(buf);
    new Thread(()->locker(lock), "T-0").start();
    new Thread(()->locker(lock), "T-1").start();
    new Thread(()->locker(lock), "T-2").start();
    //new Thread(()->locker(lock), "T-3").start();
    //new Thread(()->locker(lock), "T-4").start();
    //new Thread(()->locker(lock), "T-5").start();
    //new Thread(()->locker(lock), "T-6").start();
    //new Thread(()->locker(lock), "T-7").start();
    //new Thread(()->locker(lock), "T-8").start();
    //new Thread(()->locker(lock), "T-9").start();
  }
  
}
