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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import us.pserver.cdr.ByteBufferConverter;
import static us.pserver.chk.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/07/2014
 */
public class BufferOutputStream extends OutputStream {

  public static final int BUFFER_SIZE = 2048;
  
  
  private ByteBuffer buffer, flush;
  
  private ByteBufferConverter conv;
  
  private OutputStream out;
  
  
  public BufferOutputStream() {
    conv = new ByteBufferConverter();
    buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    flush = ByteBuffer.allocateDirect(BUFFER_SIZE);
  }
  
  
  public BufferOutputStream(OutputStream os) {
    nullarg(OutputStream.class, os);
    out = os;
    conv = new ByteBufferConverter();
    buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    flush = ByteBuffer.allocateDirect(BUFFER_SIZE);
  }
  
  
  public void reset() {
    buffer.clear();
    flush.clear();
  }
  
  
  public OutputStream getOutputStream() {
    return out;
  }
  
  
  public BufferOutputStream setOutputStream(OutputStream os) throws IOException {
    if(out != null) {
      finish();
    }
    out = os;
    reset();
    return this;
  }
  
  
  @Override
  public void close() {}
  
  
  public void finish() throws IOException {
    check();
    flushAll();
    out.close();
  }
  
  
  @Override
  public void flush() throws IOException {
    check();
    if(flush.position() > 0) {
      flush.flip();
      out.write(conv.convert(flush));
      out.flush();
      flush.clear();
    }
  }
  
  
  public void flushAll() throws IOException {
    check();
    transferBuffers();
    flush();
  }
  
  
  private void transferBuffers() throws IOException {
    if(buffer.position() == 0) {
      flush();
      return;
    }
    buffer.flip();
    while(buffer.remaining() > 0) {
      if(flush.remaining() == 0)
        flush();
      flush.put(buffer.get());
    }
    buffer.clear();
  }
  
  
  private void check() throws IOException {
    if(out == null)
      throw new IOException("OutputStream not initialized");
  }


  @Override
  public void write(int b) throws IOException {
    check();
    if(buffer.remaining() < 1)
      transferBuffers();
    buffer.put((byte) b);
  }
  
}
