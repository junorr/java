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

package us.pserver.cdr.lzma;

import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.cdr.Checker.nullbuffer;
import us.pserver.cdr.Coder;

/**
 * Codificador/Decodificador LZMA para <code>ByteBuffer</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 18/06/2014
 */
public class LzmaBufferCoder implements Coder<ByteBuffer> {
  
  private final LzmaByteCoder cdr;
  
  private final ByteBufferConverter conv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public LzmaBufferCoder() {
    cdr = new LzmaByteCoder();
    conv = new ByteBufferConverter();
  }
  
  
  @Override
  public ByteBuffer apply(ByteBuffer buf, boolean encode) {
    return (encode ? encode(buf) : decode(buf));
  }


  @Override
  public ByteBuffer encode(ByteBuffer buf) {
    nullbuffer(buf);
    byte[] bs = conv.convert(buf);
    return conv.reverse(cdr.encode(bs));
  }


  @Override
  public ByteBuffer decode(ByteBuffer buf) {
    nullbuffer(buf);
    byte[] bs = conv.convert(buf);
    return conv.reverse(cdr.decode(bs));
  }

}
