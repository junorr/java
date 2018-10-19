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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2018
 */
public interface IBuffer {

  public int capacity();
  
  public int size();
  
  public boolean isEmpty();
  
  public int seek(int pos);
  
  public int indexOf(byte[] cont);
  
  public int indexOf(IBuffer buf);
  
  public int fill(InputStream in) throws IOException;
  
  public int fill(ByteBuffer buf);
  
  public int fill(IBuffer buf);
  
  public int fill(IBuffer buf, int length);
  
  public int fill(byte[] src, int ofs, int len);
  
  public int writeTo(OutputStream out) throws IOException;
  
  public int writeTo(OutputStream out, int length) throws IOException;
  
  public int writeTo(ByteBuffer out);
  
  public int writeTo(ByteBuffer out, int length);
  
  public int writeTo(IBuffer out);
  
  public int writeTo(IBuffer out, int length);
  
  public int writeTo(byte[] out, int ofs, int len);
  
  public ByteBuffer toByteBuffer();
  
  public byte[] toByteArray();
  
  public String toString(Charset cs);
  
  public default String toUTF8String() {
    return toString(StandardCharsets.UTF_8);
  }
  
}
