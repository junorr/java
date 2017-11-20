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

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import us.pserver.coreone.Core;
import us.pserver.dbone.store.Block;
import us.pserver.dbone.internal.Region;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;
import us.pserver.tools.io.ByteBufferOutputStream;
import us.pserver.dbone.OUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class AsyncVolume2 implements Volume {
  
  private final Storage storage;
  
  
  public AsyncVolume2(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
  }
  
  
  @Override
  public Record put(StoreUnit unit) throws StorageException {
    Block block = storage.allocate();
    Core.cycle(()->{
      put(unit.objectUID(), block);
      put(unit.value(), block);
      getUID(block);
    }).start();
    return Record.of(block.region(), unit.objectUID());
  }
  

  private void put2(ByteBuffer sbuf, Block blk) throws StorageException {
    Block.copy(sbuf, blk.buffer());
    while(sbuf.hasRemaining()) {
      Block b2 = storage.allocate();
      blk.setNext(b2.region());
      storage.put(blk);
      Block.copy(sbuf, b2.buffer());
      blk = b2;
    }
    this.zeroFill(blk.buffer());
    storage.put(blk);
  }
  
  
  private void put(ByteBuffer sbuf, Block blk) throws StorageException {
    if(!sbuf.hasRemaining()) return;
    Block.copy(sbuf, blk.buffer());
    this.zeroFill(sbuf);
    if(sbuf.hasRemaining()) {
      Block b2 = storage.allocate();
      blk.setNext(b2.region());
      put2(sbuf, b2);
    }
    storage.put(blk);
  }
  
  
  private void put(OUID ouid, Block blk) {
    byte[] buid = UTF8String.from(ouid.getHash()).getBytes();
    byte[] bcls = UTF8String.from(ouid.getClassName()).getBytes();
    //System.out.println("* Volume.putUID: uidLen="+ buid.length+ ", clsLen="+ bcls.length+ ", block="+ blk);
    blk.buffer().position(0);
    blk.buffer().putInt(buid.length);
    blk.buffer().putInt(bcls.length);
    blk.buffer().put(buid);
    blk.buffer().put(bcls);
  }
  
  
  private void zeroFill(ByteBuffer buf) {
    byte[] bs = new byte[buf.remaining()];
    buf.put(bs);
  }
  
  
  @Override
  public OUID getUID(Record idx) throws StorageException {
    Block blk = storage.get(idx.getRegion());
    return this.getUID(blk);
  }
  
  
  private OUID getUID(Block blk) {
    blk.buffer().position(0);
    int uidLen = blk.buffer().getInt();
    int clsLen = blk.buffer().getInt();
    //System.out.println("* Volume.objectUID: uidLen="+ uidLen+ ", clsLen="+ clsLen+ ", block="+ blk);
    //if(uidLen == 0) {
      //blk = storage.get(blk.region());
      //blk.buffer().position(0);
      //uidLen = blk.buffer().getInt();
      //clsLen = blk.buffer().getInt();
      //System.out.println("* Volume.objectUID: uidLen="+ uidLen+ ", clsLen="+ clsLen+ ", block="+ blk);
    //}
    byte[] buid = new byte[uidLen];
    byte[] bcls = new byte[clsLen];
    blk.buffer().get(buid);
    blk.buffer().get(bcls);
    return OUID.of(
        UTF8String.from(buid).toString(), 
        UTF8String.from(bcls).toString()
    );
  }
  
  
  public <T> void get(Record rec, T attachment, BiConsumer<T,StoreUnit> bic) {
    Core.cycle(()->{
      StoreUnit su = get(rec);
      bic.accept(attachment, su);
    }).start();
  }
  
  
  public void get(Record rec, Consumer<StoreUnit> cs) {
    Core.cycle(()->{
      StoreUnit su = get(rec);
      cs.accept(su);
    }).start();
  }
  
  
  @Override
  public StoreUnit get(Record idx) throws StorageException {
    Block blk = storage.get(idx.getRegion());
    return StoreUnit.of(getUID(blk), getValue(blk));
  }
  
  
  private ByteBuffer getValue(Block blk) throws StorageException {
    ByteBufferOutputStream bos = new ByteBufferOutputStream();
    bos.write(blk.buffer());
    Optional<Region> next = blk.next();
    while(next.isPresent()) {
      blk = storage.get(next.get());
      bos.write(blk.buffer());
      next = blk.next();
    }
    return bos.toByteBuffer();
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
