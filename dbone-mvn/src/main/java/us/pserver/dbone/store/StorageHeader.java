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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public interface StorageHeader extends Writable {
  
  public static final int BYTES = Region.BYTES + Integer.BYTES;
  
  public static final int MIN_BLOCK_SIZE = Block.META_BYTES + Region.BYTES;
  

  public Region getFreeRegion();
  
  public int getBlockSize();
  
  
  public static StorageHeader of(int blockSize, Region reg) {
    return new Default(blockSize, reg);
  }
  
  
  public static StorageHeader read(ByteBuffer buf) {
    if(buf == null || buf.remaining() < BYTES) {
      throw new IllegalArgumentException("Bad ByteBuffer = "+ (buf == null ? buf : buf.remaining()));
    }
    return new Default(buf.getInt(), Region.of(buf));
  }
  
  
  
  
  
  
  static class Default implements StorageHeader {
    
    private final Region free;
    
    private final int blksize;
    
    public Default(int blockSize, Region reg) {
      this.free = Objects.requireNonNull(reg, "Bad null Region");
      this.blksize = Match.of(blockSize, n->n >= MIN_BLOCK_SIZE)
          .getOrFail("Bad block size "+ blockSize+ " (< " + MIN_BLOCK_SIZE + ")");
    }

    @Override
    public Region getFreeRegion() {
      return free;
    }


    @Override
    public int getBlockSize() {
      return blksize;
    }


    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      return ch.write(toByteBuffer());
    }


    @Override
    public int writeTo(ByteBuffer wb) {
      Objects.requireNonNull(wb, "Bad null ByteBuffer");
      if(wb.remaining() < BYTES) {
        throw new IllegalArgumentException(
            "writeTo(>> ByteBuffer wb <<): Bad remaining size "
                + wb.remaining()+ " < "+ BYTES
        );
      }
      wb.putInt(blksize);
      free.writeTo(wb);
      return BYTES;
    }


    @Override
    public ByteBuffer toByteBuffer() {
      ByteBuffer buf = ByteBuffer.allocate(BYTES);
      this.writeTo(buf);
      buf.flip();
      return buf;
    }
    
  }
  
}
