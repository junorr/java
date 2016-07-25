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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/10/2015
 */
public class OutputConnector {
  
  
  public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

  
  private final OutputStream output;
  
  private int bufferSize;
  
  private byte[] buffer;
  
  
  public OutputConnector(OutputStream out) {
    this(out, DEFAULT_BUFFER_SIZE);
  }
  
  
  public OutputConnector(OutputStream out, int bufferSize) {
    output = Valid.off(out).forNull().getOrFail(OutputStream.class);
    this.bufferSize = Valid.off(bufferSize)
        .forLesserThan(1)
        .getOrFail("Buffer size must be greater than zero");
  }
  
  
  public OutputStream getOutput() {
    return output;
  }
  
  
  public int getBufferSize() {
    return bufferSize;
  }
  
  
  public long connect(InputStream in) throws IOException {
    return connect(in, -1);
  }
  
  
  public long connect(InputStream in, long maxCount) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    long count = 0;
    int read = 0;
    if(buffer == null) {
      buffer = new byte[bufferSize];
    }
    int rlen = bufferSize;
    while(true) {
      if(maxCount > 0) {
        if(count >= maxCount) break;
        rlen = (int) Math.min((maxCount - count), buffer.length);
      }
      read = in.read(buffer, 0, rlen);
      if(read == -1) break;
      count += read;
      output.write(buffer, 0, read);
    }
    output.flush();
    return count;
  }
  
  
  public long connectAndClose(InputStream in) throws IOException {
    try {
      return connect(in);
    } finally {
      output.close();
      in.close();
    }
  }
  
  
}
