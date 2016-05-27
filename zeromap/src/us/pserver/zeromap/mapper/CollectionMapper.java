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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ClassFactory;
import us.pserver.zeromap.impl.ONode;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class CollectionMapper implements Mapper<Collection> {

  @Override
  public Node map(Collection t) {
    Node n = null;
    if(t != null) {
			Iterator it = t.iterator();
			while(it.hasNext()) {
				Object o = it.next();
				if(n == null) {
          n = new ONode(t.getClass().getName());
				}
        Node entry = new ONode("list-entry");
        entry.newChild("type").add(o.getClass().getName());
				Mapper mp = MapperFactory.factory().mapper(o.getClass());
        entry.newChild("value").add(mp.map(o));
        n.add(entry);
			}
			if(n == null) {
				n = new ONode(t.getClass().getName());
			}
    }
    return n;
  }


  public Collection unmap(Node n, Class<? extends Collection> cls) {
    Collection col = null;
    Class<? extends Collection> cclass = ClassFactory.create(n.value());
    try {
      col = cclass.newInstance();
    } catch(IllegalAccessException | InstantiationException e) {
      throw new IllegalStateException("Can not create Collection type: "+ cls, e);
    }
    for(Node e : n.childs()) {
      if(!"list-entry".equals(e.value())) continue;
      Optional<Node> otype = e.findChild("type");
      if(!otype.isPresent()) continue;
      String stype = otype.get().firstChild().get().value();
      Optional<Node> ovalue = e.findChild("value");
      if(!ovalue.isPresent()) continue;
      Class type = ClassFactory.create(stype);
      Mapper mp = MapperFactory.factory().mapper(type);
      col.add(mp.unmap(ovalue.get().firstChild().get(), type));
    }
    return col;
  }

	
  public Collection unmap2(Node n, Class<? extends Collection> cls) {
    List l = new LinkedList();
		Collection col = null;
    if(n != null) {
      Class type = ClassFactory.create(n.value());
      Mapper mp = MapperFactory.factory().mapper(type);
      for(Node nd : n.childs()) {
        l.add(mp.unmap(nd, type));
      }
			String sclass = n.value().substring(0, n.value().indexOf("|"));
			Class<? extends Collection> cclass = ClassFactory.create(sclass);
			try {
				col = cclass.newInstance();
			} catch(IllegalAccessException | InstantiationException e) {
				throw new IllegalStateException("Can not create Collection type: "+ cls, e);
			}
			col.addAll(l);
    }
    return col;
  }

	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& Collection.class.isAssignableFrom(cls); 
	}

}
