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

package us.pserver.sdb.filedriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/12/2016
 */
public interface IndexStore<T extends Serializable> extends Serializable {

  public List<Index<T>> getStore(String name);
  
  public List<Index<T>> find(String name, Predicate<T> prd);
  
  public List<Index<T>> find(Predicate<T> prd);
  
  public Optional<Index<T>> findOne(String name, Predicate<T> prd);
  
  public List<Index<T>> findByIndex(String name, Predicate<Index<T>> prd);
  
  public List<Index<T>> findByIndex(Predicate<Index<T>> prd);
  
  public IndexStore<T> insert(Index<T> idx);
  
  public IndexBuilder<T> newIndex();
  
  public int insertMany(Index<T> ... idxs);
  
  public List<Index<T>> remove(String name, Predicate<T> prd);
  
  public IndexStore<T> removeOne(String name, Predicate<T> prd);
  
  public List<Index<T>> removeByIndex(Predicate<Index<T>> prd);
  
  public int update(String name, Predicate<T> prd, T newValue);
  
  public boolean updateOne(String name, Predicate<T> prd, T newValue);
  
  public boolean updateByIndex(String name, Predicate<Index<T>> prd, Index<T> newIndex);
  
  
  public static <U extends Serializable> IndexStore<U> newStore() {
    return new DefIndexStore();
  }
  
  
  
  
  
  
  public static final class IndexBuilder<T extends Serializable> {
    
    private final IndexStore<T> store;
    
    private final Index.Builder<T> builder;
    
    
    public IndexBuilder(IndexStore<T> store) {
      this.store = Sane.of(store)
          .with("Bad Null IndexStore")
          .get(Checkup.isNotNull());
      builder = Index.builder();
    }
    
    
    public IndexBuilder<T> withName(String name) {
      builder.setName(name);
      return this;
    }
    
    
    public IndexBuilder<T> withValue(T value) {
      builder.setValue(value);
      return this;
    }
    
    
    public IndexBuilder<T> addRegion(Region r) {
      builder.addRegion(r);
      return this;
    }
    
    
    public IndexStore<T> insert() {
      return store.insert(builder.build());
    }
    
  }
  
  
  
  
  
  public static class DefIndexStore<T extends Serializable> implements IndexStore<T> {
    
    private final Map<String, List<Index<T>>> store;
    
    private final ReentrantLock lock;
    
    
    public DefIndexStore() {
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
    public List<Index<T>> find(String name, Predicate<T> prd) {
      if(name == null || name.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        return store.get(name).stream()
            .filter(i->prd.test(i.getValue()))
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> find(Predicate<T> prd) {
      lock.lock();
      try {
        return store.values().stream()
            .flatMap(l->l.stream())
            .filter(i->prd.test(i.getValue()))
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public Optional<Index<T>> findOne(String name, Predicate<T> prd) {
      if(name == null || name.isEmpty()) {
        return Optional.empty();
      }
      lock.lock();
      try {
        return store.get(name).stream()
            .filter(i->prd.test(i.getValue()))
            .findFirst();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> findByIndex(String name, Predicate<Index<T>> prd) {
      if(name == null 
          || name.isEmpty() 
          || !store.containsKey(name) 
          || prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        return store.get(name).stream()
            .filter(prd)
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> findByIndex(Predicate<Index<T>> prd) {
      if(prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        return store.values().stream()
            .flatMap(l->l.stream())
            .filter(prd)
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public IndexStore<T> insert(Index<T> idx) {
      insertMany(idx);
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
    public List<Index<T>> remove(String name, Predicate<T> prd) {
      lock.lock();
      try {
        List<Index<T>> res = find(name, prd);
        if(!res.isEmpty()) {
          store.get(res).removeAll(res);
        }
        return res;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public IndexStore<T> removeOne(String name, Predicate<T> prd) {
      lock.lock();
      try {
        findOne(name, prd).ifPresent(i->store.get(name).remove(i));
        return this;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public int update(String name, Predicate<T> prd, T newValue) {
      lock.lock();
      try {
        int count = 0;
        List<Index<T>> res = find(name, prd);
        if(res.isEmpty()) return count;
        List<Index<T>> lst = store.get(name);
        for(Index<T> i : res) {
          lst.remove(i);
          lst.add(Index.of(i.getName(), newValue, i.regions()));
          count++;
        }
        return count;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean updateOne(String name, Predicate<T> prd, T newValue) {
      lock.lock();
      try {
        Optional<Index<T>> opt = findOne(name, prd);
        if(opt.isPresent()) {
          Index<T> i = opt.get();
          List<Index<T>> lst = store.get(name);
          lst.remove(i);
          lst.add(Index.of(i.getName(), newValue, i.regions()));
        }
        return opt.isPresent();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> removeByIndex(String name, Predicate<Index<T>> prd) {
      if(name == null 
          || name.isEmpty() 
          || !store.containsKey(name) 
          || prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        List<Index<T>> lst = store.get(name);
        List<Index<T>> res = lst.stream()
            .filter(prd)
            .collect(Collectors.toList());
        lst.removeAll(res);
        return res;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean updateByIndex(String name, Predicate<Index<T>> prd, Index<T> newIndex) {
      if(name == null 
          || name.isEmpty() 
          || !store.containsKey(name) 
          || prd == null) {
        return false;
      }
      lock.lock();
      try {
        List<Index<T>> lst = store.get(name);
        Optional<Index<T>> opt = lst.stream().filter(prd).findFirst();
        if(opt.isPresent()) {
          lst.remove(opt.get());
          lst.add(newIndex);
        }
        return opt.isPresent();
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
