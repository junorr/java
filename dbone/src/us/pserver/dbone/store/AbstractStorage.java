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

import us.pserver.dbone.internal.Region;
import us.pserver.dbone.internal.RegionAllocPolicy;
import java.nio.ByteBuffer;
import java.util.function.IntFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/09/2017
 */
public abstract class AbstractStorage implements Storage {
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_DIRECT = ByteBuffer::allocateDirect;
  
  public static final IntFunction<ByteBuffer> ALLOC_POLICY_HEAP = ByteBuffer::allocate;
  
  public static final Region HEADER_REGION = Region.of(0, 2048);
  
  
  protected final IntFunction<ByteBuffer> malloc;
  
  protected final RegionAllocPolicy ralloc;
  
  protected final int blockSize;
  
  
  protected AbstractStorage(int blockSize, IntFunction<ByteBuffer> allocPolicy, RegionAllocPolicy ralloc) {
    this.blockSize = blockSize;
    this.malloc = NotNull.of(allocPolicy).getOrFail("Bad null alloc policy");
    this.ralloc = NotNull.of(ralloc).getOrFail("Bad null RegionAllocPolicy");
  }
  
  
  @Override
  public Block allocate() {
    Region reg = ralloc.next();
    ByteBuffer buf = malloc.apply(reg.intLength());
    return new DefaultBlock(reg, buf).setNext(Region.of(0, 0));
  }


  @Override
  public void reallocate(Block blk) throws StorageException {
    if(ralloc.discard(blk.region()) && blk.buffer().hasRemaining()) {
      this.put(blk);
    }
  }


  @Override
  public void deallocate(Block blk) {
    NotNull.of(blk).failIfNull("Bad null Block");
    ralloc.offer(blk.region());
  }
  
  
  @Override
  public int getBlockSize() {
    return this.blockSize;
  }


  @Override
  public StorageTransaction startTransaction() {
    return new StorageTransaction(this);
  }
  
}
