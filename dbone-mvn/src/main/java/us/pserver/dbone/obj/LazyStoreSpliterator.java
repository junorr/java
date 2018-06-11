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

package us.pserver.dbone.obj;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.store.Storage;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class LazyStoreSpliterator<T> implements Spliterator<Record<T>> {
  
  private final List<Region> roots;
  
  private Class cls;
  
  private final Storage store;
  
  private final AtomicInteger idx;
  
  
  public LazyStoreSpliterator(Class cls, Storage stg, List<Region> roots) {
    this.roots = Objects.requireNonNull(roots, "Bloody Roots");
    this.store = Objects.requireNonNull(stg, "Bad null Storage");
    this.cls = cls;
    idx = new AtomicInteger(0);
  }

  
  public LazyStoreSpliterator(Storage stg, List<Region> roots) {
    this(null, stg, roots);
  }

  
  public LazyStoreSpliterator(Class cls, Storage stg) throws IOException {
    this(cls, Objects.requireNonNull(stg, "Bad null Storage"), stg.getRootRegions());
  }

  
  public LazyStoreSpliterator(Storage stg) throws IOException {
    this(Objects.requireNonNull(stg, "Bad null Storage"), stg.getRootRegions());
  }

  
  @Override
  public boolean tryAdvance(Consumer<? super Record<T>> action) {
    try {
      if(idx.get() < roots.size()) {
        Region reg = roots.get(idx.getAndIncrement());
        Record<T> rec = Record.of(store.get(reg));
        //Log.on("class = %s", rec.getValueClass());
        if(cls == null) {
          action.accept(rec.withRegion(reg));
        }
        else if(cls.isAssignableFrom(rec.getValueClass())) {
          action.accept(rec.withRegion(reg));
        }
        return true;
      }
      return false;
    } 
    catch(ClassNotFoundException e) {
      return tryAdvance(action);
    } 
    catch(IOException e) {
      throw new IllegalStateException(e.toString(), e);
    }
  }


  @Override
  public Spliterator<Record<T>> trySplit() {
    int size = roots.size();
    if(size < 2) return Collections.EMPTY_LIST.spliterator();
    return new LazyStoreSpliterator<>(cls, store, roots.subList(size / 2, size));
  }


  @Override
  public long estimateSize() {
    return roots.size();
  }


  @Override
  public int characteristics() {
    return NONNULL & DISTINCT;
  }

}
