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

package us.pserver.tools.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.LinkedList;
import java.util.Queue;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/11/2017
 */
public class BufferQueueChannel {

  private final Queue<ByteBuffer> queue;
  
  private final SeekableByteChannel channel;
  
  
  public BufferQueueChannel(SeekableByteChannel channel) {
    this(channel, new LinkedList<>());
  }
  
  
  public BufferQueueChannel(SeekableByteChannel channel, Queue<ByteBuffer> queue) {
    this.channel = NotNull.of(channel).getOrFail("Bad null SeekableByteChannel");
    this.queue = NotNull.of(queue).getOrFail("Bad null Queue");
  }
  
  
  public BufferQueueChannel add(ByteBuffer buf) {
    if(buf != null && buf.hasRemaining()) {
      queue.add(buf);
    }
    return this;
  }
  
  
  public int size() {
    return queue.size();
  }
  
  
  public BufferQueueChannel clear() {
    queue.clear();
    return this;
  }
  
  
  public long write() throws IOException {
    return 0;
  }
  
}
