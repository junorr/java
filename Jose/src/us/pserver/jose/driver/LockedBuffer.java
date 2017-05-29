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
 * @version 0.0 - 13/03/2017
 */
public interface LockedBuffer extends Closeable {

  public ByteBuffer getBuffer();
  
  public ByteReader getReader();
  
  public LockedBuffer reset();
  
  public void unlock();
  
  @Override public void close();
  
  
  
  public static LockedBuffer of(ByteBuffer buf, Lock lok) {
    return new LockedBufferImpl(buf, lok);
  }
  
  
  
  
  
  public static class LockedBufferImpl implements LockedBuffer {
    
    protected final ByteBuffer buffer;
    
    private final ByteReader reader;
    
    protected final Lock lock;
    
    
    protected LockedBufferImpl(ByteBuffer buf, Lock lok) {
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
    public LockedBuffer reset() {
      buffer.position(0);
      return this;
    }


    @Override
    public ByteReader getReader() {
      return reader;
    }


    @Override
    public ByteBuffer getBuffer() {
      return this.buffer;
    }


    @Override
    public void unlock() {
      lock.unlock();
    }


    @Override
    public void close() {
      lock.unlock();
    }
    
  }
  
}
