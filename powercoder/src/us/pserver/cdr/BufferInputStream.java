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

package us.pserver.cdr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.nullbuffer;
import static us.pserver.cdr.Checker.range;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/06/2014
 */
public class BufferInputStream extends InputStream {
  
  private DynamicBuffer buffer;
  
  
  public BufferInputStream() {
    buffer = new DynamicBuffer();
  }
  
  
  public long size() {
    return buffer.size();
  }
  
  
  public DynamicBuffer buffer() {
    return buffer;
  }
  
  
  public BufferInputStream put(byte b) {
    buffer.write(b);
    return this;
  }
  
  
  public BufferInputStream put(byte[] bs) {
    return this.put(bs, 0, bs.length);
  }
  
  
  public BufferInputStream put(byte[] bs, int off, int len) {
    nullarray(bs);
    range(len, 1, bs.length - off);
    range(off, 0, bs.length - len);
    buffer.write(bs, off, len);
    return this;
  }
  
  
  public BufferInputStream put(ByteBuffer buf) {
    nullbuffer(buf);
    while(buf.remaining() > 0) {
      this.put(buf.get());
    }
    return this;
  }


  @Override
  public int read() throws IOException {
    if(buffer.isEmpty() || !buffer.hasNext()) 
      return -1;
    return this.read();
  }

}
