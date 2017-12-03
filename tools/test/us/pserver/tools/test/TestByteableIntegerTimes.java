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

package us.pserver.tools.test;

import java.nio.ByteBuffer;
import us.pserver.tools.io.ByteableNumber;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/11/2017
 */
public class TestByteableIntegerTimes {
  
  public static final int TOTAL_INTS = 10_000_000;
  
  
  public static void encodeInts(ByteableNumber[] bn) {
    for(int i = 0; i < bn.length; i++) {
      bn[i] = ByteableNumber.of(i);
    }
  }
  
  
  public static void encodeIntsBuffer(ByteBuffer[] bs) {
    for(int i = 0; i < bs.length; i++) {
      bs[i] = ByteBuffer.wrap(new byte[Integer.BYTES]);
      bs[i].putInt(i);
    }
  }

  
  public static void encodeIntsBufferByteable(ByteBuffer[] bs) {
    for(int i = 0; i < bs.length; i++) {
      bs[i] = ByteableNumber.of(i).toByteBuffer();
    }
  }

  
  public static void main(String[] args) {
    System.out.printf("* encoding %d ints [ByteableNumber]...%n", TOTAL_INTS);
    ByteableNumber[] bn = new ByteableNumber[TOTAL_INTS];
    Timer tm = new Timer.Nanos().start();
    encodeInts(bn);
    System.out.printf("-- time to encode ints: %s --%n", tm.stop());
    
    System.out.printf("* encoding %d ints [ByteBuffer]...%n", TOTAL_INTS);
    ByteBuffer[] bs = new ByteBuffer[TOTAL_INTS];
    tm.clear().start();
    encodeIntsBuffer(bs);
    System.out.printf("-- time to encode ints: %s --%n", tm.stop());
    
    System.out.printf("* encoding %d ints [ByteBuffer with ByteableNumber]...%n", TOTAL_INTS);
    tm.clear().start();
    encodeIntsBufferByteable(bs);
    System.out.printf("-- time to encode ints: %s --%n", tm.stop());
  }
  
}
