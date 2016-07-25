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

package us.pserver.fpack.test;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/11/2015
 */
public class TestByteBuffer {
  
  
  public static byte b(int b) {
    return (byte)b;
  }
  
  
  public static void m1() {
    ByteBuffer buf = ByteBuffer.allocate(5);
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.put(7);");
    buf.put(b(7));
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.flip();");
    buf.flip();
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.get()="+ buf.get());
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.flip();");
    buf.flip();
    System.out.println("buf.compact();");
    buf.compact();
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.put(2);");
    buf.put(b(2));
    System.out.println("buf.remaining()="+ buf.remaining());
    System.out.println("buf.put(0);");
    buf.put(b(0));
  }

  
  public static void m2() {
    int max = 5;
    ArrayDeque buf = new ArrayDeque(max);
    System.out.println("buf.remaining()="+ (max-buf.size()));
    System.out.println("buf.put(7);");
    buf.offer(b(7));
    System.out.println("buf.remaining()="+ (max-buf.size()));
    System.out.println("buf.get()="+ buf.poll());
    System.out.println("buf.remaining()="+ (max-buf.size()));
    System.out.println("buf.compact();");
    System.out.println("buf.remaining()="+ (max-buf.size()));
    System.out.println("buf.put(2);");
    buf.offer(b(2));
    System.out.println("buf.remaining()="+ (max-buf.size()));
    System.out.println("buf.put(0);");
    buf.offer(b(0));
  }

  
  public static void main(String[] args) {
    m2();
  }
  
}
