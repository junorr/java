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

import com.jpower.rfl.Reflector;
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
import us.pserver.sdb.OID;
import us.pserver.sdb.util.ObjectUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/10/2014
 */
public class ResultOID implements List<OID>, Iterator<OID> {

  private List<OID> list;
  
  private int index;
  
  private String orderby;
  
  
  public ResultOID() {
    list = new ArrayList<>();
    index = 0;
    orderby = null;
  }
  
  
  public ResultOID(int size) {
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
  public Iterator<OID> iterator() {
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
  
  
  public OID[] toObjectIDArray() {
    OID[] ds = new OID[list.size()];
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
  
  
  public OID addObj(Object obj) {
    OID id = new OID().set(obj);
    this.add(id);
    return id;
  }
  
  
  public OID addObj(Object obj, long block) {
    OID id = new OID(block, obj);
    this.add(id);
    return id;
  }


  @Override
  public boolean add(OID e) {
    if(e != null && !containsBlock(e.block()))
      return list.add(e);
    return false;
  }


  @Override
  public boolean remove(Object o) {
    if(o == null) return false;
    return list.remove(o);
  }


  public OID removeBlock(long blk) {
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
  public boolean addAll(Collection<? extends OID> c) {
    return list.addAll(c);
  }


  @Override
  public boolean addAll(int index, Collection<? extends OID> c) {
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
  public OID get(int index) {
    return list.get(index);
  }


  @Override
  public OID set(int index, OID element) {
    return list.set(index, element);
  }


  @Override
  public void add(int index, OID element) {
    list.add(index, element);
  }


  @Override
  public OID remove(int index) {
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
  public ListIterator<OID> listIterator() {
    return list.listIterator();
  }


  @Override
  public ListIterator<OID> listIterator(int index) {
    return list.listIterator(index);
  }


  @Override
  public List<OID> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }
  
  
  @Override
  public Stream<OID> stream() {
    return list.stream();
  }
  
  
  public List<Long> blocks() {
    List<Long> ls = Collections.EMPTY_LIST;
    if(list.isEmpty()) 
      return ls;
    ls = new ArrayList<>(list.size());
    for(OID d : list)
      ls.add(d.block());
    return ls;
  }


  @Override
  public boolean hasNext() {
    return index < list.size();
  }


  @Override
  public OID next() {
    if(index >= list.size())
      index = 0;
    return list.get(index++);
  }
  
  
  public ResultOID orderBy(String key) {
    orderby = key;
    return this;
  }
  
  
  public boolean asc() {
    return ascending();
  }
  
  
  public boolean ascending() {
    if(orderby == null || list.isEmpty()) 
      return false;
    Comparator<OID> cp = new Comparator<OID>() {
      private Reflector ref = new Reflector();
      @Override
      public int compare(OID d1, OID d2) {
        Object o1 = ref.on(d1.get()).field(orderby).get();
        Object o2 = ref.on(d2.get()).field(orderby).get();
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
    Comparator<OID> cp = new Comparator<OID>() {
      private Reflector ref = new Reflector();
      @Override
      public int compare(OID d1, OID d2) {
        Object o1 = ref.on(d1.get()).field(orderby).get();
        Object o2 = ref.on(d2.get()).field(orderby).get();
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
  
  
  public ResultOID filter(Query q) {
    return filter(q, this);
  }
 
  
  private ResultOID filter(Query q, List<OID> list) {
    ResultOID res = new ResultOID();
    Result docs = new Result();
    if(q == null || list == null || list.isEmpty()) 
      return res;
    
    for(OID oid : list) {
      if(oid == null || !oid.hasObject()) continue;
      Document d = ObjectUtils.toDocument(oid.get(), true);
      d.block(oid.block());
      if(q.exec(d)) res.add(oid);
      
      if(q.limit() > 0 && docs.size() >= q.limit()) 
        break;
    }//for
    
    return res;
  }
  

  public ResultOID notIn(List<OID> list) {
    ResultOID dif = new ResultOID();
    if(this.isEmpty() || list == null || list.isEmpty())
      return dif;
    
    for(OID o : list) {
      if(!this.contains(o) && !dif.contains(o))
        dif.add(o);
    }
    return dif;
  }


  @Override
  public String toString() {
    return "ResultOID{ size=" + list.size() + " }";
  }
  
}
