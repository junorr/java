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

package us.pserver.dbone.store;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import us.pserver.fastgear.Gear;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class AsyncVolume2 implements Volume {
  
  private final Storage storage;
  
  private final JavaSerializationService serial;
  
  
  public AsyncVolume2(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
    this.serial = new JavaSerializationService();
  }
  

  @Override
  public Record put(StoreUnit unit) throws StorageException {
    NotNull.of(unit).failIfNull("Bad null StoreUnit");
    ByteBuffer sbuf = serial.serialize(unit.getValue());
    Block blk = storage.allocate();
    Record rec = Record.of(
        blk.region(), 
        unit.getUID()
    );
    Gear.of(()->{
      put(unit.getUID(), blk); 
      put(sbuf, blk);
    }).start();
    return rec;
  }
  
  
  private void put(ByteBuffer sbuf, Block blk) throws StorageException {
    Block.copy(sbuf, blk.buffer());
    while(sbuf.hasRemaining()) {
      Block b2 = storage.allocate();
      blk.setNext(b2.region());
      blk.buffer().flip();
      storage.put(blk);
      Block.copy(sbuf, b2.buffer());
      blk = b2;
    }
    this.zeroFill(blk.buffer());
    storage.put(blk);
  }
  
  
  private synchronized void put(ObjectUID uid, Block blk) {
    blk.buffer().position(0);
    byte[] buid = UTF8String.from(uid.getHash()).getBytes();
    byte[] bcls = UTF8String.from(uid.getClassName()).getBytes();
    blk.buffer().putShort((short)buid.length);
    blk.buffer().putShort((short)bcls.length);
    blk.buffer().put(buid);
    blk.buffer().put(bcls);
  }
  
  
  private void zeroFill(ByteBuffer buf) {
    if(buf.hasArray()) return;
    byte[] bs = new byte[buf.remaining()];
    buf.put(bs);
  }
  
  
  @Override
  public Record put(ObjectUID uid, MappedValue val) {
    return this.put(StoreUnit.of(uid, val));
  }
  
  
  @Override
  public ObjectUID getUID(Record idx) throws StorageException {
    return this.getUID(storage.get(idx.getRegion()));
  }
  
  
  public void getUID(Record rec, Consumer<ObjectUID> cs) throws StorageException {
    Gear.of(()->getUID(storage.get(rec.getRegion())))
        .start()
        .input()
        .onAvailable(cs);
  }
  
  
  private synchronized ObjectUID getUID(Block blk) {
    blk.buffer().position(0);
    int uidLen = blk.buffer().getShort();
    int clsLen = blk.buffer().getShort();
    byte[] buid = new byte[uidLen];
    byte[] bcls = new byte[clsLen];
    blk.buffer().get(buid);
    blk.buffer().get(bcls);
    return ObjectUID.builder()
        .withHash(UTF8String.from(buid).toString())
        .withClassName(UTF8String.from(bcls).toString())
        .build();
  }


  @Override
  public StoreUnit get(Record rec) throws StorageException {
    Block blk = storage.get(rec.getRegion());
    MappedValue val = this.getValue(blk);
    ObjectUID uid = this.getUID(blk);
    return StoreUnit.of(uid, val);
  }
  
  
  public void get(Record rec, Consumer<StoreUnit> cs) throws StorageException {
    Gear.of(()->{
      Block blk = storage.get(rec.getRegion());
      return StoreUnit.of(getUID(blk), getValue(blk));
    }).start().input().onAvailable(cs);
  }
  
  
  private MappedValue getValue(Block blk) throws StorageException {
    ByteBufferOutputStream bos = new ByteBufferOutputStream(storage.getAllocationPolicy());
    blk.buffer().position(0);
    int uidLen = blk.buffer().getShort();
    int clsLen = blk.buffer().getShort();
    blk.buffer().position(Short.BYTES * 2 + uidLen + clsLen);
    bos.write(blk.buffer());
    Optional<Region> next = blk.next();
    while(next.isPresent()) {
      blk = storage.get(next.get());
      bos.write(blk.buffer());
      next = blk.next();
    }
    return (MappedValue) serial.deserialize(bos.toByteBuffer());
  }


  @Override
  public void close() throws StorageException {
    storage.close();
  }
  
  
  @Override
  public VolumeTransaction startTransaction() {
    return new VolumeTransaction(this.storage.startTransaction());
  }

}
