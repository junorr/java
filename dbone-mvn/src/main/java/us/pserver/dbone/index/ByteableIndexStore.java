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

package us.pserver.dbone.index;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/12/2017
 */
public class ByteableIndexStore implements IndexStore {
  
  private final IndexStore store;
  
  public ByteableIndexStore(IndexStore store) {
    this.store = NotNull.of(store).getOrFail("Bad null IndexStore");
  }
  
  @Override
  public ByteableIndexStore putIndex(Class cls, Index idx) {
    store.putIndex(cls, idx);
    return this;
  }
  
  @Override
  public ByteableIndexStore putIndex(Object obj, Record rec) {
    store.putIndex(obj, rec);
    return this;
  }
  
  @Override
  public <V extends Comparable<V>> Stream<Index<V>> getIndex(Class cls, String name, V value) {
    return store.getIndex(cls, name, value);
  }
  
  @Override
  public Stream<Index> getIndex(Class cls, Region reg) {
    return store.getIndex(cls, reg);
  }
  
  @Override
  public Stream<Index> getIndex(Class cls, String name) {
    return store.getIndex(cls, name);
  }
  
  @Override
  public <V extends Comparable<V>> List<Index<V>> removeIndex(Class cls, String name, V value) {
    return store.removeIndex(cls, name, value);
  }
  
  @Override
  public List<Index> removeIndex(Class cls, String name) {
    return store.removeIndex(cls, name);
  }
  
  @Override
  public List<Index> removeIndex(Class cls, Region reg) {
    return store.removeIndex(cls, reg);
  }
  
  @Override
  public Stream<Index<String>> getUIDIndexes(Class cls) {
    return store.getUIDIndexes(cls);
  }
  
  @Override
  public <T, R extends Comparable<R>> ByteableIndexStore appendIndexBuilder(Class cls, String name, Function<T, R> acessor) {
    store.appendIndexBuilder(cls, name, acessor);
    return this;
  }
  
  @Override
  public ByteableIndexStore appendAnnotatedIndexBuilder(Class cls) {
    store.appendAnnotatedIndexBuilder(cls);
    return this;
  }
  
  @Override
  public ByteableIndexStore appendAnnotatedIndexBuilder(Class cls, MethodHandles.Lookup lookup) {
    store.appendAnnotatedIndexBuilder(cls, lookup);
    return this;
  }
  
  @Override
  public ByteableIndexStore appendReflectiveIndexBuilder(Class cls, String name) {
    store.appendReflectiveIndexBuilder(cls, name);
    return this;
  }
  
  @Override
  public ByteableIndexStore appendReflectiveIndexBuilder(Class cls, String name, MethodHandles.Lookup lookup) {
    store.appendReflectiveIndexBuilder(cls, name, lookup);
    return this;
  }
  
  @Override
  public IndexBuilder removeIndexBuilder(Class cls, String name) {
    return store.removeIndexBuilder(cls, name);
  }
  
  @Override
  public boolean containsIndex(Class cls, Index idx) {
    return store.containsIndex(cls, idx);
  }
  
  @Override
  public boolean containsIndexBuilder(Class cls, String name) {
    return store.containsIndexBuilder(cls, name);
  }
  
  public ByteableIndexStore writeTo
  
}
