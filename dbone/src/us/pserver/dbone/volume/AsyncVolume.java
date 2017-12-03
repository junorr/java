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

package us.pserver.dbone.volume;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import us.pserver.coreone.Core;
import us.pserver.coreone.Pipe;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;
import us.pserver.dbone.OUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/10/2017
 */
public class AsyncVolume implements Volume {
  
  private final Volume volume;
  
  
  public AsyncVolume(Volume vol) {
    this.volume = NotNull.of(vol).getOrFail("Bad null Volume");
  }
  
  
  public void putAsync(StoreUnit unit, Consumer<Record> cs) {
    Core.cycle(()->volume.put(unit)).start().input().onAvailable(cs);
  }
  
  
  public Pipe<Record> putAsync(StoreUnit unit) {
    return Core.cycle(()->volume.put(unit)).start().input();
  }
  
  
  public <T> void putAsync(StoreUnit unit, T attachment, BiConsumer<T,Record> bic) {
    //System.out.printf("!!! AsyncVolume.putAsync: %s%n", Core.cycle(()->{
    Core.cycle(()->{
      Record r = volume.put(unit);
      bic.accept(attachment, r);
    //}).start().getClass().getName());
    }).start();
  }
  

  @Override
  public Record put(StoreUnit unit) throws StorageException {
    return volume.put(unit);
  }
  
  
  public void getAsync(Record rec, Consumer<StoreUnit> cs) {
    Core.cycle(()->volume.get(rec)).start().input().onAvailable(cs);
  }
  
  
  public Pipe<StoreUnit> getAsync(Record rec) {
    return Core.cycle(()->volume.get(rec)).start().input();
  }


  public <T> void getAsync(Record rec, T attachment, BiConsumer<T,StoreUnit> bic) {
    //System.out.printf("!!! AsyncVolume.getAsync: %s%n", Core.cycle(()->{
    Core.cycle(()->{
      StoreUnit s = volume.get(rec);
      bic.accept(attachment, s);
    //}).start().getClass().getName());
    }).start();
  }
  

  @Override
  public StoreUnit get(Record idx) throws StorageException {
    return volume.get(idx);
  }


  @Override
  public OUID getUID(Record idx) throws StorageException {
    return volume.getUID(idx);
  }


  @Override
  public void close() {
    Core.INSTANCE.waitShutdown();
    volume.close();
  }


  @Override
  public VolumeTransaction startTransaction() {
    return volume.startTransaction();
  }

}
