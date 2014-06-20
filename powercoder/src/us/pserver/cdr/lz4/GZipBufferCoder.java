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

package us.pserver.cdr.lz4;

import us.pserver.cdr.gzip.*;
import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import us.pserver.cdr.Coder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2014
 */
public class GZipBufferCoder implements Coder<ByteBuffer> {
  
  private final GZipByteCoder cdr;
  
  private final ByteBufferConverter conv;
  
  
  public GZipBufferCoder() {
    cdr = new GZipByteCoder();
    conv = new ByteBufferConverter();
  }
  
  
  private void checkBuffer(ByteBuffer buf) {
    if(buf == null || buf.remaining() == 0)
      throw new IllegalArgumentException(
          "Invalid ByteBuffer [buf="
          + (buf == null ? buf : buf.remaining()));
  }


  @Override
  public ByteBuffer apply(ByteBuffer buf, boolean encode) {
    return (encode ? encode(buf) : decode(buf));
  }


  @Override
  public ByteBuffer encode(ByteBuffer buf) {
    checkBuffer(buf);
    byte[] bs = conv.convert(buf);
    return conv.reverse(cdr.encode(bs));
  }


  @Override
  public ByteBuffer decode(ByteBuffer buf) {
    checkBuffer(buf);
    byte[] bs = conv.convert(buf);
    return conv.reverse(cdr.decode(bs));
  }

}
