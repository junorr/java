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
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2018
 */
public class InputStreamView {
  
  public static final int DEFAULT_BUFFER_SIZE = 32*1024;
  
  public static final int MIN_BUFFER_SIZE = 128;
  
  private final InputStream input;
  
  private final Buffer buffer;
  
  private long total;
  
  
  public InputStreamView(InputStream in, int bufsize) {
    this.input = Objects.requireNonNull(in, "Bad null InputStream");
    if(bufsize < MIN_BUFFER_SIZE) {
      throw new IllegalArgumentException("Bad buffer size: "+ bufsize+ "(<"+ MIN_BUFFER_SIZE+ ")");
    }
    this.buffer = Buffer.directFactory().create(bufsize);
    total = 0;
  }
  
  public InputStreamView(InputStream in) {
    this(in, DEFAULT_BUFFER_SIZE);
  }
  
  
  public long find(Buffer cont) throws IOException {
    if(cont == null || !cont.isReadable()) {
      throw new IllegalArgumentException("Bad Buffer: "+ (cont == null ? cont : cont.readLength()));
    }
    int read = 0;
    long count = 0;
    do {
      if(buffer.isReadable()) {
        int idx = buffer.find(cont);
        count += (idx >= 0 ? idx : read);
        if(idx >= 0) {
          return count;
        }
      }
    } while((read = buffer.fillBuffer(input)) != -1);
    return -1;
  }
  
  
  public long skip(long n) throws IOException {
    long count = 0;
    do {
      count += buffer.skip((int)(n - count));
      if(!buffer.isReadable()) buffer.clear();
    } while(count < n && buffer.fillBuffer(input) != -1);
    return count;
  }
  
  
  public long flushUntil(OutputStream out, Buffer cont) throws IOException {
    Objects.requireNonNull(out, "Bad null OutputStream");
    Objects.requireNonNull(cont, "Bad null Buffer");
    int idx = -1;
    long count = 0;
    do {
      if(buffer.isReadable()) {
        int readlen = buffer.readLength();
        buffer.readMark();
        idx = buffer.find(cont);
        int len = readlen - buffer.readLength();
        count += len;
        buffer.readReset().writeTo(out, len);
      }
      else buffer.clear();
    } while(idx < 0 && buffer.fillBuffer(input) != -1);
    return count;
  }
  
  
  public Buffer cacheUntil(Buffer cont) throws IOException {
    Objects.requireNonNull(cont, "Bad null Buffer");
    Buffer cache = Buffer.expansibleHeapFactory().create(buffer.capacity());
    int idx = -1;
    long count = 0;
    do {
      if(buffer.isReadable()) {
        int readlen = buffer.readLength();
        buffer.readMark();
        idx = buffer.find(cont);
        int len = readlen - buffer.readLength();
        count += len;
        buffer.readReset().writeTo(cache, len);
      }
      else buffer.clear();
    } while(idx < 0 && buffer.fillBuffer(input) != -1);
    return cache;
  }
  
}
