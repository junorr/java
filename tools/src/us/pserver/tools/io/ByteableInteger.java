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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/11/2017
 */
public class ByteableInteger extends AbstractByteableNumber {

  public ByteableInteger(int n) {
    super(Integer.BYTES);
    int shift = (bytes.length -1) * 8;
    for(int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte)(n >>> shift);
      shift -= 8;
    }
  }
  
  public int getInt() {
    int result = 0;
    int shift = 0;
    for(int i = bytes.length -1; i >= 0; i--) {
      result |= (bytes[i] & 0xFF) << shift;
      shift += 8;
    }
    return result;
  }
  
  @Override
  public Integer getNumber() {
    return getInt();
  }
  
}
