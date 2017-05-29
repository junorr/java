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

package us.pserver.job.json.adapter;

import java.nio.ByteBuffer;
import java.util.Arrays;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2017
 */
public class ByteSearch {
  
  public int search(byte[] val);
  
  
  public static ByteSearch of(byte )
  

  private final byte[] value;
  
  private final byte[] found;
  
  private int count;
  
  
  public ByteSearch(byte[] val) {
    this.value = val;
    this.found = new byte[val.length];
    this.count = 0;
  }
  
  
  public int search(ByteBuffer buf) {
    if(buf == null || buf.remaining() < value.length) {
      return -1;
    }
    int idx = 0;
    while(idx < value.length && buf.hasRemaining()) {
      count ++;
      byte b = buf.get();
      if(b == value[idx]) {
        found[idx] = b;
        idx++;
      }
      else idx = 0;
    }
    return (idx != value.length ? -1 : count - idx);
  }
  
  
  private boolean find(byte b, ByteBuffer buf) {
    count ++;
    if(buf.get() == b) return true;
    else return find(b, buf);
  }
  
  
  public static void main(String[] args) {
    String str0 = "Hell";
    String str1 = " Wor";
    String str3 = "d, ";
    String str = "";
    for(int i = 0; i < 100; i++) {
      str += str0 + i + str1 + ++i + str3;
    }
    System.out.println(str);
    String src = "Hell98";
    byte[] sbs = UTF8String.from(src).getBytes();
    System.out.println("* "+ src+ ": "+ Arrays.toString(sbs));
    byte[] bst = UTF8String.from(str).getBytes();
    System.out.println("* str: "+ Arrays.toString(bst));
    
    ByteBuffer buf = ByteBuffer.allocateDirect(bst.length);
    buf.put(bst);
    buf.flip();
    
    ByteSearch bs = new ByteSearch(sbs);
    Timer tm = new Timer.Nanos().start();
    int count = bs.search(buf);
    tm.stop();
    System.out.println("* found on index: "+ count);
    System.out.println("* bs.found="+ UTF8String.from(bs.found));
    System.out.println("* str.indexOf('"+ src+ "'): "+ str.indexOf(src));
    System.out.println(tm);
    
    if(buf.remaining() >= 7) {
      System.out.print("'");
      for(int i = 0; i < 7; i++) {
        System.out.write(buf.get());
      }
      System.out.print("'");
      System.out.flush();
      System.out.println();
    }
  }
  
}
