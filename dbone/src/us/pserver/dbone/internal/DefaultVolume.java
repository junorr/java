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

package us.pserver.dbone.internal;

import java.nio.ByteBuffer;
import us.pserver.dbone.ObjectUID;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/10/2017
 */
public class DefaultVolume implements Volume {
  
  private final SerializationService serial;
  
  private final Storage storage;
  
  
  public DefaultVolume(Storage storage, SerializationService serial) {
    this.storage = NotNull.of(storage).getOrFail("Bad null Storage");
    this.serial = NotNull.of(serial).getOrFail("Bad null SerializationService");
  }
  
  
  @Override
  public Region put(StoreUnit unit) throws StorageException {
    NotNull.of(unit).failIfNull("Bad null StoreUnit");
    byte[] data = serial.serialize(unit.getObject());
    byte[] bcls = UTF8String.from(unit.getUID().getClassName()).getBytes();
    byte[] buid = UTF8String.from(unit.getUID().getUID()).getBytes();
    int total = data.length
        + Integer.BYTES * 2 
        + bcls.length 
        + buid.length;
    ByteBuffer buf = storage.allocateBuffer(total);
    buf.putInt(bcls.length);
    buf.putInt(buid.length);
    buf.put(bcls);
    buf.put(buid);
    buf.put(data);
    return storage.put(buf);
  }
  
  
  @Override
  public StoreUnit get(Region reg) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    ByteBuffer buf = storage.get(reg);
    byte[] bcls = new byte[buf.getInt()];
    byte[] buid = new byte[buf.getInt()];
    buf.get(bcls);
    buf.get(buid);
    byte[] data = new byte[buf.remaining()];
    buf.get(data);
    ObjectUID uid = ObjectUID.of(
        UTF8String.from(buid).toString(), 
        UTF8String.from(bcls).toString()
    );
    return StoreUnit.of(uid, serial.deserialize(data));
  }
  
  
  @Override
  public void close() throws StorageException {
    storage.close();
  }
  
}
