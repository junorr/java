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

import us.pserver.streams.StreamCoderFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/04/2015
 */
public abstract class AbstractMixedBuffer implements MixedBuffer {

  protected boolean valid;
  
  protected ByteBuffer buffer;
  
  protected File temp;
  
  protected RandomAccessFile raf;
  
  protected StreamCoderFactory coderfac;
  
  
  protected AbstractMixedBuffer() {
    valid = true;
    coderfac = new StreamCoderFactory();
  }
  
  
  @Override
  public StreamCoderFactory getCoderFactory() {
    return coderfac;
  }
  
  
  public AbstractMixedBuffer setCoderFactory(StreamCoderFactory fac) {
    if(fac != null) {
      coderfac = fac;
    }
    return this;
  }
  
  
  @Override
  public File getTempFile() {
    return temp;
  }
  
  
  @Override
  public ByteBuffer getInternalBuffer() {
    return buffer;
  }
  
  
  @Override
  public boolean isValid() {
    return valid;
  }
  
  
  private void validate() {
    if(!valid) {
      throw new IllegalStateException("This buffer becomes invalid");
    }
  }
  
  
  @Override
  public MixedBuffer seek(long pos) throws IOException {
    validate();
    if(pos < 0 || pos > length()) {
      throw new IndexOutOfBoundsException(
          "[MixedBuffer.seek( long )] Invalid position {pos="+ pos+ "}");
    }
    if(pos < buffer.limit()) {
      buffer.position((int) pos);
    }
    else if(raf != null && raf.length() >= pos - buffer.limit()) {
      raf.seek(pos - buffer.limit());
    }
    return this;
  }
  
  
  @Override
  public long length() throws IOException {
    validate();
    long sz = buffer.limit();
    if(raf != null)
      sz += raf.length();
    return sz;
  }
  
  
  @Override
  protected void finalize() {
    try {
      super.finalize();
      this.close();
    } catch(Throwable th) {}
  }
  
  
  @Override
  public void close() {
    valid = false;
    try {
      buffer.clear();
      if(raf != null) {
        raf.close();
        temp.deleteOnExit();
        temp.delete();
        raf = null;
        temp = null;
      }
    } catch(Exception e) {}
  }
  
}
