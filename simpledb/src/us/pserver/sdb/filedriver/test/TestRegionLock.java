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

  
  public static void main(String[] args) {
    ByteBuffer buf = ByteBuffer.allocate(256);
    System.out.println(buf.remaining());
    
    RegionLock rl = RegionLock.of(buf);
    System.out.println("* rl.locks(): "+ rl.locks());
    
    Region r = new DefRegion(100, 200);
    System.out.println("* r: "+ r);
    
    System.out.println("* rl.tryLock(r): "+ rl.tryLock(r));
    System.out.println("* rl.locks(): "+ rl.locks());
    
    Region r2 = new DefRegion(300, 200);
    System.out.println("* r2: "+ r2);
    
    System.out.println("* rl.tryLock(r2): "+ rl.tryLock(r2));
    System.out.println("* rl.locks(): "+ rl.locks());
    
    Region r3 = new DefRegion(150, 30);
    System.out.println(r3);
    System.out.println("* rl.isLocked(r3): "+ rl.isLocked(r3));
    
    System.out.println("* rl.unlock(r);");
    rl.unlock(r);
    System.out.println("* rl.isLocked(r3): "+ rl.isLocked(r3));
    System.out.println("* rl.tryLock(r3): "+ rl.tryLock(r3));
    System.out.println("* rl.locks(): "+ rl.locks());
  }
  
}
