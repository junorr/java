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

package us.pserver.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/09/2017
 */
public class SortedList<T> implements List<T> {

  private final List<T> list;
  
  private final Comparator<T> compare;
  
  
  public SortedList(List<T> lst, Comparator<T> cmp) {
    this.compare = cmp;
    this.list = NotMatch.notNull(lst).getOrFail("Bad null List");
  }
  
  
  public SortedList(Comparator<T> cmp) {
    this(new ArrayList<>(), cmp);
  }
  
  
  public SortedList(List<T> lst) {
    this(lst, null);
  }
  
  
  public SortedList() {
    this(new ArrayList<>(), null);
  }
  
  
  public SortedList<T> sort() {
    if(compare == null) {
      Collections.sort((List)list);
    } else {
      Collections.sort(list, compare);
    }
    return this;
  }
  
  
  @Override
  public int size() {
    return list.size();
  }


  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }


  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }


  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }


  @Override
  public Object[] toArray() {
    return list.toArray();
  }


  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }
  
  
  public SortedList<T> put(T e) {
    list.add(NotMatch.notNull(e).getOrFail("Bad null Element"));
    return sort();
  }


  @Override
  public boolean add(T e) {
    boolean b = list.add(NotMatch.notNull(e).getOrFail("Bad null Element"));
    if(b) this.sort();
    return b;
  }


  @Override
  public boolean remove(Object o) {
    return list.remove(NotMatch.notNull(o).getOrFail("Bad null Element"));
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }


  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean b = list.addAll(c);
    if(b) this.sort();
    return b;
  }


  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean b = list.addAll(index, c);
    if(b) this.sort();
    return b;
  }


  @Override
  public boolean removeAll(Collection<?> c) {
    boolean b = list.removeAll(c);
    if(b) this.sort();
    return b;
  }


  @Override
  public boolean retainAll(Collection<?> c) {
    boolean b = list.retainAll(c);
    if(b) this.sort();
    return b;
  }


  @Override
  public void clear() {
    list.clear();
  }


  @Override
  public T get(int index) {
    return list.get(index);
  }


  @Override
  public T set(int index, T element) {
    T t = list.set(index, element);
    if(t != null) this.sort();
    return t;
  }


  @Override
  public void add(int index, T element) {
    list.add(index, element);
    this.sort();
  }


  @Override
  public T remove(int index) {
    return list.remove(index);
  }


  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }


  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }


  @Override
  public ListIterator<T> listIterator() {
    return list.listIterator();
  }


  @Override
  public ListIterator<T> listIterator(int index) {
    return list.listIterator(index);
  }


  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

}
