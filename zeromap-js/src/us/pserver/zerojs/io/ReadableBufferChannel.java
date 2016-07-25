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

package us.pserver.zerojs.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/04/2016
 */
public class ReadableBufferChannel implements ReadableByteChannel {
  
  private final ByteBuffer buffer;
  
  
  public ReadableBufferChannel(ByteBuffer buf) {
    if(buf == null || (buf.remaining() < 1 && buf.position() < 1)) {
      throw new IllegalArgumentException(
          "Invalid empty ByteBuffer"
      );
    }
    if(buf.remaining() < 1) {
      buf.flip();
    }
    this.buffer = buf;
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  

  @Override
  public int read(ByteBuffer dst) throws IOException {
    if(dst == null || dst.remaining() < 1) {
      throw new IOException(
          "Invalid empty ByteBuffer"
      );
    }
    int len = Math.min(dst.remaining(), buffer.remaining());
    if(len < 1) {
      len = -1;
    }
    else {
      byte[] bs = new byte[len];
      buffer.get(bs);
      dst.put(bs);
    }
    return len;
  }


  @Override
  public boolean isOpen() {
    return true;
  }


  @Override
  public void close() throws IOException {
    buffer.clear();
  }

}
