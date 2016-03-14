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

package us.pserver.zeromap.mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class ListMapper implements Mapper<List> {

  @Override
  public Node map(List t) {
    Node n = null;
    if(t != null && !t.isEmpty()) {
      n = new ONode(t.get(0).getClass().getName());
      for(Object o : t) {
        Mapper mp = MapperFactory.mapper(o.getClass());
        n.add(mp.map(o));
      }
    }
    return n;
  }


  @Override
  public List unmap(Node n) {
    List l = new LinkedList();
    if(n != null) {
      Class cls = null;
      try {
        cls = Class.forName(n.value());
      } catch (ClassNotFoundException ex) {
        throw new RuntimeException(ex);
      }
      Mapper mp = MapperFactory.mapper(cls);
      for(Node nd : n.childs()) {
        l.add(mp.unmap(nd));
      }
    }
    return l;
  }

}
