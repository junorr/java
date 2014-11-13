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

package us.pserver.sdb.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;
import us.pserver.sdb.Document;
import us.pserver.sdb.Query;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/10/2014
 */
public class Result implements List<Document>, Iterator<Document> {

  private List<Document> list;
  
  private int index;
  
  private String orderby;
  
  
  public Result() {
    list = new ArrayList<>();
    index = 0;
    orderby = null;
  }
  
  
  public Result(int size) {
    if(size <= 0) size = 5;
    list = new ArrayList<>(size);
    index = 0;
    orderby = null;
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
  public Iterator<Document> iterator() {
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
  
  
  public Document[] toDocArray() {
    Document[] ds = new Document[list.size()];
    return list.toArray(ds);
  }
  
  
  public boolean containsBlock(long blk) {
    return indexOfBlock(blk) >= 0;
  }
  
  
  public int indexOfBlock(long blk) {
    if(blk < 0) return -1;
    for(int i = 0; i < list.size(); i++) {
      if(blk == list.get(i).block())
        return i;
    }
    return -1;
  }


  @Override
  public boolean add(Document e) {
    if(e != null && !containsBlock(e.block()))
      return list.add(e);
    return false;
  }


  @Override
  public boolean remove(Object o) {
    if(o == null) return false;
    return list.remove(o);
  }


  public Document removeBlock(long blk) {
    if(blk < 0) return null;
    int id = indexOfBlock(blk);
    if(id < 0) return null;
    return list.remove(id);
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }


  @Override
  public boolean addAll(Collection<? extends Document> c) {
    return list.addAll(c);
  }


  @Override
  public boolean addAll(int index, Collection<? extends Document> c) {
    return list.addAll(index, c);
  }


  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }


  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }


  @Override
  public void clear() {
    list.clear();
  }


  @Override
  public Document get(int index) {
    return list.get(index);
  }


  @Override
  public Document set(int index, Document element) {
    return list.set(index, element);
  }


  @Override
  public void add(int index, Document element) {
    list.add(index, element);
  }


  @Override
  public Document remove(int index) {
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
  public ListIterator<Document> listIterator() {
    return list.listIterator();
  }


  @Override
  public ListIterator<Document> listIterator(int index) {
    return list.listIterator(index);
  }


  @Override
  public List<Document> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }
  
  
  @Override
  public Stream<Document> stream() {
    return list.stream();
  }
  
  
  public List<Long> blocks() {
    List<Long> ls = Collections.EMPTY_LIST;
    if(list.isEmpty()) 
      return ls;
    ls = new ArrayList<>(list.size());
    for(Document d : list)
      ls.add(d.block());
    return ls;
  }


  @Override
  public boolean hasNext() {
    return index < list.size();
  }


  @Override
  public Document next() {
    if(index >= list.size())
      index = 0;
    return list.get(index++);
  }
  
  
  public Result orderBy(String key) {
    orderby = key;
    return this;
  }
  
  
  public boolean asc() {
    return ascending();
  }
  
  
  public boolean ascending() {
    if(orderby == null || list.isEmpty()) 
      return false;
    Comparator<Document> cp = new Comparator<Document>() {
      @Override
      public int compare(Document d1, Document d2) {
        Object o1 = d1.get(orderby);
        Object o2 = d2.get(orderby);
        if(o1 == null && o2 == null)
          return 0;
        else if(o1 == null)
          return 1;
        else if(o2 == null)
          return -1;
        else if(o1 instanceof Comparable<?> && o2 instanceof Comparable<?>) {
          return ((Comparable)o1).compareTo(o2);
        }
        else {
          Object[] obs = new Object[2];
          obs[0] = o1;
          obs[1] = o2;
          Arrays.sort(obs);
          if(obs[0] == o1) return -1;
          else return 1;
        }
      }
    };
    Collections.sort(list, cp);
    return true;
  }
  
  
  public boolean desc() {
    return descending();
  }
  
  
  public boolean descending() {
    if(orderby == null || list.isEmpty()) 
      return false;
    Comparator<Document> cp = new Comparator<Document>() {
      @Override
      public int compare(Document d1, Document d2) {
        Object o1 = d1.get(orderby);
        Object o2 = d2.get(orderby);
        if(o1 == null && o2 == null)
          return 0;
        else if(o1 == null)
          return -1;
        else if(o2 == null)
          return 1;
        else if(o1 instanceof Comparable<?> && o2 instanceof Comparable<?>) {
          return (((Comparable)o1).compareTo(o2) * -1);
        }
        else {
          Object[] obs = new Object[2];
          obs[0] = o1;
          obs[1] = o2;
          Arrays.sort(obs);
          if(obs[0] == o1) return 1;
          else return -1;
        }
      }
    };
    Collections.sort(list, cp);
    return true;
  }
  
  
  public Result filter(Query1 q) {
    return filter(q, this);
  }
 
  
  private Result filter(Query1 q, List<Document> list) {
    Result docs = new Result();
    if(q == null || list == null || list.isEmpty()) 
      return docs;
    
    for(int i = 0; i < list.size(); i++) {
      if(q.exec(list.get(i)))
        docs.add(list.get(i));
    }
    return docs;
  }
  

  public Result notIn(List<Document> list) {
    Result dif = new Result();
    if(this.isEmpty() || list == null || list.isEmpty())
      return dif;
    
    for(Document d : list) {
      if(!this.contains(d) && !dif.contains(d))
        dif.add(d);
    }
    return dif;
  }
  
}
