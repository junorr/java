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

package us.pserver.orb.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public class DefaultMethodTransform implements MethodTransform<Object> {
  
  @Override
  public boolean canHandle(InvocationContext ctx) {
    return ctx.getMethod().isDefault();
  }
  
  public Object invokeJava8(InvocationContext ctx) {
    try {
      Constructor<Lookup> constructor = Lookup.class
          .getDeclaredConstructor(Class.class);
      constructor.setAccessible(true);
      Object instance = constructor.newInstance(ctx.getMethod().getDeclaringClass());
      System.out.println(System.getProperty("java.version"));
      MethodType mt = MethodType.methodType(ctx.getMethod().getReturnType(), ctx.getMethod().getParameterTypes());
      return constructor.newInstance(ctx.getMethod().getDeclaringClass())
          .in(ctx.getMethod().getDeclaringClass())
          .unreflectSpecial(ctx.getMethod(), ctx.getMethod().getDeclaringClass())
          .bindTo(ctx.getProxyInstance())
          .invokeWithArguments(ctx.getArguments());
    } 
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  public Object invokeJavaHigher(InvocationContext ctx) {
    try {
      MethodType mt = MethodType.methodType(
          ctx.getMethod().getReturnType(), 
          ctx.getMethod().getParameterTypes()
      );
      return MethodHandles.lookup()
          .findSpecial(
              ctx.getMethod().getDeclaringClass(), 
              ctx.getMethod().getName(), mt, 
              ctx.getMethod().getDeclaringClass())
          .bindTo(ctx.getProxyInstance())
          .invokeWithArguments(ctx.getArguments());
    } 
    catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  @Override
  public Object apply(InvocationContext ctx) {
    String sver = System.getProperty("java.version");
    double version = 0;
    if(sver.contains(".")) {
      int fd = sver.indexOf(".", 0);
      version = Double.parseDouble(sver.substring(0, fd + 2));
    }
    else {
      version = Double.parseDouble(sver.substring(0, 2));
    }
    System.out.println(version);
    if(1.8 == version) {
      return invokeJava8(ctx);
    }
    else if(version > 1.8) {
      return invokeJavaHigher(ctx);
    }
    else {
      throw new IllegalStateException("Unsupported Java version: "+ sver);
    }
  }
  
}
