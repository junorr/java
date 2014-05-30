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

package com.jpower.csv;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class ListContainer<T> {
  
  protected List<T> list;
  
  
  public ListContainer() {
    list = new LinkedList<>();
  }
  
  
  public ListContainer(List<T> ls) {
    if(ls == null)
      throw new IllegalArgumentException(
          "Invalid list: "+ ls);
    list = ls;
  }
  
  
  public List<T> list() {
    return Collections.unmodifiableList(list);
  }
  
  
  public int size() {
    return list.size();
  }
  
  
  public boolean isEmpty() {
    return list.isEmpty();
  }
  
  
  public boolean remove(T t) {
    return list.remove(t);
  }
  
  
  public T remove(int idx) {
    return list.remove(idx);
  }
  
  
  public T get(int idx) {
    if(idx >= 0 && idx < list.size())
      return list.get(idx);
    return null;
  }
  
  
  public boolean add(T t) {
    if(t == null) return false;
    return list.add(t);
  }
  
  
  public T set(int idx, T t) {
    if(idx >= 0 && t != null)
      return list.set(idx, t);
    return null;
  }

}
