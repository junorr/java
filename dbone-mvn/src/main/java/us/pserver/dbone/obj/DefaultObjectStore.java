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
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.store.Storage;
import us.pserver.tools.Match;
import us.pserver.dbone.serial.SerializationService;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class DefaultObjectStore implements ObjectStore {
  
  private final Storage store;
  
  private final SerializationService sconf;
  
  
  public DefaultObjectStore(Storage stg, SerializationService cfg) {
    this.store = Match.notNull(stg).getOrFail("Bad null Storage");
    this.sconf = Objects.requireNonNull(cfg, "Bad null SerializationService");
  }
  
  
  @Override
  public Record put(Object obj) throws IOException {
    Record rec = Record.of(obj, sconf);
    return rec.withRegion(store.put(rec.toByteBuffer(store.allocBufferPolicy())));
  }


  @Override
  public <T> T get(Region reg) throws ClassNotFoundException, IOException {
    return (T) Record.of(store.get(reg), sconf).withRegion(reg).getValue();
  }
  
  
  @Override
  public <T> Record<T> getRecord(Region reg) throws ClassNotFoundException, IOException {
    return (Record<T>) Record.of(reg, store.get(reg), sconf);
  }
  
  
  @Override
  public void putReserved(Object obj) throws IOException {
    store.putReservedData(Record.of(obj, sconf).toByteBuffer(store.allocBufferPolicy()));
  }
  
  
  @Override
  public <T> T getReserved() throws ClassNotFoundException, IOException {
    return (T) Record.of(store.getReservedData(), sconf).getValue();
  }
  
  
  @Override
  public <T> Record<T> remove(Region reg) throws ClassNotFoundException, IOException {
    return (Record<T>) Record.of(reg, store.remove(reg), sconf);
  }
  
  
  @Override
  public <T> Stream<Record<T>> streamOf(Class<T> cls) throws ClassNotFoundException, IOException {
    return StreamSupport.stream(new LazyStoreSpliterator<>(cls, store, sconf), false);
  }
  
  
  @Override
  public Stream<Record> streamAll() throws ClassNotFoundException, IOException {
    return StreamSupport.stream(new LazyStoreSpliterator(store, sconf), false);
  }
  
  
  @Override
  public void close() throws IOException {
    store.close();
  }
  
}
