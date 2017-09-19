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
import java.util.Optional;
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
public class DefaultVolume implements Volume {
  
  private final Storage storage;
  
  private final SerializationService serial;
  
  
  public DefaultVolume(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
    this.serial = new SerializationService();
  }
  

  @Override
  public Index put(StoreUnit unit) throws StoreException {
    NotNull.of(unit).failIfNull("Bad null StoreUnit");
    ByteBuffer sbuf = serial.serialize(unit.getValue());
    Block blk = storage.allocate();
    Index idx = Index.of(
        unit.getUID().getHash(), 
        blk.region(), 
        unit.getUID()
    );
    this.put(unit.getUID(), blk);
    this.put(sbuf, blk);
    return idx;
  }
  
  
  private void put(ByteBuffer sbuf, Block blk) {
    copy(sbuf, blk.buffer());
    while(sbuf.hasRemaining()) {
      Block b2 = storage.allocate();
      blk.setNext(b2.region());
      blk.buffer().flip();
      storage.put(blk);
      copy(sbuf, b2.buffer());
      blk = b2;
    }
    this.zeroFill(blk.buffer());
    storage.put(blk);
  }
  
  
  private void put(ObjectUID uid, Block blk) {
    byte[] buid = UTF8String.from(uid.getHash()).getBytes();
    byte[] bcls = UTF8String.from(uid.getClassName()).getBytes();
    blk.buffer().putInt(buid.length);
    blk.buffer().putInt(bcls.length);
    blk.buffer().put(buid);
    blk.buffer().put(bcls);
  }
  
  
  private void zeroFill(ByteBuffer buf) {
    if(buf.hasArray()) return;
    byte[] bs = new byte[buf.remaining()];
    buf.put(bs);
  }
  
  
  private void copy(ByteBuffer from, ByteBuffer to) {
    int minLen = Math.min(from.remaining(), to.remaining());
    if(from.hasArray()) {
      to.put(
          from.array(), 
          from.arrayOffset(), 
          minLen
      );
    }
    else {
      byte[] bs = new byte[minLen];
      from.get(bs);
      to.put(bs);
    }
  }


  @Override
  public Index put(ObjectUID uid, MappedValue val) throws StoreException {
    return this.put(StoreUnit.of(uid, val));
  }
  
  
  @Override
  public ObjectUID getUID(Index idx) throws StoreException {
    NotNull.of(idx).failIfNull("Bad null Index");
    return this.getUID(storage.get(idx.getRegion()));
  }
  
  
  private ObjectUID getUID(Block blk) {
    int uidLen = blk.buffer().getInt();
    int clsLen = blk.buffer().getInt();
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
  public StoreUnit get(Index idx) throws StoreException {
    NotNull.of(idx).failIfNull("Bad null Index");
    Block blk = storage.get(idx.getRegion());
    ObjectUID uid = this.getUID(blk);
    return StoreUnit.of(uid, this.getValue(blk));
  }
  
  
  private MappedValue getValue(Block blk) {
    ByteBufferOutputStream bos = new ByteBufferOutputStream(storage.getAllocationPolicy());
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
  public void close() throws StoreException {
    storage.close();
  }

}
