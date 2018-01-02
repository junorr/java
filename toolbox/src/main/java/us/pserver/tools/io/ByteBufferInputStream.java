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

import java.io.InputStream;
import java.nio.ByteBuffer;
import us.pserver.tools.NotMatch;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2017
 */
public class ByteBufferInputStream extends InputStream {

  private final ByteBuffer buffer;
  
  
  public ByteBufferInputStream(ByteBuffer buf) {
    this.buffer = NotMatch.notNull(buf).getOrFail("Bad null buffer");
    if(buffer.remaining() < 1) {
      throw new IllegalArgumentException("No remaining bytes to read");
    }
  }
  
  
  public int size() {
    return this.buffer.remaining();
  }


  @Override
  public int read() {
    return this.size() < 1 ? -1 : this.buffer.get();
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) {
    int size = Math.min(len, this.size());
    if(size < 1) {
      return -1;
    }
    this.buffer.get(bs, off, size);
    return size;
  }
  
  
  @Override
  public int read(byte[] bs) {
    return this.read(bs, 0, bs.length);
  }
  
}
