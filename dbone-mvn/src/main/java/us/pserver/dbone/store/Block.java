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

import us.pserver.dbone.region.Region;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.function.IntFunction;
import us.pserver.dbone.util.Log;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/05/2018
 */
public interface Block extends Writable {
  
  public static final int MIN_BLOCK_SIZE = Block.META_BYTES + Region.BYTES;
  
  public static final int DEFAULT_BLOCK_SIZE = 1024;
  
  
  public static enum Type implements Writable {
    
    NODE, ROOT;
    
    @Override
    public int writeTo(WritableByteChannel ch, IntFunction<ByteBuffer> alloc) throws IOException {
      ch.write(toByteBuffer(alloc));
      return BYTES;
    }
    
    @Override
    public int writeTo(ByteBuffer wb) {
      wb.putInt(this.ordinal());
      return BYTES;
    }
    
    @Override
    public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> alloc) {
      ByteBuffer wb = alloc.apply(BYTES);
      writeTo(wb);
      wb.flip();
      return wb;
    }
    
    public static final int BYTES = Integer.BYTES;
    
  }
  

  public static final int STARTING_BYTES = Integer.BYTES + Type.BYTES;
  
  public static final int ENDING_BYTES = Region.BYTES;
  
  public static final int META_BYTES = STARTING_BYTES + ENDING_BYTES;
  
  
  public Region region();
  
  public Block withRegion(Region reg);
  
  public Region nextRegion();
  
  public Block withNext(Region next);
  
  public ByteBuffer buffer();
  
  public Block withBuffer(ByteBuffer buf);
  
  public Block asNode();
  
  public Block asRoot();
  
  public int size();
  
  public boolean isRoot();
  
  public boolean isNode();
  
  
  
  public static Block root(Region reg, ByteBuffer buf, Region next) {
    return new DefaultBlock(Type.ROOT, reg, buf, next);
  }
  
  public static Block node(Region reg, ByteBuffer buf, Region next) {
    return new DefaultBlock(Type.NODE, reg, buf, next);
  }
  
  public static Block read(ByteBuffer br) {
    int type = br.getInt();
    //Log.on("int type: %d", type);
    Type t = Type.values()[type];
    int size = br.getInt();
    int lim = br.limit();
    //Log.on("buffer = %s, br.position() = %d, br.limit( %d ), content = %s", br, br.position(), (br.position() + size), BytesToString.of(br).toString(4, '-'));
    br.limit(br.position() + size);
    ByteBuffer buf = br.slice();
    br.limit(lim);
    br.position(lim - Region.BYTES);
    Region reg = Region.of(br);
    return new DefaultBlock(t, Region.invalid(), buf, reg);
  }
  
  
  public static void validateMinBlockSize(int size) {
    if(!isValidBlockSize(size)) {
      throw new IllegalArgumentException(
          String.format("Bad block size: %d (< %d)", size, MIN_BLOCK_SIZE)
      );
    }
  }
  
  
  public static boolean isValidBlockSize(int size) {
    return size >= MIN_BLOCK_SIZE;
  }
  
}
