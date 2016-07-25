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

package us.pserver.streams.test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/08/2015
 */
public class TestArrayPerformance {

  
  public static void main(String[] args) {
    ByteBuffer bs = ByteBuffer.allocateDirect(4096);
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < bs.capacity()-1; i++) {
      byte b = (byte)i;
      if(b==-1)b=0;
      bs.put(b);
    }
    bs.put((byte)-1);
    System.out.println("* time to fill 4096: "+ tm.lapAndStop());
    tm = new Timer.Nanos().start();
    bs.flip();
    int max = bs.remaining();
    for(int i = 0; i < max; i++) {
      if(bs.get()==-1)break;
    }
    System.out.println("* Time to NO parallel iterate over 4096 array: "+ tm.lapAndStop());
    
    
    byte[] bb = new byte[4096];
    tm = new Timer.Nanos().start();
    for(int i = 0; i < bb.length; i++) {
      byte b = (byte)i;
      if(b==-1)b=0;
      bb[i] = b;
    }
    bb[bb.length-1] = -1;
    System.out.println("* time to fill 4096: "+ tm.lapAndStop());
    tm = new Timer.Nanos().start();
    for(int i = 0; i < bb.length; i++) {
      if(bb[i]==-1)break;
    }
    System.out.println("* Time to NO parallel iterate over 4096 array: "+ tm.lapAndStop());
  }
  
}
