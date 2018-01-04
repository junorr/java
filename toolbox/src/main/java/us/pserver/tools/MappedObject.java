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

package us.pserver.tools;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class MappedObject implements InvocationHandler {
  
  public static final Function<Method,String> GETTER_AS_DOTTED_KEY = new KeyMethodName('.', 3);
  
  public static final Function<Method,String> NAME_AS_DOTTED_KEY = new KeyMethodName('.', 0);
  
  public static final Function<Method,String> GETTER_AS_KEY = new KeyMethodName((char)0, 3);
  
  public static final Function<Method,String> NAME_AS_KEY = new KeyMethodName((char)0, 0);
  
  public static final Function<Method,String> GETTER_AS_UNDERSCORED_KEY = new KeyMethodName('_', 3);
  
  public static final Function<Method,String> NAME_AS_UNDERSCORED_KEY = new KeyMethodName('_', 0);
  
  private final StringMap map;
  
  private final Function<Method,String> methodToKey;
  
  public MappedObject(StringMap map, Function<Method,String> methodToKey) {
    this.map = NotMatch.notNull(map).getOrFail("Bad null StringMap");
    this.methodToKey = NotMatch.notNull(methodToKey).getOrFail("Bad null method name Function");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return null;
  }
  
  public Object getter(Object proxy, Method method) throws Throwable {
    
    return null;
  }
  
  public Object setter(Object proxy, Method method, Object[] args) throws Throwable {
    
    return null;
  }
  
  
  
  
  
  public static class KeyMethodName implements Function<Method,String> {
    
    private final char separator;
    
    private final int startAt;
    
    public KeyMethodName(char camelCaseSeparator, int startNameAt) {
      this.separator = camelCaseSeparator;
      this.startAt = startNameAt;
    }
    
    @Override
    public String apply(Method m) {
      String name = m.getName().substring(startAt);
      StringBuilder sb = new StringBuilder(name.substring(0, 1).toLowerCase());
      char[] cs = name.toCharArray();
      for(int i = 1; i < cs.length; i++) {
        if(Character.isUpperCase(cs[i]) && separator != 0) {
          sb.append(separator);
        }
        sb.append(Character.toLowerCase(cs[i]));
      }
      return sb.toString();
    }
    
  }

}
