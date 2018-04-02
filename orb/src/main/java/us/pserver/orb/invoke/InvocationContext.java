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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public interface InvocationContext {

  public Method getMethod();
  
  public Object getValue();
  
  public Object getProxyInstance();
  
  public List getArguments();
  
  
  public InvocationContext method(Method meth);
  
  public InvocationContext value(Object value);
  
  public InvocationContext proxy(Object proxy);
  
  public InvocationContext arguments(List args);
  
  
  public static InvocationContext of(Object proxy, Method meth, List args, Object value) {
    return new Default(proxy, meth, args, value);
  }
  
  
  
  
  
  public static class Default implements InvocationContext {
    
    private final Method meth;
    
    private final Object value;
    
    private final Object proxy;
    
    private final List args;
    
    public Default(Object proxy, Method meth, List args, Object value) {
      this.proxy = Match.notNull(proxy).getOrFail("Bad null proxy Object");
      this.meth = Match.notNull(meth).getOrFail("Bad null Method");
      this.args = args != null ? args : Collections.EMPTY_LIST;
      this.value = value;
    }

    @Override
    public Method getMethod() {
      return meth;
    }
    
    @Override
    public Object getValue() {
      return value;
    }
    
    @Override
    public Object getProxyInstance() {
      return proxy;
    }
    
    @Override
    public List getArguments() {
      return args;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 97 * hash + Objects.hashCode(this.meth);
      hash = 97 * hash + Objects.hashCode(this.value);
      hash = 97 * hash + Objects.hashCode(this.proxy);
      return hash;
    }


    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Default other = (Default) obj;
      if (!Objects.equals(this.meth, other.meth)) {
        return false;
      }
      if (!Objects.equals(this.value, other.value)) {
        return false;
      }
      if (!Objects.equals(this.proxy, other.proxy)) {
        return false;
      }
      return true;
    }
    
    @Override
    public String toString() {
      return "Default{" + "meth=" + meth + ", value=" + value + '}';
    }
    
    @Override
    public InvocationContext method(Method meth) {
      return new Default(proxy, meth, args, value);
    }
    
    @Override
    public InvocationContext value(Object value) {
      return new Default(proxy, meth, args, value);
    }
    
    @Override
    public InvocationContext proxy(Object proxy) {
      return new Default(proxy, meth, args, value);
    }
    
    @Override
    public InvocationContext arguments(List args) {
      return new Default(proxy, meth, args, value);
    }
    
  }
  
}
