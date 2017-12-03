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

import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/11/2017
 */
public interface ByteableNumber {

  public byte[] getBytes();
  
  public ByteBuffer toByteBuffer();
  
  public Number getNumber();
  
  
  
  public static ByteableNumber of(short n) {
    return new ByteableShort(n);
  }
  
  
  public static ByteableNumber of(int n) {
    return new ByteableInteger(n);
  }
  
  
  public static ByteableNumber of(long n) {
    return new ByteableLong(n);
  }
  
  
  public static ByteableNumber of(float n) {
    return new ByteableFloat(n);
  }
  
  
  public static ByteableNumber of(double n) {
    return new ByteableDouble(n);
  }
  
}
