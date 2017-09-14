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

import java.util.Date;
import java.util.LinkedList;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectClassMapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/09/2017
 */
public class TestObjectClassMapper {

  
  public static void main(String[] args) {
    ObjectClassMapper mapper = new ObjectClassMapper();
    //AObj a = new AObj("hello", 5, new int[] {5,4,3,2,1,0}, new char[] {'a', 'b', 'c', 'd'}, new Date());
    AObj a = new AObj("hello", new Date());
    LinkedList<Integer> ls = new LinkedList<>();
    ls.add(10);
    ls.add(9);
    ls.add(8);
    ls.add(7);
    ls.add(6);
    ls.add(5);
    BObj b = new BObj("world", a, ls);
    System.out.println("* a: "+ a);
    System.out.println("* b: "+ b);
    MappedValue omp = mapper.map(a);
    System.out.println("* a.mapped  : "+ omp);
    a = mapper.unmap(omp);
    System.out.println("* a.unmapped: "+ a);
    omp = mapper.map(b);
    System.out.println("* b.mapped  : "+ omp);
    b = mapper.unmap(omp);
    System.out.println("* b.unmapped: "+ b);
  }
  
}
