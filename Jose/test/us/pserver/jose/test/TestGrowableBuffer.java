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

package us.pserver.jose.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import us.pserver.jose.util.Buffer;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/03/2017
 */
public class TestGrowableBuffer {
  
  public static final int DATA_SIZE = 1024*1024*100;

  
  public static void main(String[] args) throws IOException {
    System.out.print("* Create ByteArrayOutputStream: ");
    Timer tm = new Timer.Nanos().start();
    ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
    System.out.println(tm.stop());
    
    System.out.print("* Fill Data in ByteArrayOutputStream: ");
    tm.clear().start();
    for(int i = 0; i < DATA_SIZE; i++) {
      bos.write(i);
    }
    System.out.println(tm.stop());
    
    System.out.println("-------------------------------------");
    
    System.out.print("* Create Buffer: ");
    tm.clear().start();
    Buffer buf = Buffer.create(4096);
    byte[] bs = new byte[2048];
    System.out.println(tm.stop());
    
    System.out.print("* Fill Data in Buffer: ");
    int ibs = 0;
    tm.clear().start();
    for(int i = 0; i < DATA_SIZE; i++) {
      bs[ibs++] = (byte)(i+1);
      if(ibs >= bs.length) {
        buf.write(bs, 0, ibs);
        ibs = 0;
      }
    }
    buf.write(bs, 0, ibs);
    System.out.println(tm.stop());
    System.out.println("* buf.size()="+ buf.size());
    System.out.print("* toBytes(): ");
    tm.clear().start();
    byte[] bbuf = buf.toBytes();
    System.out.println(tm.stop());
    //System.arraycopy(bbuf, 0, bs, 0, bs.length);
    //System.out.println(Arrays.toString(bs));
    
    System.out.println("-------------------------------------");
    
    System.out.print("* Create Heap ByteBuffer: ");
    tm.clear().start();
    ByteBuffer bb = ByteBuffer.allocate(DATA_SIZE);
    System.out.println(tm.stop());
    
    System.out.print("* Fill Data in Heap ByteBuffer: ");
    tm.clear().start();
    for(int i = 0; i < DATA_SIZE; i++) {
      bb.put((byte) (i+1));
    }
    System.out.println(tm.stop());
    System.out.println("* bb.position()="+ bb.position());
    
    System.out.println("-------------------------------------");
    
    System.out.print("* Create Direct ByteBuffer: ");
    tm.clear().start();
    bb = ByteBuffer.allocateDirect(DATA_SIZE);
    System.out.println(tm.stop());
    
    System.out.print("* Fill Data in Direct ByteBuffer: ");
    tm.clear().start();
    for(int i = 0; i < DATA_SIZE; i++) {
      bb.put((byte) (i+1));
    }
    System.out.println(tm.stop());
    System.out.println("* bb.position()="+ bb.position());
  }
  
}
