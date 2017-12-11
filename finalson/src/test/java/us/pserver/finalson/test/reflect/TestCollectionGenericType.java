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

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public class TestCollectionGenericType {

  private final List<Integer> list = new ArrayList<>();
  
  @Test
  public void getGenericListType() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    System.out.println(list.getClass().getGenericSuperclass());
    System.out.println(Arrays.toString(list.getClass().getGenericInterfaces()));
    TypeVariable[] tv = list.getClass().getTypeParameters();
    System.out.println(Arrays.toString(tv));
    for(int i = 0; i < tv.length; i++) {
      System.out.println(tv[i].getGenericDeclaration());
      System.out.println(Arrays.toString(tv[i].getBounds()));
    }
    list.clear();
  }
  
}
