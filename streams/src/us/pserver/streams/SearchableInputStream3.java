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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/11/2015
 */
public class SearchableInputStream3 extends FilterInputStream {
  
  private int count;
  
  private long total;
  
  private final byte[] pattern;
  
  private ArrayDeque<Byte> buffer;
  
  private int max;
  
  private boolean rmode;
  
  
  public SearchableInputStream3(InputStream in, byte[] pattern) {
    super(Valid.off(in).forNull()
        .getOrFail(InputStream.class)
    );
    this.pattern = Valid.off(pattern).forEmpty()
        .getOrFail("Invalid byte array");
    max = pattern.length;
    buffer = new ArrayDeque<>(max);
    count = 0;
    total = 0;
    rmode = false;
  }
  
  
  public long getTotal() {
    return total;
  }
  
  
  public byte[] getSearchPattern() {
    return pattern;
  }
  
  
  public int left() {
    return max - buffer.size();
  }
  
  
  @Override
  public int read() throws IOException {
    if(count == pattern.length) {
      return -1;
    }
    byte[] bs = new byte[1];
    int r = super.read(bs);
    if(r < 1) {
      return -1;
    }
    if(pattern[count] == bs[0]) {
      buffer.add(bs[0]);
      count++;
      return this.read();
    }
    else {
      if(left() > 0) {
        r = buffer.poll();
        buffer.offer(bs[0]);
      }
      else {
        r = bs[0];
      }
      count = 0;
    }
    return r;
  }

}
