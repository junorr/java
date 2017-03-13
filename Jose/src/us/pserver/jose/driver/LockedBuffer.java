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

package us.pserver.jose.driver;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/03/2017
 */
public interface LockedBuffer extends Closeable {

  public ByteBuffer getBuffer();
  
  public ByteReader<byte[]> getReader();
  
  public ByteReader<String> getStringReader();
  
  public void releaseLock();
  
  
  
  public static LockedBuffer of(ByteBuffer buf, Lock lock) {
    return null;
  }
  
  
  
  
  
  public static class LockedBufferImpl implements LockedBuffer {
    
    private final ByteBuffer buffer;
    
    private final Lock lock;
    
    private final ByteReader<byte[]> reader;
    
    
    public LockedBufferImpl(ByteBuffer buf, Lock lok) {
      if(buf == null) {
        throw new IllegalArgumentException("Bad Null ByteBuffer");
      }
      if(lok == null) {
        throw new IllegalArgumentException("Bad Null Lock");
      }
      this.buffer = buf;
      this.lock = lok;
      this.reader = ByteReader.of(buffer);
    }


    @Override
    public ByteBuffer getBuffer() {
      return buffer;
    }


    @Override
    public ByteReader<byte[]> getReader() {
      return reader;
    }


    @Override
    public ByteReader<String> getStringReader() {
      return StringByteReader.of(reader);
    }


    @Override
    public void releaseLock() {
      lock.unlock();
    }


    @Override
    public void close() {
      this.releaseLock();
    }
    
  }
  
}
