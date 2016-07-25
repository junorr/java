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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class ArrayMapper<T> implements Mapper<T[]> {

  @Override
  public Node map(T[] t) {
    Node n = null;
    if(t != null && t.length > 0) {
			List list = new LinkedList();
			list.addAll(Arrays.asList(t));
      n = new CollectionMapper().map(list);
    }
    return n;
  }


  @Override
  public T[] unmap(Node n, Class cls) {
    T[] t = null;
    if(n != null) {
      Collection list = new CollectionMapper().unmap(n, LinkedList.class);
			int i = 0;
			for(Object o : list) {
				if(t == null) {
					t = (T[]) Array.newInstance(o.getClass(), list.size());
				}
				t[i++] = (T) o;
			}
    }
    return t;
  }
	
	
	@Override
	public boolean canHandle(Class cls) {
		return cls != null 
				&& cls.isArray() 
				&& !cls.getComponentType().isPrimitive();
	}
  
}
