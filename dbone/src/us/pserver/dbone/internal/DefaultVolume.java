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
import java.util.function.IntFunction;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;
import us.pserver.dbone.OUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/10/2017
 */
public class DefaultVolume implements Volume {
  
  private final SerializationService serial;
  
  private final Storage storage;
  
  private final IntFunction<ByteBuffer> alloc;
  
  
  public DefaultVolume(Storage storage, SerializationService serial, IntFunction<ByteBuffer> alloc) {
    this.storage = NotNull.of(storage).getOrFail("Bad null Storage");
    this.serial = NotNull.of(serial).getOrFail("Bad null SerializationService");
    this.alloc = NotNull.of(alloc).getOrFail("Bad null ByteBuffer alloc function");
  }
  
  
  @Override
  public Region put(StoreUnit unit) throws StorageException {
    NotNull.of(unit).failIfNull("Bad null StoreUnit");
    byte[] data = serial.serialize(unit.getObject());
    int total = data.length
        + Integer.BYTES * 2 
        + unit.getOUID().getClassName().length()
        + unit.getOUID().getHash().length();
    ByteBuffer buf = alloc.apply(total);
    put(buf, unit.getOUID());
    buf.put(data);
    return storage.put(buf);
  }
  
  
  private void put(ByteBuffer buf, OUID uid) throws StorageException {
    byte[] bcls = UTF8String.from(uid.getClassName()).getBytes();
    byte[] buid = UTF8String.from(uid.getHash()).getBytes();
    buf.putInt(bcls.length);
    buf.putInt(buid.length);
    buf.put(bcls);
    buf.put(buid);
  }
  
  
  @Override
  public StoreUnit get(Region reg) throws StorageException {
    ByteBuffer buf = storage.get(reg);
    OUID uid = getOUID(buf);
    byte[] data = new byte[buf.remaining()];
    return StoreUnit.of(uid, serial.deserialize(data));
  }
  
  
  @Override
  public OUID getOUID(Region reg) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    return getOUID(storage.get(reg));
  }
  
  
  private OUID getOUID(ByteBuffer buf) throws StorageException {
    byte[] bcls = new byte[buf.getInt()];
    byte[] buid = new byte[buf.getInt()];
    buf.get(bcls);
    buf.get(buid);
    return OUID.of(
        UTF8String.from(buid).toString(), 
        UTF8String.from(bcls).toString()
    );
  }
  
  
  @Override
  public void close() throws StorageException {
    storage.close();
  }
  
}
