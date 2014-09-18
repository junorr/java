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

package us.pserver.jcs;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import javax.swing.SwingUtilities;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/04/2014
 */
public class BufferStream extends OutputStream {
  
  public static final int BUFFER_CAPACITY = 1500;
  
  public static final int BYTE_LN = 10;
  

  private Printable pr;
  
  private ByteBuffer buf;
  
  
  public BufferStream(Printable print) {
    this(print, BUFFER_CAPACITY);
  }
  
  
  public BufferStream(Printable print, int capacity) {
    if(print == null)
      throw new IllegalArgumentException(
          "Invalid Printable: "+ print);
    if(capacity <= 0) 
      throw new IllegalArgumentException(
          "Invalid capacity="+ capacity);
    pr = print;
    buf = ByteBuffer.allocate(capacity);
  }
  
  
  @Override
  public void write(int b) {
    if(buf == null)
      throw new IllegalStateException(
          "BufferStream is closed");
    buf.put((byte)b);
    if(b == BYTE_LN || buf.position() 
        == buf.capacity())
      flush();
  }
  
  
  @Override
  public void flush() {
    if(buf.position() == 0) return;
    byte[] bs = new byte[buf.position()];
    buf.flip();
    buf.get(bs);
    buf.clear();
    final String str = new String(bs);
    SwingUtilities.invokeLater(()->{
      if(str.contains("\n")) pr.print(str);
      else pr.println(str);
    });
  }
  
  
  @Override
  public void close() {
    buf = null;
  }
  
}
