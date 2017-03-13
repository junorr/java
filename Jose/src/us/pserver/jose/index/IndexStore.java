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

package us.pserver.jose.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;
import us.pserver.jose.Region;
import us.pserver.jose.index.Index.IndexImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/12/2016
 */
public interface IndexStore<T> {

  public List<Index<T>> getStore(String name);
  
  public IndexQuery<T> query();
  
  public IndexUpdate<T> update();
  
  public IndexStore<T> insert(Index<T> idx);
  
  public int insertMany(Index<T> ... idxs);
  
  public IndexBuilder<T> newIndex();
  
  
  public static <U> IndexStore<U> newStore() {
    return new IndexStoreImpl();
  }
  
  
  
  
  
  public static final class IndexBuilder<T> {
    
    private final IndexStore<T> store;
    
    private final List<Region> regions;
    
    private String name;
    
    private T value;
    
    
    public IndexBuilder(IndexStore<T> store) {
      this.store = Sane.of(store)
          .with("Bad Null IndexStore")
          .get(Checkup.isNotNull());
      regions = new ArrayList<>();
    }
    
    
    public IndexBuilder<T> withName(String name) {
      this.name = name;
      return this;
    }
    
    
    public IndexBuilder<T> withValue(T value) {
      this.value = value;
      return this;
    }
    
    
    public IndexBuilder<T> addRegion(Region r) {
      if(r != null) {
        regions.add(r);
      }
      return this;
    }
    
    
    public IndexBuilder<T> addAll(Collection<Region> cs) {
      if(cs != null) {
        regions.addAll(cs);
      }
      return this;
    }
    
    
    public IndexStore<T> insert() {
      return store.insert(new IndexImpl<>(name, value, regions));
    }
    
  }
  
  
  
  
  
  public static class IndexStoreImpl<T> implements IndexStore<T> {
    
    private final Map<String, List<Index<T>>> store;
    
    private final ReentrantLock lock;
    
    
    public IndexStoreImpl() {
      store = Collections.synchronizedMap(new TreeMap<>());
      lock = new ReentrantLock();
    }
    

    @Override
    public List<Index<T>> getStore(String name) {
      if(name == null || name.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      return store.get(name);
    }
    
    
    @Override
    public IndexQuery<T> query() {
      return IndexQuery.of(store, lock);
    }
    
    
    @Override
    public IndexUpdate<T> update() {
      return IndexUpdate.of(store, lock);
    }


    @Override
    public IndexStore<T> insert(Index<T> idx) {
      this.insertMany(idx);
      return this;
    }
    
    
    @Override
    public IndexBuilder<T> newIndex() {
      return new IndexBuilder<>(this);
    }


    @Override
    public int insertMany(Index<T>... idxs) {
      int count = 0;
      if(idxs == null || idxs.length < 1) {
        return count;
      }
      lock.lock();
      try {
        for(Index<T> i : idxs) {
          if(i != null && i.getName() != null) {
            List<Index<T>> lst = (store.containsKey(i.getName()) 
                ? store.get(i.getName()) 
                : new ArrayList<>()
            );
            lst.add(i);
            store.put(i.getName(), lst);
            count++;
          }
        }
        return count;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public String toString() {
      lock.lock();
      try {
        String n = "\n";
        String q = "\"";
        String c = ":";
        String s = "   ";
        StringBuilder sb = new StringBuilder("IndexStore{ size=")
            .append(store.size());
        for(Entry<String, List<Index<T>>> e : store.entrySet()) {
          sb.append(n)
              .append(s)
              .append(q)
              .append(e.getKey())
              .append("\": ");
          e.getValue().forEach(i->sb.append("{")
              .append(q)
              .append(i.getValue())
              .append("\", ")
              .append(i.regions())
              .append("}, ")
          );
          if(", ".equals(sb.substring(sb.length() -2))) {
            sb.delete(sb.length() -2, sb.length());
          }
        }
        return sb.append("\n}").toString();
      }
      finally {
        lock.unlock();
      }
    }

  }
  
}
