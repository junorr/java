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

package us.pserver.tools.test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/03/2018
 */
public class TestType {

  
  public static void main(String[] args) {
    String s = "hello";
    printClassType(toClass(s));
    int i = 50;
    printClassType(toClass(i));
    int[] ii = {1,2,3};
    printClassType(toClass(ii));
  }
  
  public static Class toClass(Object obj) {
    return obj.getClass();
  }
  
  public static void printClassType(Type tp) {
    System.out.printf("* TypeName: %s%n", tp.getTypeName());
  }
  
  public static void printTypes(TypeVariable tv) {
    Type[] tps = tv.getBounds();
    System.out.printf("* Bounds of TypeVariable {%s}: %d%n", tv.getName(), tps.length);
    for(int i = 0; i < tps.length; i++) {
      System.out.printf("  %d. %s%n", i, tps[i].getTypeName());
    }
  }
  
}
