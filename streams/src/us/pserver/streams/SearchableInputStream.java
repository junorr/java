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
import java.util.ArrayDeque;
import java.util.function.Consumer;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/11/2015
 */
public class SearchableInputStream extends FilterInputStream {
  
  private int count;
  
  private long total;
  
  private final byte[] pattern;
  
  private ArrayDeque<Byte> buffer;
  
  private int max;
  
	private Consumer<SearchableInputStream> cs;
  
  
  public SearchableInputStream(InputStream in, byte[] pattern) {
    super(Valid.off(in).forNull()
        .getOrFail(InputStream.class)
    );
    this.pattern = Valid.off(pattern).forEmpty()
        .getOrFail("Invalid byte array");
    max = pattern.length;
    buffer = new ArrayDeque<>(max);
    count = 0;
    total = 0;
  }
	
	
	public SearchableInputStream(
			InputStream in, 
			byte[] pattern, 
			Consumer<SearchableInputStream> cs
	) {
		this(in, pattern);
		this.cs = cs;
	}
	
	
	public InputStream getSourceStream() {
		return in;
	}
  
  
  public long getTotal() {
    return total;
  }
  
  
  public byte[] getSearchPattern() {
    return pattern;
  }
  
  
  int left() {
    return max - buffer.size();
  }
  
  
  @Override
  public int read() throws IOException {
    if(count == pattern.length) {
			if(cs != null) cs.accept(this);
      return -1;
    }
    byte[] bs = new byte[1];
    int r = super.read(bs);
		System.out.println(in.toString()+ ": read="+ bs[0]);
    if(r < 1) return -1;
    if(pattern[count] == bs[0]) {
      buffer.add(bs[0]);
			System.out.println("* SearchableIS.match: "+ bs[0]+ ", count="+ count);
      count++;
      r = this.read();
    }
    else {
			if(buffer.isEmpty()) {
				r = bs[0];
			}
			else {
				r = buffer.poll();
				buffer.add(bs[0]);
			}
			count = 0;
    }
    return r;
  }

}
