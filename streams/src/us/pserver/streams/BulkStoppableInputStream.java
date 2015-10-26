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
import java.util.function.Consumer;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/08/2015
 */
public class BulkStoppableInputStream extends FilterInputStream {
  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  
  
  private final PushbackInputStream input;
  
  private final byte[] stopBytes;
  
  private byte[] buffer;
  
  private int read, index, stopIndex;
  
  private boolean stopped;
  
  private final Consumer<BulkStoppableInputStream> listener;
  
  
  public BulkStoppableInputStream(InputStream source, byte[] stopBytes, Consumer<BulkStoppableInputStream> onstop) {
    super(source);
    if(PushbackInputStream.class.isAssignableFrom(source.getClass())) {
      input = (PushbackInputStream) source;
    }
    else {
      input = new PushbackInputStream(
          Valid.off(source).forNull()
              .getOrFail(InputStream.class), 
          DEFAULT_BUFFER_SIZE - 
              Valid.off(stopBytes).forEmpty()
                  .getOrFail("Invalid empty stop bytes")
                  .length
      );
    }
    this.stopBytes = Valid.off(stopBytes)
        .forEmpty().getOrFail("Invalid empty stop bytes");
    stopped = false;
    buffer = new byte[DEFAULT_BUFFER_SIZE];
	stopIndex = -1;
    read = index = 0;
    listener = onstop;
  }
  
  
  public byte[] getStopBytes() {
    return stopBytes;
  }
  
  
  public PushbackInputStream getInputStream() {
    return input;
  }
  
  
  public Consumer<BulkStoppableInputStream> getStopListener() {
    return listener;
  }
  
  
  public int getStopIndex() {
    return stopIndex;
  }
  
  
  public boolean isStopped() {
    return stopped;
  }
  
  
  @Override
  public void close() throws IOException {
    super.close();
    buffer = null;
    stopped = true;
  }
  
  
  private boolean compare() {
    if(stopped) return false;
    int count = 0;
    stopIndex = -1;
    for(int i = index; i < (read-stopBytes.length); i++) {
      if(buffer[i] == stopBytes[0]) {
        count++;
        stopIndex = i;
        for(int j = 1; j < stopBytes.length; j++) {
          if(buffer[i+j] == stopBytes[j])
            count++;
        }
      } 
      else count = 0;
      if(count == stopBytes.length) {
        return true;
      }
    }
    stopIndex = -1;
    return false;
  }
  
  
  private void fillAndCompare() throws IOException {
    if(stopped) return;
    if(index < read) return;
    read = input.read(buffer);
	if(read > 0)
	  System.out.println("\nBulk.fillAndCompare: ["+ new UTF8String(buffer, 0, read)+ "]");
    if(read < 1) {
      stopped = true;
      stopIndex = -1;
      return;
    }
    index = 0;
    stopped = compare();
    if(stopped && stopIndex >= 0 
        && stopIndex < buffer.length-stopBytes.length-1) {
      
      int ix = stopIndex + stopBytes.length;
      for(int i = ix; i <= read; i++) {
        input.unread(buffer[i]);
      }
	  read = stopIndex + 1;
    }
  }
  
  
  @Override
  public int read(byte[] array, int off, int len) throws IOException {
    if(stopped && index >= read) {
	  System.out.println("Bulk.read: first if");
      return -1;
    }
    Valid.off(array).forEmpty()
        .fail("Invalid empty array");
    Valid.off(off).forNotBetween(0, array.length-1)
        .fail("Invalid off set: ");
    Valid.off(len).forNotBetween(1, array.length-off)
        .fail("Invalid length: ");
    fillAndCompare();
    int nread = -1;
    int nlen = 0;
    if(stopIndex >= 0 && index < stopIndex) {
      nlen = Math.min(len, stopIndex - index);
    }
    else if(index < read) {
      nlen = Math.min(len, read - index);
    }
	System.out.println("\nBulk.read: index="+ index);
	System.out.println("Bulk.read: read ="+ read);
    if(nlen > 0) {
      System.arraycopy(buffer, index, array, off, nlen);
      nread = nlen;
      index += nlen;
    }
    if(stopped && stopIndex >= 0 && listener != null) {
      listener.accept(this);
    }
	if(nread == -1) {
	  System.out.println("Bulk.read: nread == -1");
	  System.out.println("Bulk.dump buffer: {"+ new UTF8String(buffer, 0, read+1));
	}
    return nread;
  }
  
  
  @Override
  public int read(byte[] array) throws IOException {
    return read(
        Valid.off(array).forEmpty()
            .getOrFail("Invalid empty array"), 
        0, array.length
    );
  }
  
  
  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int read = read(bs);
    if(read != -1) read = bs[0];
    return read;
  }
  
}
