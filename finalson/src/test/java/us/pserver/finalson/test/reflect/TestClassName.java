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

package us.pserver.finalson.test.reflect;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public class TestClassName {

  private final Class intClass = int.class;
  
  private final Class intArrayClass = int[].class;
  
  private final Class stringClass = String.class;
  
  @Test
  public void intClassNames() {
    System.out.println("* intClass:");
    System.out.println("  name="+ intClass.getName());
    System.out.println("  canonicalName="+ intClass.getCanonicalName());
    System.out.println("  simpleName="+ intClass.getSimpleName());
    System.out.println("  toString="+ intClass.toString());
  }
  
  @Test
  public void intArrayClassNames() {
    System.out.println("* intArrayClass:");
    System.out.println("  name="+ intArrayClass.getName());
    System.out.println("  canonicalName="+ intArrayClass.getCanonicalName());
    System.out.println("  simpleName="+ intArrayClass.getSimpleName());
    System.out.println("  toString="+ intArrayClass.toString());
  }
  
  @Test
  public void stringClassNames() {
    System.out.println("* stringClass:");
    System.out.println("  name="+ stringClass.getName());
    System.out.println("  canonicalName="+ stringClass.getCanonicalName());
    System.out.println("  simpleName="+ stringClass.getSimpleName());
    System.out.println("  toString="+ stringClass.toString());
  }
  
  @Test
  public void intArrayForName() throws ClassNotFoundException {
    String name = intArrayClass.getName();
    System.out.println("* intArrayClass='"+ name+ "'");
    System.out.println(Class.forName(name));
    //System.out.println(this.getClass().getClassLoader().loadClass(name));
  }
  
}
