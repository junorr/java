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

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import us.pserver.dbone.ObjectUIDFactory;
import us.pserver.dbone.bean.AObj;
import us.pserver.dbone.bean.BObj;
import us.pserver.dbone.serial.GsonSerializationService;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/11/2017
 */
public class DefaultVolumeTest {

  private static final AObj a = new AObj("Aobj", 123, new int[]{1,2,3}, new char[]{'a','b','c'}, new Date());
  
  private static final BObj b = new BObj("Bobj", a, Arrays.asList(1,2,3));
  
  private static final Path path = Paths.get("/home/juno/dbone");
  
  //private static final Storage store = new MockStorage(512);
  
  private static final Storage store = new FileStorage(path, 512, ByteBuffer::allocateDirect);
  
  private static final Volume vol = new DefaultVolume(store, new GsonSerializationService(), ByteBuffer::allocateDirect);
  
  private final StoreUnit storeUnit = StoreUnit.of(ObjectUIDFactory.create(b), b);
  
  
  @AfterClass
  public static void closeVolume() {
    vol.close();
  }
  
  
  @Test
  public void writeReadConsistency() {
    Region r = vol.put(storeUnit);
    System.out.printf("writeReadConsistency.put: %s%n", r);
    StoreUnit readUnit = vol.get(r);
    System.out.printf("writeReadConsistency.get: %s%n", readUnit);
    Assert.assertEquals(storeUnit, readUnit);
  }
  
}
