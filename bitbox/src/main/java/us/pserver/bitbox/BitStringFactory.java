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

package us.pserver.bitbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import us.pserver.tools.io.DynamicByteBuffer;
import us.pserver.tools.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitStringFactory extends AbstractBitBoxFactory<BitString,String> {
  
  
  public BitStringFactory(BitBoxConfiguration conf) {
    super(conf);
  }
  
  
  @Override
  public BitString createFrom(String str) {
    return new UTF8BitString(str.length(), StandardCharsets.UTF_8.encode(str));
  }
  
  
  public BitString createFrom(byte[] bs) {
    return createFrom(bs, 0, bs.length);
  }
  
  
  public BitString createFrom(byte[] bs, int off, int len) {
    ByteBuffer content = ByteBuffer.wrap(bs, off, len);
    return new UTF8BitString(content.remaining(), content);
  }
  
  
  @Override
  public BitString createFrom(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    int id = buf.getInt();
    if(id != BitString.ID) {
      throw new IllegalArgumentException("Not a BitString content");
    }
    int len = buf.getInt();
    buf.position(pos).limit(pos + len);
    UTF8BitString str = new UTF8BitString(buf.slice());
    buf.limit(lim);
    return str;
  }


  @Override
  public BitString createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(BitBox.HEADER_BYTES);
    ch.read(buf);
    buf.flip();
    int id = buf.getInt();
    int len = buf.getInt();
    buf = ByteBuffer.allocate(len - BitBox.HEADER_BYTES);
    buf.putInt(id);
    buf.putInt(len);
    ch.read(buf);
    buf.flip();
    return new UTF8BitString(buf);
  }


  @Override
  public BitString createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
