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

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public class DirectMemoryStorage extends AbstractStorage {
  
  private final ByteBuffer buffer;
  
  
  protected DirectMemoryStorage(int size, LinkedList<Region> freeBlocks, int blockSize) {
    super(freeBlocks, ALLOC_POLICY_DIRECT, blockSize);
    if(size <= blockSize*2) {
      throw new IllegalArgumentException("To small memory size: "+ size);
    }
    this.buffer = ALLOC_POLICY_DIRECT.apply(size);
  }
  
  
  @Override
  public long size() {
    return buffer.capacity();
  }
  
  
  @Override
  protected Region nextRegion() {
    Region reg = super.nextRegion();
    if(reg.length() + reg.offset() > size()) {
      throw new BufferOverflowException();
    }
    return reg;
  }
  
  
  @Override
  public Block allocate() {
    Region reg = frees.isEmpty() ? nextRegion() : frees.pop();
    return get(reg);
  }


  @Override
  public Block get(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
    buffer.limit(reg.intLength() + reg.intOffset());
    buffer.position(reg.intOffset());
    ByteBuffer buf = buffer.slice();
    return new DefaultBlock(reg, buffer.slice());
  }
  
  
  public boolean isSharedBuffer(ByteBuffer shared) {
    Object att = Reflector.of(shared).selectField("att").get();
    return att != null && att.equals(buffer);
  }


  @Override
  public void put(Block blk) throws StorageException {
    NotNull.of(blk).failIfNull("Bad null Block");
    if(isSharedBuffer(blk.buffer())) {
      return;
    }
    buffer.limit(blk.region().intLength() + blk.region().intOffset());
    buffer.position(blk.region().intOffset());
    Block.copy(blk.buffer(), buffer);
  }


  @Override
  public void close() {
    buffer.clear();
  }
  
  
  @Override
  public StorageTransaction startTransaction() {
    return new StorageTransaction(this);
  }
  
}
