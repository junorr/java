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

import java.io.EOFException;
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
  
  private Consumer<SearchableInputStream> cs;
  
  
  public SearchableInputStream(InputStream in, byte[] pattern) {
    super(Valid.off(in).forNull()
        .getOrFail(InputStream.class)
    );
    this.pattern = Valid.off(pattern).forEmpty()
        .getOrFail("Invalid byte array");
    buffer = new ArrayDeque<>(pattern.length);
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
  
  
	public int readByte() throws IOException, EOFException {
    if(count == pattern.length) {
			if(cs != null) cs.accept(this);
      throw new EOFException();
    }
		if(count <= 0 && !buffer.isEmpty()) {
			return buffer.poll();
		}
    byte[] bs = new byte[1];
    int r = in.read(bs);
    if(r < 1) {
			count = -1;
			if(buffer.isEmpty())
				throw new EOFException();
			else
				return buffer.poll();
		}
    if(pattern[count] == bs[0]) {
      buffer.add(bs[0]);
      count++;
      r = this.readByte();
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
		total++;
    return r;
  }
  
  
  @Override
  public int read() throws IOException {
    try {
			return readByte();
		} catch(EOFException e) {
			return -1;
		}
  }
	
	
	@Override
	public int read(byte[] bs, int off, int len) throws IOException {
		Valid.off(bs).forEmpty().fail("Invalid empty byte array");
		Valid.off(off).forLesserThan(0).fail("Invalid offset: ");
		Valid.off(len).forLesserThan(1)
				.or().forGreaterThan(bs.length-off)
				.fail("Invalid length: ");
		int count = 0;
		for(int i = off; i < (len+off); i++) {
			try {
				int r = this.readByte();
				bs[i] = (byte) r;
				count++;
			} catch(EOFException e) {
				break;
			};
		}
		return count;
	}
	
	
	@Override
	public int read(byte[] bs) throws IOException {
		return this.read(
				Valid.off(bs).forEmpty()
						.getOrFail("Invalid empty byte array"), 
				0, bs.length
		);
	}

}
