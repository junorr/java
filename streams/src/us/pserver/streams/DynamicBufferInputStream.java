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

package us.pserver.streams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.streams.Checker.range;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/06/2014
 */
public class DynamicBufferInputStream extends InputStream {

  private ByteArrayInputStream bis;
  
  
  public DynamicBufferInputStream() {
    bis = null;
  }
  
  
  public DynamicBufferInputStream setBuffer(byte[] buf) {
    if(buf != null && buf.length > 0) {
      bis = new ByteArrayInputStream(
          Arrays.copyOf(buf, buf.length));
    }
    else bis = null;
    return this;
  }


  public DynamicBufferInputStream setBuffer(byte[] buf, int offset, int length) {
    if(buf != null && buf.length > 0) {
      range(offset, -1, buf.length -2);
      range(length, 1, buf.length - offset);
      bis = new ByteArrayInputStream(
          Arrays.copyOfRange(buf, offset, offset+length));
    }
    else bis = null;
    return this;
  }
  
  
  @Override
  public int available() throws IOException {
    if(bis == null) return -1;
    return bis.available();
  }


  @Override
  public int read() throws IOException {
    if(bis == null)
      return -1;
    return bis.read();
  }
  
}
