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

package us.pserver.streams.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class TrimmedSearchableInputStream extends FilterInputStream {
  
  private final InputStream source;
  
  private Consumer<TrimmedSearchableInputStream> action;
  
  private final byte[] stopFactor;
  
  private final LimitedBuffer buffer;
  
  private boolean stopped, found;
  
  private final AtomicLong count;
  
  
  public TrimmedSearchableInputStream(InputStream src, final byte[] stopOn) {
    this(src, stopOn, null);
  }
  
  
  public TrimmedSearchableInputStream(InputStream src, final byte[] stopOn, Consumer<TrimmedSearchableInputStream> cs) {
    super(src);
    Valid.off(src).forNull().fail(InputStream.class);
    Valid.off(stopOn).forEmpty().fail("Invalid stop content");
    source = src;
    action = cs;
    stopped = found = false;
    buffer = new LimitedBuffer(stopOn.length);
    stopFactor = stopOn;
    count = new AtomicLong(0L);
  }
  
  
  public long getCount() {
    return count.get();
  }
  
  
  public InputStream getSource() {
    return source;
  }


  public byte[] getStopFactor() {
    return stopFactor;
  }
  
  
  public boolean isStopReached() {
    return found;
  }
  
  
  private void fillBuffer() throws IOException {
    if(stopped) return;
    long before = count.get();
    while(!stopped 
        && buffer.size() < stopFactor.length) {
      readOne();
    }
    if(count.get() == before) {
      readOne();
    }
  }
  
  
  private void readOne() throws IOException {
    byte[] bs = new byte[1];
    int read = source.read(bs);
    if(read < 1) {
      stopped = true;
    } else {
      count.incrementAndGet();
      buffer.put(bs[0]);
    }
  }
  
  
  private boolean readAndStop() throws IOException {
    fillBuffer();
    stopped = found = Arrays.equals(stopFactor, buffer.buffer());
    if(stopped && action != null) {
      action.accept(this);
    }
    return stopped;
  }
  
  
  @Override
  public int read() throws IOException {
    return (readAndStop() ? -1 : buffer.get(0));
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(stopped) return -1;
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forNotBetween(0, bs.length -1)
        .fail("Invalid off set: ");
    Valid.off(len).forNotBetween(1, bs.length-off)
        .fail("Invalid length: ");
    
    int read = 0;
    for(int i = off; i < (off+len); i++) {
      byte b = (byte) read();
      if(stopped) {
        return read;
      }
      read++;
      bs[i] = b;
    }
    return read;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, (bs != null ? bs.length : 0));
  }
  
}
