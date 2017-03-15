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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;
import us.pserver.jose.query.Query;
import us.pserver.jose.query.QueryLimit;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2016
 */
public interface IndexQuery extends QueryLimit {

  public List<Index> find(String name, Predicate prd);
  
  public List<Index> find(Predicate<Index> prd);
  
  public List<Index> find(Query query);
  
  @Override public IndexQuery skip(int n);
  
  @Override public IndexQuery limit(int len);
  
  
  static <U> IndexQuery of(Map<String, List<Index>> store, ReentrantLock lock) {
    return new IndexQueryImpl(store, lock);
  }
  
  
  
  
  
  public static class IndexQueryImpl implements IndexQuery {
    
    private final Map<String, List<Index>> store;
    
    private final ReentrantLock lock;
    
    private final int skip;
    
    private final int limit;
    
    
    public IndexQueryImpl(Map<String, List<Index>> store, ReentrantLock lock) {
      this(store, lock, 0, Integer.MAX_VALUE);
    }
    

    public IndexQueryImpl(Map<String, List<Index>> store, ReentrantLock lock, int skip, int limit) {
      this.store = Sane.of(store)
          .with("Bad Null Map")
          .get(Checkup.isNotNull());
      this.lock = Sane.of(lock)
          .with("Bad Null ReentrantLock")
          .get(Checkup.isNotNull());
      this.skip = skip;
      this.limit = limit;
    }
    

    @Override
    public List<Index> find(String name, Predicate prd) {
      if(name == null || name.isEmpty() 
          || !store.containsKey(name)
          || prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        return store.get(name).stream()
            .filter(i->prd.test(i.getValue()))
            .skip(skip)
            .limit(limit)
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public List<Index> find(Predicate<Index> prd) {
      if(prd == null) {
        return Collections.EMPTY_LIST;
      }
      lock.lock();
      try {
        return store.values().stream()
            .flatMap(l->l.stream())
            .filter(prd)
            .skip(skip)
            .limit(limit)
            .collect(Collectors.toList());
      }
      finally {
        lock.unlock();
      }
    }
    

    @Override
    public List<Index> find(Query query) {
      if(query == null || query.name() == null) {
        return Collections.EMPTY_LIST;
      }
      List<Index> idx = store.get(query.name());
      if(idx == null || idx.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      List<Index> res = idx.stream().filter(
          i->query.operation().apply(i.getValue())
      ).collect(Collectors.toList());
      return res;
    }
  
  
    @Override
    public IndexQuery skip(int skip) {
      return new IndexQueryImpl(store, lock, skip, limit);
    }


    @Override
    public IndexQuery limit(int limit) {
      return new IndexQueryImpl(store, lock, skip, limit);
    }
    
  }
  
}
