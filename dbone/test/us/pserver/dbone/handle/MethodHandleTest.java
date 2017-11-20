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

package us.pserver.dbone.handle;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;
import us.pserver.dbone.bean.AObj;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/11/2017
 */
public class MethodHandleTest {
  
  private Object rec;
  
  private final int times = 100_000;
  
  private final PrintStream ps = printStream("/dev/null");

  private final AObj a = new AObj("hello", 37, new int[]{1, 2, 3}, new char[]{'a', 'b', 'c'}, new Date());
  
  
  private PrintStream printStream(String file) {
    try {
      return new PrintStream(file);
    }
    catch(FileNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  @Test
  public void UNreflectGetterOnAObj() throws Throwable {
    Field f = AObj.class.getDeclaredField("age");
    f.setAccessible(true);
    MethodHandle mh = MethodHandles.lookup().unreflectGetter(f);
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      int ret = (int)mh.invokeExact(a);
      ps.print(ret);
      Assert.assertEquals(37, ret);
    }
    System.out.printf("UNreflectGetterOnAObj: %s%n", tm.stop());
  }
  
  public void receive(Object obj) {
    rec = obj;
  }
  
  @Test
  public void reflectGetterOnAObj() throws Throwable {
    Field f = AObj.class.getDeclaredField("age");
    f.setAccessible(true);
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      Object ret = f.get(a);
      ps.print(ret);
      Assert.assertEquals(37, ret);
    }
    System.out.printf("reflectGetterOnAObj: %s%n", tm.stop());
  }
  
}
