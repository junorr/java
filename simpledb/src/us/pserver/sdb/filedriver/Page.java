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

package us.pserver.sdb.filedriver;

import java.nio.ByteBuffer;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public interface Page {
  
  public static final int DEFAULT_BLOCK_SIZE = 1024;
  

  public ByteBuffer buffer();
  
  public int blockSize();
  
  
  public static Page of(ByteBuffer buf) {
    return new BasicPage(buf);
  }
  
  
  public static Page of(ByteBuffer buf, int blockSize) {
    return new BasicPage(buf, blockSize);
  }
  
  
  
  
  public static class BasicPage implements Page {
    
    private final ByteBuffer buffer;
    
    private final int size;
    
    
    public BasicPage(ByteBuffer buf) {
      this(buf, DEFAULT_BLOCK_SIZE);
    }
    
    
    public BasicPage(ByteBuffer buf, int blockSize) {
      Sane.of(buf)
          .check(Checkup.isNotNull())
          .and(b->b.capacity() > 0)
          .with("Bad Null/Empty Buffer")
          .check();
      this.size = Sane.of(blockSize)
          .with("Bad Block Size")
          .get(Checkup.isGreaterThan(0))
          .intValue();
      if(buf.capacity() % blockSize != 0) {
        if(buf.isDirect()) {
          buffer = ByteBuffer.allocateDirect((buf.capacity() % blockSize + 1) * blockSize);
        } else {
          buffer = ByteBuffer.allocate((buf.capacity() % blockSize + 1) * blockSize);
        }
        buffer.put(buf).flip();
      } 
      else {
        buffer = buf;
      }
    }
    

    @Override
    public ByteBuffer buffer() {
      return buffer;
    }


    @Override
    public int blockSize() {
      return size;
    }
    
  }
  
}
