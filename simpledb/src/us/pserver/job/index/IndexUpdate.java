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

package us.pserver.job.index;

import us.pserver.job.query.QueryLimit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2016
 */
public interface IndexUpdate<T> extends QueryLimit {

  public int update(String name, Predicate<T> prd, T newValue);
  
  public int update(Predicate<Index<T>> prd, Index<T> newIndex);
  
  public List<Index<T>> remove(String name, Predicate<T> prd);
  
  public List<Index<T>> remove(Predicate<Index<T>> prd);
  
  @Override public IndexUpdate<T> skip(int n);
  
  @Override public IndexUpdate<T> limit(int len);
  
  
  static <U> IndexUpdate<U> of(Map<String, List<Index<U>>> store, ReentrantLock lock) {
    return new DefIndexUpdate(store, lock);
  }
  
  
  
  
  
  public static class DefIndexUpdate<T> implements IndexUpdate<T> {
    
    private final Map<String, List<Index<T>>> store;
    
    private final ReentrantLock lock;
    
    private final IndexQuery<T> query;
    
    private final int skip;
    
    private final int limit;
    
    
    public DefIndexUpdate(Map<String, List<Index<T>>> store, ReentrantLock lock, int skip, int limit) {
      this.store = Sane.of(store)
          .with("Bad Null Map")
          .get(Checkup.isNotNull());
      this.lock = Sane.of(lock)
          .with("Bad Null ReentrantLock")
          .get(Checkup.isNotNull());
      query = IndexQuery.of(store, lock);
      this.skip = skip;
      this.limit = limit;
    }
    
    
    public DefIndexUpdate(Map<String, List<Index<T>>> store, ReentrantLock lock) {
      this(store, lock, 0, Integer.MAX_VALUE);
    }


    @Override
    public int update(String name, Predicate<T> prd, T newValue) {
      if(name == null || name.isEmpty() 
          || prd == null 
          || newValue == null 
          || !store.containsKey(name)) {
        return -1;
      }
      lock.lock();
      try {
        List<Index<T>> ls = query.skip(skip).limit(limit).find(name, prd);
        ls.forEach(i->{
          store.get(name).remove(i);
          store.get(name).add(
              Index.of(name, newValue, i.regions())
          );
        });
        return ls.size();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public int update(Predicate<Index<T>> prd, Index<T> newIndex) {
      if(prd == null || newIndex == null) {
        return -1;
      }
      lock.lock();
      try {
        List<Index<T>> ls = query.skip(skip).limit(limit).find(prd);
        ls.forEach(i->{
          store.get(i.getName()).remove(i);
          if(!store.containsKey(newIndex.getName())) {
            store.put(newIndex.getName(), new ArrayList<>());
          }
          store.get(newIndex.getName()).add(newIndex);
        });
        return ls.size();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> remove(String name, Predicate<T> prd) {
      if(name == null || name.isEmpty() || prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        List<Index<T>> res = query.skip(skip).limit(limit).find(name, prd);
        if(!res.isEmpty()) {
          store.get(name).removeAll(res);
        }
        return res;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index<T>> remove(Predicate<Index<T>> prd) {
      if(prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        List<Index<T>> res = query.skip(skip).limit(limit).find(prd);
        res.forEach(i->store.get(i.getName()).remove(i));
        return res;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public IndexUpdate<T> skip(int skip) {
      return new DefIndexUpdate(store, lock, skip, limit);
    }


    @Override
    public IndexUpdate<T> limit(int limit) {
      return new DefIndexUpdate(store, lock, skip, limit);
    }
    
  }
  
}
