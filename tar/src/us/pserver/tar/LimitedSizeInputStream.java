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

package us.pserver.tar;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/10/2015
 */
public class LimitedSizeInputStream extends FilterInputStream {

  
  private long count;
      
  private final long max;
  
  
  public LimitedSizeInputStream(InputStream src, long maxSize) {
    super(Valid.off(src).forNull()
        .getOrFail(InputStream.class)
    );
    max = Valid.off(maxSize).forLesserThan(1)
        .getOrFail("Max Size must be greater than zero");
    count = 0;
  }
  
  
  public long getReadCount() {
    return count;
  }
  
  
  public long getMaxSize() {
    return max;
  }
  
  
  @Override
  public int read() throws IOException {
    if(count >= max) return -1;
    int r = super.read();
    count++;
    if(r == -1) count = max;
    return r;
  }
  
  
  @Override
  public int read(byte[] buf, int off, int len) throws IOException {
    if(count >= max) return -1;
    int rlen = (int) Math.min((max - count), len);
    int r = super.read(buf, off, rlen);
    count += r;
    if(r == -1) count = max;
    return r;
  }
  
  
  @Override
  public int read(byte[] buf) throws IOException {
    return read(
        Valid.off(buf).forNull().getOrFail(), 
        0, buf.length
    );
  }
  
  
  @Override
  public void close() throws IOException {
    super.close();
    count = max;
  }
  
  
}
