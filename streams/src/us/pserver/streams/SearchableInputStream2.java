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
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/11/2015
 */
public class SearchableInputStream2 extends FilterInputStream {
  
  private final ByteBuffer buffer;
  
  private final byte[] search;
  
  private int count;
  
  private boolean finish;
  
  
  public SearchableInputStream2(InputStream in, byte[] search) {
    super(Valid.off(in).forNull()
        .getOrFail(InputStream.class)
    );
    this.search = Valid.off(search).forEmpty()
        .getOrFail("Invalid search bytes");
    buffer = ByteBuffer.allocate(search.length);
    finish = false;
  }
  
  
  public byte[] getSearchBytes() {
    return search;
  }
  
  
  private void fill() throws IOException {
    if(finish) return;
    int size = search.length;
    if(count > 0) {
      size = search.length - count;
    }
    buffer.clear();
    byte[] bs = new byte[size];
    int read = in.read(bs);
    if(read < 1) {
      finish = true;
      return;
    }
    buffer.put(bs, 0, read);
    buffer.flip();
  }
  
  
  private boolean search() throws IOException {
    if(finish) return false;
    int max = Math.min(search.length - count, buffer.remaining());
    for(int i = 0; i < max; i++) {
      if(buffer.get() != search[count++]) {
        count = 0;
      }
      if(count == search.length) {
        return true;
      }
    }
    if(count > 0) {
      System.out.println("* SearchableInputStream2.count="+ count);
      buffer.compact();
    }
    else buffer.rewind();
    return false;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(finish) {
      return -1;
    }
    Valid.off(bs).forNull().fail("Invalid byte array");
    Valid.off(off).forLesserThan(0).fail("Invalid offset: ");
    Valid.off(len).forLesserThan(1)
        .or().forGreaterThan(bs.length - off)
        .fail("Invalid length: ");
    if(!buffer.hasRemaining()) {
      fill();
      if(search()) {
        finish = true;
        if(!buffer.hasRemaining())
          return -1;
      }
    }
    int nlen = Math.min(buffer.remaining(), len);
    buffer.get(bs, off, nlen);
    return nlen;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    if(finish) {
      return -1;
    }
    return read(Valid.off(bs).forNull()
        .getOrFail("Invalid byte array"), 
        0, bs.length
    );
  }
  
  
  @Override
  public int read() throws IOException {
    if(finish) {
      return -1;
    }
    byte[] bs = new byte[1];
    int read = this.read(bs);
    if(read == 1) read = bs[0];
    return read;
  }

}
