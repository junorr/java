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
import java.util.concurrent.atomic.AtomicLong;
import us.pserver.tools.FileSizeFormatter;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/08/2015
 */
public class CounterInputStream extends FilterInputStream {

  private final AtomicLong count;
  
  
  protected CounterInputStream(InputStream in) {
		super(in);
    count = new AtomicLong(0L);
  }
  
  
  public long getCount() {
    return count.get();
  }
  
  
  public String getCountFormatted() {
    return new FileSizeFormatter()
        .format(count.get());
  }
	
	
	@Override
	public int read() throws IOException {
		byte[] bs = new byte[1];
		int r = this.read(bs);
		return (r > 0 ? bs[0] : r);
	}
	
	
	@Override
	public int read(byte[] bs, int off, int len) throws IOException {
		Valid.off(bs).forEmpty().fail("Invalid empty byte array");
		Valid.off(off).forLesserThan(0).fail("Invalid offset: ");
		Valid.off(len).forLesserThan(1)
				.or().forGreaterThan(bs.length-off)
				.fail("Invalid length: ");
		int r = in.read(bs, off, len);
		if(r > 0) count.addAndGet(r);
		return r;
	}
	
	
	@Override
	public int read(byte[] bs) throws IOException {
		return this.read(Valid.off(bs).forEmpty()
				.getOrFail("Invalid empty byte array"),
				0, bs.length
		);
	}
  
}
