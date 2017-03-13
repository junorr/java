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

import java.nio.ByteBuffer;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import us.pserver.jose.Region;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.jose.json.iterator.ByteIteratorFactory;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/03/2017
 */
public interface ByteReader<T> {

  public ByteIterator iterator();
  
  public Stream<JsonType> stream();
  
  public int indexOf(byte[] val);
  
  public Region regionOf(byte[] off, byte[] until);
  
  public T read(Region reg);
  
  
  
  public static ByteReader of(ByteBuffer buf) {
    return null;
  }
  
  
  
  
  
  public static class ByteReaderImpl implements ByteReader<byte[]> {
    
    private final ByteBuffer buffer;
    
    private final ByteIterator iter;
    
    
    public ByteReaderImpl(ByteBuffer buf) {
      if(buf == null) {
        throw new IllegalArgumentException("Bad Null ByteBuffer");
      }
      this.buffer = buf;
      this.iter = ByteIteratorFactory.of(buffer);
    }
    
    
    private void resetBuffer() {
      buffer.position(0);
    }
    

    @Override
    public ByteIterator iterator() {
      this.resetBuffer();
      return iter;
    }


    @Override
    public Stream<JsonType> stream() {
      return StreamSupport.stream(
          Spliterators.spliterator(iterator(), 0, 0), false
      );
    }


    @Override
    public int indexOf(byte[] val) {
      if(val == null || val.length < 1) return -1;
      int idx = 0;
      while(idx < val.length && buffer.hasRemaining()) {
        byte b = buffer.get();
        if(b == val[idx]) {
          idx++;
        }
        else idx = 0;
      }
      if(idx != val.length) {
        return -1;
      } else {
        buffer.position(buffer.position() - idx);
        return buffer.position();
      }
    }


    @Override
    public Region regionOf(byte[] off, byte[] until) {
      if(off == null || off.length < 1
          || until == null || until.length < 1) {
        return Region.of(-1, -1);
      }
      int is = this.indexOf(off);
      int ie = this.indexOf(until);
      if(is < 0 || ie < is) {
        return Region.of(-1, -1);
      }
      return Region.of(is, ie-is);
    }


    @Override
    public byte[] read(Region reg) {
      if(reg == null || !reg.isValid()) {
        return new byte[0];
      }
      int len = Math.min(buffer.remaining(), reg.length());
      byte[] bs = new byte[len];
      int pos = buffer.position();
      buffer.position(reg.start());
      buffer.get(bs);
      buffer.position(pos);
      return bs;
    }
    
  }
  
}
