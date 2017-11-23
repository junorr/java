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

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hervian.lambda.Lambda;
import com.hervian.lambda.LambdaFactory;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
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
  
  private final int times = 1_000_000;
  
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
      Assert.assertEquals(37, ret);
    }
    System.out.printf(". UNreflectGetterOnAObj: %s%n", tm.stop());
  }
  
  @Test
  public void reflectGetterOnAObj() throws Throwable {
    Field f = AObj.class.getDeclaredField("age");
    f.setAccessible(true);
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      Object ret = f.get(a);
      Assert.assertEquals(37, ret);
    }
    System.out.printf(". reflectGetterOnAObj: %s%n", tm.stop());
  }
  
  @Test
  public void mhExactHashCode() throws Throwable {
    Method[] mts = AObj.class.getDeclaredMethods();
    Method hashCode = Arrays.asList(mts).stream().filter(m->m.getName().equals("hashCode")).findFirst().get();
    MethodHandle mh = MethodHandles.lookup().findVirtual(
        hashCode.getDeclaringClass(), 
        hashCode.getName(), 
        MethodType.methodType(hashCode.getReturnType())
    );
    System.out.println("-> mhExactHashCode.toString: "+ mh.toString());
    System.out.println("-> mhExactHashCode.type: "+ mh.type());
    int expectedHash = -624836825;
    long result = 0;
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      int hash = (int) mh.invokeExact(a);
      Assert.assertEquals(expectedHash, hash);
      result += hash;
    }
    System.out.println("-> mhExactHashCode: "+ tm.stop());
    System.out.println(result);
  }
  
  @Test
  public void refAsmHashCode() throws Throwable {
    Method[] mts = AObj.class.getDeclaredMethods();
    Method hashCode = Arrays.asList(mts).stream().filter(m->m.getName().equals("hashCode")).findFirst().get();
    MethodAccess mac = MethodAccess.get(hashCode.getDeclaringClass());
    int hci = mac.getIndex("hashCode");
    int expectedHash = -624836825;
    long result = 0;
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      int hash = (int) mac.invoke(a, hci, null);
      Assert.assertEquals(expectedHash, hash);
      result += hash;
    }
    System.out.println("-> refAsmHashCode: "+ tm.stop());
    System.out.println(result);
  }
  
  @Test
  public void mhVirtualHashCode() throws Throwable {
    Method[] mts = AObj.class.getDeclaredMethods();
    Method hashCode = Arrays.asList(mts).stream().filter(m->m.getName().equals("hashCode")).findFirst().get();
    MethodHandle mh = MethodHandles.lookup().findVirtual(
        hashCode.getDeclaringClass(), 
        hashCode.getName(), 
        MethodType.methodType(hashCode.getReturnType())
    );
    int expectedHash = -624836825;
    long result = 0;
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      Object hash = mh.invoke(a);
      Assert.assertEquals(expectedHash, hash);
      result += (int) hash;
    }
    System.out.println("-> mhVirtualHashCode: "+ tm.stop());
    System.out.println(result);
  }
  
  @Test
  public void reflectHashCode() throws Throwable {
    Method[] mts = AObj.class.getDeclaredMethods();
    Method hashCode = Arrays.asList(mts).stream().filter(m->m.getName().equals("hashCode")).findFirst().get();
    int expectedHash = -624836825;
    long result = 0;
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      Object hash = hashCode.invoke(a, null);
      Assert.assertEquals(expectedHash, hash);
      //result += hash;
    }
    System.out.println("-> reflectHashCode: "+ tm.stop());
    System.out.println(result);
  }
  
  @Test
  public void lambdaHashCode() throws Throwable {
    Method[] mts = AObj.class.getDeclaredMethods();
    Method hashCode = Arrays.asList(mts).stream().filter(m->m.getName().equals("hashCode")).findFirst().get();
    Lambda lm = LambdaFactory.create(hashCode);
    int expectedHash = -624836825;
    long result = 0;
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      int hash = lm.invoke_for_int(a);
      Assert.assertEquals(expectedHash, hash);
      result += hash;
    }
    System.out.println("-> lamdaHashCode: "+ tm.stop());
    System.out.println(result);
  }
  
}
