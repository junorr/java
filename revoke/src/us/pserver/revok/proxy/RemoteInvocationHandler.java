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

package us.pserver.revok.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import us.pserver.revok.MethodInvocationException;
import us.pserver.revok.RemoteMethod;
import us.pserver.revok.RemoteObject;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/04/2015
 */
public class RemoteInvocationHandler implements InvocationHandler {

  private RemoteObject rob;
  
  private String namespace;
  
  
  public RemoteInvocationHandler(RemoteObject rob, String namespace) {
    if(rob == null)
      throw new IllegalArgumentException(
          "[RemoteInvocationHandler( RemoteObject )] "
              + "Invalid RemoteObject {"+ rob+ "}");
    if(namespace == null || namespace.trim().isEmpty())
      throw new IllegalArgumentException(
          "[RemoteInvocationHandler( RemoteObject )] "
              + "Invalid namespace {"+ rob+ "}");
    this.rob = rob;
    this.namespace = namespace;
  }
  
  
  public RemoteObject getRemoteObject() {
    return rob;
  }
  
  
  public String getNamespace() {
    return namespace;
  }
  
  
  private Class mapToNative(Class c) {
    if(c == null) return c;
    else if(c.equals(Boolean.class))
      return boolean.class;
    else if(c.equals(Byte.class))
      return byte.class;
    else if(c.equals(Short.class))
      return short.class;
    else if(c.equals(Integer.class))
      return int.class;
    else if(c.equals(Character.class))
      return char.class;
    else if(c.equals(Long.class))
      return long.class;
    else if(c.equals(Float.class))
      return float.class;
    else if(c.equals(Double.class))
      return double.class;
    else
      return c;
  }
  
  
  private boolean maybeNative(Class c) {
    return c.equals(Boolean.class)
        || c.equals(Byte.class)
        || c.equals(Short.class)
        || c.equals(Integer.class)
        || c.equals(Character.class)
        || c.equals(Long.class)
        || c.equals(Float.class)
        || c.equals(Double.class);
  }
  

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Class[] ints = proxy.getClass().getInterfaces();
    if(ints == null || ints.length == 0)
      throw new IllegalArgumentException("Invalid Proxy object. No implemented interfaces");
    RemoteMethod rm = new RemoteMethod()
        .forObject(namespace.concat(".")
            .concat(ints[0].getSimpleName()))
        .method(method.getName());
    if(args != null && args.length > 0) {
      rm.types(method.getParameterTypes()).params(args);
    }
    return rob.invoke(rm);
  }
  
}
