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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/08/2015
 */
public class StoppableInputStream extends FilterInputStream {
  
  public static final int DEFAULT_BUFFER_SIZE = 8192;
  
  
  private final InputStream input;
  
  private final byte[] stopBytes;
  
  private byte[] buffer1;
  
  private int read1;
  
  private byte[] buffer2;
  
  private int read2;
  
  private final AtomicBoolean stop;
  
  private final ExecutorService exec;
  
  
  public StoppableInputStream(InputStream source, byte[] stopBytes) {
    super(source);
    input = Valid.off(source).forNull()
        .getOrFail(InputStream.class);
    this.stopBytes = Valid.off(stopBytes)
        .forEmpty().getOrFail("Invalid empty stop bytes");
    stop = new AtomicBoolean(false);
    buffer1 = new byte[DEFAULT_BUFFER_SIZE/2];
    buffer2 = new byte[DEFAULT_BUFFER_SIZE/2];
    read1 = read2 = 0;
    exec = Executors.newFixedThreadPool(1);
  }
  
  
  public byte[] getStopBytes() {
    return stopBytes;
  }
  
  
  public InputStream getSource() {
    return input;
  }
  
  
  public boolean isStopped() {
    return stop.get();
  }
  
  
  private void fillAndCompare() throws Exception {
    if(stop.get()) return;
    read1 = input.read(buffer1);
    if(read1 < 1) stop.set(true);
    Future<Boolean> future = exec.submit(
        new Searcher(stopBytes, buffer1, 0, read1)
    );
    read2 = input.read(buffer2);
    stop.set(future.get());
    if(!stop.get()) {
      future = exec.submit(
          new Searcher(stopBytes, buffer1, 0, read2)
      );
      stop.set(future.get());
    }
  }
  
  
  
  
  private static class Searcher implements Callable<Boolean> {
    
    private final byte[] stop;
    
    private final byte[] buffer;
    
    private final int off, len;
    
    
    public Searcher(
        final byte[] stopBytes, 
        final byte[] buffer, 
        final int off, 
        final int len
    ) {
      this.stop = stopBytes;
      this.buffer = buffer;
      this.off = off;
      this.len = len;
    }
    
    
    @Override
    public Boolean call() throws Exception {
      int count = 0;
      for(int i = off; i < (len-stop.length); i++) {
        if(buffer[i] == stop[0]) {
          count++;
          for(int j = 1; j < stop.length; j++) {
            if(buffer[i+j] == stop[j])
              count++;
          }
        } 
        else count = 0;
        if(count == stop.length) {
          return true;
        }
      }
      return false;
    }
    
  }
  
}
