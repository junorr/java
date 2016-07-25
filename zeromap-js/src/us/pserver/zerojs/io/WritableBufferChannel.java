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
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/04/2016
 */
public class WritableBufferChannel implements WritableByteChannel {
  
  private final ArrayList<ByteBuffer> list;
  
  private int index;
  
  
  public WritableBufferChannel() {
    list = new ArrayList<>();
    index = 0;
  }
  

  @Override
  public int write(ByteBuffer src) throws IOException {
    if(src == null || src.remaining() < 1) {
      throw new IOException("Invalid empty ByteBuffer");
    }
    ByteBuffer buf = ByteBuffer.allocate(src.remaining());
    buf.put(src).flip();
    list.add(buf);
    index++;
    return buf.remaining();
  }


  @Override
  public boolean isOpen() {
    return true;
  }


  @Override
  public void close() throws IOException {
    list.clear();
  }
  
  
  public long length() {
    AtomicLong len = new AtomicLong(0);
    list.forEach(b->len.addAndGet(b.remaining()));
    return len.get();
  }
  
  
  public ByteBuffer getBuffer() {
    if(list.isEmpty()) {
      return ByteBuffer.allocate(0);
    }
    ByteBuffer buf = ByteBuffer.allocateDirect((int) length());
    list.forEach(b->{
      buf.put(b);
      b.rewind();
    });
    buf.flip();
    return buf;
  }
  
  
  public String toString(Charset cst) {
    if(cst == null) {
      throw new IllegalArgumentException(
          "Invalid null Charset"
      );
    }
    return cst.decode(getBuffer()).toString();
  }
  
  
  @Override
  public String toString() {
    return toString(Charset.forName("UTF-8"));
  }

}
