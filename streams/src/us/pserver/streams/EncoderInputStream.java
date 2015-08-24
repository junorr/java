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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/08/2015
 */
public class EncoderInputStream extends CounterInputStream {
  
  public static final int BUFFER_SIZE = 1024 * 256; // 256KB

  private final InputStream source;
  
  private final int bufsize;
  
  private final StreamCoderFactory coder;
  
  private final ByteArrayOutputStream bytesOut;
  
  private ByteArrayInputStream bytesIn;
  
  private final OutputStream encOut;
  
  
  public EncoderInputStream(InputStream source, StreamCoderFactory coder) throws IOException {
    this(source, coder, 0);
  }
  
  
  public EncoderInputStream(InputStream source, StreamCoderFactory coder, int bufferSize) throws IOException {
    super();
    this.source = Valid.off(source).forNull()
        .getOrFail(InputStream.class);
    this.coder = Valid.off(coder).forNull()
        .getOrFail(StreamCoderFactory.class);
    bufsize = (bufferSize > 0 
        ? bufferSize : BUFFER_SIZE);
    bytesOut = new ByteArrayOutputStream();
    encOut = coder.create(bytesOut);
  }
  
  
  public int getBufferSize() {
    return bufsize;
  }
  
  
  public InputStream getSourceInputStream() {
    return source;
  }
  
  
  public StreamCoderFactory getStreamCoderFactory() {
    return coder;
  }
  
  
  @Override
  public void close() throws IOException {
    source.close();
    encOut.close();
  }
  
  
  @Override
  public int read(byte[] array, int off, int len) throws IOException {
    Valid.off(array).forEmpty()
        .fail("Invalid byte array");
    Valid.off(off).forLesserThan(0)
        .fail("Invalid off set: ");
    Valid.off(len).forGreaterThan(array.length - off)
        .fail("Invalid length: ");
    
    fill();
    if(bytesIn == null || bytesIn.available() < 1) {
      return -1;
    }
    return increment(bytesIn.read(array, off, len));
  }
  
  
  private void fill() throws IOException {
    if(bytesIn != null && bytesIn.available() > 0)
      return;
    bytesOut.reset();
    byte[] array = new byte[4096];
    while(bytesOut.size() < bufsize) {
      int read = source.read(array);
      if(read <= 0) {
        encOut.close();
        break;
      }
      encOut.write(array, 0, read);
      encOut.flush();
    }
    if(bytesOut.size() > 0) {
      bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
    }
  }
  

  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int read = read(bs, 0, bs.length);
    if(read <= 0) return -1;
    return bs[0];
  }

}
