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

package us.pserver.cdr;

import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * Conversor de objetos <code>ByteBuffer</code> para <code>byte[]</code>.
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 25/06/2014
 */
public class ByteBufferConverter implements Converter<ByteBuffer, byte[]> {


  @Override
  public byte[] convert(ByteBuffer a) {
    if(a == null || a.limit() == 0)
      return null;
    byte[] bs = new byte[a.limit()];
    a.get(bs);
    return bs;
  }


  @Override
  public ByteBuffer reverse(byte[] b) {
    if(b == null || b.length == 0)
      return null;
    ByteBuffer buf = ByteBuffer.allocateDirect(b.length);
    buf.put(b);
    buf.flip();
    return buf;
  }
  

  /**
   * Converte parte do byte array <code>b</code> 
   * para um objeto <code>ByteBuffer</code>.
   * @param b Array a ser convertido.
   * @param offset Index de início da parte a ser convertida.
   * @param length Tamanho da parte a ser convertida.
   * @return <code>ByteBuffer</code> contendo a parte do 
   * byte array especificada.
   */
  public ByteBuffer reverse(byte[] b, int offset, int length) {
    if(b == null || b.length == 0 || offset < 0 
        || length < 1 || offset + length > b.length)
      return null;
    b = Arrays.copyOfRange(b, offset, offset+length);
    ByteBuffer buf = ByteBuffer.allocateDirect(b.length);
    buf.put(b);
    buf.flip();
    return buf;
  }
  
}
