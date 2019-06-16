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
 *
*/

package net.powercoder.cdr.crypt;

import java.nio.ByteBuffer;
import java.util.Objects;
import net.powercoder.cdr.ByteBufferConverter;


/**
 * Codificador/Decodificador de criptografia para <code>ByteBuffer</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptBufferCoder implements CryptCoder<ByteBuffer> {
  
  private CryptByteCoder coder;
  
  private ByteBufferConverter bcv;
  
  
  /**
   * Construtor padrão que receve a chave de criptografia.
   * @param key Chave de criptografia.
   * @see net.powercoder.cdr.crypt.CryptKey
   */
  public CryptBufferCoder(CryptKey key) {
    if(key == null || key.getKeySpec() == null)
      throw new IllegalArgumentException(
          "Invalid CryptKey: "+ key);
    coder = new CryptByteCoder(key);
    bcv = new ByteBufferConverter();
  }
  
  
  @Override
  public CryptKey getKey() {
    return coder.getKey();
  }
  
  
  /**
   * Retorna o codificador criptográfico de bytes.
   * @return <code>CryptByteCoder</code>.
   * @see net.powercoder.cdr.crypt.CryptByteCoder
   */
  public CryptByteCoder getCoder() {
    return coder;
  }
  
  
  @Override
  public ByteBuffer apply(ByteBuffer buf, boolean encode) {
    Objects.requireNonNull(buf);
    byte[] bs = bcv.convert(buf);
    if(bs == null) return buf;
    bs = coder.apply(bs, encode);
    return bcv.reverse(bs);
  }
  
  
  @Override
  public ByteBuffer encode(ByteBuffer buf) {
    return this.apply(buf, true);
  }
  
  
  @Override
  public ByteBuffer decode(ByteBuffer buf) {
    return this.apply(buf, false);
  }
  
}
