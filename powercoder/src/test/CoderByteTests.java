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

package test;

import us.pserver.cdr.Coder;
import us.pserver.cdr.b64.Base64ByteCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/06/2014
 */
public class CoderByteTests {
  
  
  public static byte[] BYTES = new byte[255];
  
  
  public static void fillBytes() {
    for(int i = 0; i < BYTES.length; i++) {
      BYTES[i] = (byte) i;
    }
  }
  
  
  public static void fillRandom(byte[] bs) {
    for(int i = 0; i < bs.length; i++) {
      int rdm = (int) (Math.random() * BYTES.length);
      bs[i] = BYTES[rdm];
    }
  }

  
  public static void main(String[] args) {
    Coder<byte[]> cdr = new Base64ByteCoder();
    //Coder<byte[]> cdr = new HexByteCoder();
    fillBytes();
    
    byte[] ten = new byte[1024];
    for(int i = 0; i < 10; i++) {
      fillRandom(ten);
      String str = new String(ten);
      System.out.println("* str = '"+ str+ "'");
      System.out.println("* DEC length = "+ ten.length);
      byte[] out = cdr.encode(ten);
      str = new String(out);
      System.out.println("* str = '"+ str+ "'");
      System.out.println("* ENC length = "+ out.length);
      System.out.println("---------------------");
    }
  }
  
}
