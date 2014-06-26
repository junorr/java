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

package us.pserver.cdr.hex;

import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.cdr.Checker.nullbuffer;
import us.pserver.cdr.Coder;

/**
 * Codificador/Decodificador GZIP para <code>ByteBuffer</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/08/2013
 */
public class HexBufferCoder implements Coder<ByteBuffer> {

  private HexByteCoder hcd;
  
  private ByteBufferConverter bcv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HexBufferCoder() {
    hcd = new HexByteCoder();
    bcv = new ByteBufferConverter();
  }
  
  
  @Override
  public ByteBuffer apply(ByteBuffer buffer, boolean encode) {
    nullbuffer(buffer);
    byte[] bs = bcv.convert(buffer);
    if(bs == null || bs.length == 0)
      return null;
    
    return bcv.reverse(
        hcd.apply(bs, encode));
  }
  
  
  @Override
  public ByteBuffer encode(ByteBuffer buffer) {
    return this.apply(buffer, true);
  }
  
  
  @Override
  public ByteBuffer decode(ByteBuffer buffer) {
    return this.apply(buffer, false);
  }
  
}
