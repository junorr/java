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

package br.com.bb.disec.microb.test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/07/2017
 */
public class TestTypeImpl {

  private final Object obj;
  
  private final Class cls;
  
  
  public TestTypeImpl(Object obj) {
    this.obj = obj;
    this.cls = null;
  }
  
  
  public TestTypeImpl(Class cls) {
    this.obj = null;
    this.cls = cls;
  }
  
  
  public void testTypes() {
    if(obj != null) {
      System.out.println("* Value assigned to [Object]");
    }
    else if(cls != null) {
      System.out.println("* Value assigned to [Class]");
    }
  }
  
  
  public static TestTypeImpl of(Object obj) {
    return new TestTypeImpl(obj);
  }
  
  
  public static TestTypeImpl of(Class cls) {
    return new TestTypeImpl(cls);
  }
  
  
  static class TypeProvider<T> {
    
    private final T val;
    
    public TypeProvider(T t) {
      this.val = t;
    }
    
    public T get() {
      return val;
    }
    
    public static <U> TypeProvider<U> of(U u) {
      return new TypeProvider(u);
    }
    
  }
  
  
  public static void main(String[] args) {
    TypeProvider<Object> obp = TypeProvider.of(new Object());
    TestTypeImpl.of(obp.get()).testTypes();
    TypeProvider<Class> clp = TypeProvider.of(Object.class);
    TestTypeImpl.of(clp.get()).testTypes();
  }
  
}
