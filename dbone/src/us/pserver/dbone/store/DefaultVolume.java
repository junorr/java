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
import us.pserver.dbone.store.tx.ComposeableTransaction;
import us.pserver.dbone.store.tx.DefaultTransaction;
import us.pserver.dbone.store.tx.Transaction;
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
  public Transaction<Index> put(StoreUnit unit) {
    NotNull.of(unit).failIfNull("Bad null StoreUnit");
    ByteBuffer sbuf = serial.serialize(unit.getValue());
    Transaction<Block> tx = storage.allocate();
    //System.out.println("* Volume.put: success="+ tx.isSuccessful()+ ", error="+ tx.getError()+ ", value="+ tx.value());
    if(!tx.isSuccessful()) {
      return new ComposeableTransaction(tx.getError().get(), null, tx);
    }
    Block blk = tx.value().get();
    Index idx = Index.of(
        unit.getUID().getHash(), 
        blk.region(), 
        unit.getUID()
    );
    this.put(unit.getUID(), blk);
    Transaction<Block> tx2 = this.put(sbuf, blk);
    if(!tx2.isSuccessful()) {
      return new ComposeableTransaction(tx2.getError().get(), idx, tx2);
    }
    else {
      return new ComposeableTransaction(null, idx, tx);
    }
  }
  
  
  private Transaction<Block> put(ByteBuffer sbuf, Block blk) {
    copy(sbuf, blk.buffer());
    while(sbuf.hasRemaining()) {
      Transaction<Block> tx = storage.allocate();
      if(!tx.isSuccessful()) return tx;
      Block b2 = tx.value().get();
      blk.setNext(b2.region());
      blk.buffer().flip();
      storage.put(blk);
      copy(sbuf, b2.buffer());
      blk = b2;
    }
    this.zeroFill(blk.buffer());
    storage.put(blk);
    return new DefaultTransaction(null, null);
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
      from.position(from.position() + minLen);
    }
    else {
      byte[] bs = new byte[minLen];
      from.get(bs);
      to.put(bs);
    }
  }


  @Override
  public Transaction<Index> put(ObjectUID uid, MappedValue val) {
    return this.put(StoreUnit.of(uid, val));
  }
  
  
  @Override
  public Transaction<ObjectUID> getUID(Index idx) {
    try {
      return new DefaultTransaction(null, this.getUID(storage.get(idx.getRegion())));
    } catch(Exception e) {
      return new DefaultTransaction(e, null);
    }
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
  public Transaction<StoreUnit> get(Index idx) {
    try {
      Block blk = storage.get(idx.getRegion());
      ObjectUID uid = this.getUID(blk);
      return new DefaultTransaction(null, StoreUnit.of(uid, this.getValue(blk)));
    } catch(Exception e) {
      return new DefaultTransaction(e, null);
    }
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
