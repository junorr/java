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
public class ByteableDouble extends AbstractByteableNumber {

  public ByteableDouble(double d) {
    super(Long.BYTES);
    long n = Double.doubleToLongBits(d);
    for(int i = bytes.length -1; i >= 0; i--) {
      bytes[i] = (byte)(n & 0xFF);
      n >>= bytes.length;
    }
  }
  
  public double getDouble() {
    long result = 0;
    for(int i = 0; i < bytes.length; i++) {
      result <<= bytes.length;
      result |= (bytes[i] & 0xFF);
    }
    return Double.longBitsToDouble(result);
  }
  
  @Override
  public Double getNumber() {
    return getDouble();
  }
  
}
