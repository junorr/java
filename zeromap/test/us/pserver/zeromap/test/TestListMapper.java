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

package us.pserver.zeromap.test;

import java.util.LinkedList;
import java.util.List;
import us.pserver.date.SimpleDate;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.CollectionMapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class TestListMapper {

  public static void main(String[] args) {
    List list = new LinkedList();
    for(int i = 0; i < 10; i++) {
      list.add(i);
    }
    System.out.println("* list : "+ list);
    Mapper map = new CollectionMapper();
    Node nl = map.map(list);
    System.out.println("* map  : "+ nl);
    list = (List) map.unmap(nl);
    System.out.println("* unmap: "+ list);
    
    SimpleDate d = new SimpleDate().date(2016, 1, 1, 23, 59, 59);
    list.clear();
    for(int i = 1; i < 13; i++) {
      list.add(d.clone().month(i).hour(i));
    }
    
    System.out.println("* list : "+ list);
    map = new CollectionMapper();
    nl = map.map(list);
    System.out.println("* map  : "+ nl);
    list = (List) map.unmap(nl);
    System.out.println("* unmap: "+ list);
  }
  
}
