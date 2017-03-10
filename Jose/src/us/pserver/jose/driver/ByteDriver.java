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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import us.pserver.jose.Region;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.jose.json.iterator.ByteIteratorFactory;
import us.pserver.tools.UTF8String;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/03/2017
 */
public interface ByteDriver {

  public ByteBuffer getBuffer();
  
  public ByteBuffer get(Region reg);
  
  public ByteIterator iterator(Region reg);
  
  public ByteIterator iterator();
  
  public int search(Region reg, byte[] val);
  
  public int search(byte[] val);
  
  public int search(Region reg, String val);
  
  public int search(String val);
  
  public ByteBuffer readUntil(byte[] val);
  
  public ByteBuffer readBetween(byte[] start, byte[] end);
  
  public String readStringUntil(String val);
  
  public String readStringBetween(String start, String end);
  
  public ByteDriver seek(int pos);
  
  public ByteDriver put(ByteBuffer buf);
  
  public ByteDriver put(byte[] bts);
  
  
  public static ByteDriver of(ByteBuffer buf) {
    return new DefByteDriver(buf);
  }
  
  
  
  
  
  public static class DefByteDriver implements ByteDriver {
    
    private final ByteBuffer buffer;
    
    
    private static void check(ByteBuffer buf) {
      if(buf == null || !buf.hasRemaining()) {
        throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
      }
    }
    
    
    private static void check(byte[] val) {
      if(val == null || val.length < 1) {
        throw new IllegalArgumentException("Bad Null/Empty byte array");
      }
    }
    
    
    private static void check(Region reg) {
      if(reg == null || reg.start() < 0) {
        throw new IllegalArgumentException("Bad Region: "+ reg);
      }
    }
    
    
    public DefByteDriver(ByteBuffer buf) {
      check(buf);
      this.buffer = buf;
    }


    @Override
    public ByteBuffer getBuffer() {
      return buffer;
    }


    @Override
    public ByteBuffer get(Region reg) {
      check(reg);
      int pos = buffer.position();
      buffer.position(reg.start());
      ByteBuffer bb = buffer.slice();
      if(reg.length() > 0) {
        bb.limit(reg.length());
      }
      buffer.position(pos);
      return bb;
    }


    @Override
    public ByteIterator iterator(Region reg) {
      return ByteIteratorFactory.of(this.get(reg));
    }


    @Override
    public ByteIterator iterator() {
      return iterator(Region.of(buffer.position(), 0));
    }


    @Override
    public int search(Region reg, byte[] val) {
      check(reg);
      check(val);
      int lim = buffer.limit();
      buffer.position(reg.start()).limit(reg.start() + reg.length());
      int idx = this.search(val);
      buffer.limit(lim);
      return idx;
    }


    @Override
    public int search(byte[] val) {
      check(val);
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
    public int search(String val) {
      return search(UTF8String.from(val).getBytes());
    }


    @Override
    public int search(Region reg, String val) {
      return search(reg, UTF8String.from(val).getBytes());
    }


    @Override
    public ByteDriver seek(int pos) {
      if(pos < 0 || pos > buffer.capacity()) {
        throw new IllegalArgumentException("Bad Position: "+ pos);
      }
      return this;
    }


    @Override
    public ByteDriver put(ByteBuffer buf) {
      check(buf);
      buffer.put(buf);
      return this;
    }


    @Override
    public ByteDriver put(byte[] val) {
      check(val);
      buffer.put(val);
      return this;
    }


    @Override
    public ByteBuffer readUntil(byte[] val) {
      if(val == null || val.length < 1) {
        return ByteBuffer.wrap(new byte[0]);
      }
      int pos = this.buffer.position();
      int idx = this.search(val);
      if(idx < pos) {
        ByteBuffer.wrap(new byte[0]);
      }
      return get(Region.of(pos, idx-pos));
    }


    @Override
    public ByteBuffer readBetween(byte[] start, byte[] end) {
      if(end == null || end.length < 1
          || end == null || end.length < 1) {
        return ByteBuffer.wrap(new byte[0]);
      }
      int pos = this.search(start);
      int idx = this.search(end);
      if(pos < 0 || idx < pos) {
        ByteBuffer.wrap(new byte[0]);
      }
      return get(Region.of(pos, idx-pos));
    }


    @Override
    public String readStringUntil(String val) {
      ByteBuffer bb = readUntil(UTF8String.from(val).getBytes());
      if(!bb.hasRemaining()) return "";
      return StandardCharsets.UTF_8.decode(bb).toString();
    }


    @Override
    public String readStringBetween(String start, String end) {
      ByteBuffer bb = readBetween(
          UTF8String.from(start).getBytes(),
          UTF8String.from(end).getBytes()
      );
      if(!bb.hasRemaining()) return "";
      return StandardCharsets.UTF_8.decode(bb).toString();
    }
    
  }
  
}
