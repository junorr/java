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
    byte[] buid = UTF8String.from(unit.getObjectUID()).getBytes();
    byte[] data = serial.serialize(unit.getObject());
    ByteBuffer buf = alloc.apply(
        Short.BYTES + buid.length + data.length
    );
    buf.putShort((short)buid.length);
    buf.put(buid);
    buf.put(data);
    buf.flip();
    return storage.put(buf);
  }
  
  
  @Override
  public StoreUnit get(Region reg) throws StorageException {
    ByteBuffer buf = storage.get(reg);
    String uid = this.getObjectUID(buf);
    byte[] data = new byte[buf.remaining()];
    buf.get(data);
    return StoreUnit.of(uid, serial.deserialize(data));
  }
  
  
  @Override
  public String getObjectUID(Region reg) throws StorageException {
    NotNull.of(reg).failIfNull("Bad null Region");
    return DefaultVolume.this.getObjectUID(storage.get(reg));
  }
  
  
  private String getObjectUID(ByteBuffer buf) throws StorageException {
    byte[] buid = new byte[buf.getShort()];
    buf.get(buid);
    return UTF8String.from(buid).toString();
  }
  
  
  @Override
  public void close() throws StorageException {
    storage.close();
  }
  
}
