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
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/08/2015
 */
public class BufferInputStream extends CounterInputStream {
  
  private ByteArrayInputStream source;
  
  
  public BufferInputStream() {
    super();
    source = null;
  }
  
  
  public BufferInputStream(byte[] source) {
    super();
    setSource(source);
  }
  
  
  public BufferInputStream setSource(byte[] source) {
    this.source = new ByteArrayInputStream(
        Valid.off(source).forEmpty()
            .getOrFail("Invalid empty source byte array")
    );
    return this;
  }
  
  
  @Override
  public int read(byte[] array, int off, int len) {
    Valid.off(array).forEmpty()
        .fail("Invalid byte array");
    Valid.off(off).forLesserThen(0)
        .fail("Invalid off set: ");
    Valid.off(len).forGreaterThen(array.length - off)
        .fail("Invalid length: ");
    
    if(source == null || source.available() < 1) {
      return -1;
    }
    int read = source.read(array, off, len);
    return increment(read);
  }
  
  
  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int read = read(bs, 0, bs.length);
    if(read <= 0) return -1;
    return bs[0];
  }

}
