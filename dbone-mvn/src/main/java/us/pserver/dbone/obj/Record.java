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

package us.pserver.dbone.obj;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.store.Writable;
import us.pserver.tools.misc.Final;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public interface Record<T> extends Writable {

  public Region region();
  
  public Record<T> withRegion(Region reg);
  
  public Class<T> clazz();
  
  public T value();
  
  public <U> Record<U> withValue(U val);
  
  
  
  
  public static <U> Record<U> of(Region reg, U val) {
    return new Default<>(reg, val);
  }
  
  
  public static <U> Record<U> of(Region reg) {
    return new Default<>(reg);
  }
  
  
  public static <U> Record<U> of(U val) {
    return new Default<>(val);
  }
  
  
  public static <U> Record<U> of(Region reg, ByteBuffer buf) throws ClassNotFoundException, IOException {
    Objects.requireNonNull(buf, "Bad null ByteBuffer");
    int classLength = buf.getInt();
    int lim = buf.limit();
    buf.limit(buf.position() + classLength);
    String scl = StandardCharsets.UTF_8.decode(buf).toString();
    buf.limit(lim);
    Class<?> cls = Class.forName(scl);
    String json = StandardCharsets.UTF_8.decode(buf).toString();
    U val = (U) ObjectMapperConfig.MAPPER_INSTANCE.get()
        .readerFor(cls).readValue(json);
    return Record.of(reg, val);
  }
  
  
  public static <U> Record<U> of(ByteBuffer buf) throws ClassNotFoundException, IOException {
    return Record.of(null, buf);
  }
  
  
  
  
  
  public static class Default<T> implements Record<T> {
    
    private final Region reg;
    
    private final T val;
    
    private final Class<T> cls;
    
    private final Final<ByteBuffer> buf;
    
    public Default(Region reg, T val) {
      this.reg = reg;
      this.val = val;
      this.cls = val != null ? (Class<T>) val.getClass() : null;
      this.buf = new Final<>();
    }
    
    public Default(T val) {
      this(null, val);
    }
    
    public Default(Region reg) {
      this(reg, null);
    }
    
    @Override
    public Region region() {
      return reg;
    }
    
    @Override
    public Record<T> withRegion(Region reg) {
      return new Default<>(reg, val);
    }
    
    @Override
    public Class<T> clazz() {
      return cls;
    }
    
    @Override
    public T value() {
      return val;
    }
    
    @Override
    public <U> Record<U> withValue(U val) {
      return new Default<>(reg, val);
    }
    
    @Override
    public int writeTo(WritableByteChannel ch, IntFunction<ByteBuffer> alloc) throws IOException {
      ByteBuffer bb = toByteBuffer(alloc);
      return ch.write(bb);
    }
    
    @Override
    public int writeTo(ByteBuffer wb) {
      if(!buf.isDefined()) {
        toByteBuffer(ByteBuffer::allocate);
      }
      if(wb.remaining() < buf.val().remaining()) {
        String msg = String.format("ByteBuffer not large enough: %s < %d", wb, buf.val().remaining());
        throw new IllegalArgumentException(msg);
      }
      wb.put(buf.val());
      buf.val().flip();
      return buf.val().remaining();
    }
    
    @Override
    public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> alloc) {
      if(buf.isDefined()) return buf.val();
      Objects.requireNonNull(val, "Bad null value");
      try {
        ByteBuffer bval = ByteBuffer.wrap(
            ObjectMapperConfig.MAPPER_INSTANCE.get()
                .writer().writeValueAsBytes(val)
        );
        ByteBuffer bcls = StandardCharsets.UTF_8.encode(val.getClass().getName());
        ByteBuffer bb = alloc.apply(Integer.BYTES + bcls.remaining() + bval.remaining());
        bb.putInt(bcls.remaining());
        bb.put(bcls);
        bb.put(bval);
        bb.flip();
        buf.tryDefine(bb);
        return bb;
      }
      catch(JsonProcessingException e) {
        throw new RuntimeException(e.toString(), e);
      }
    }
    
    @Override
    public int hashCode() {
      int hash = 7;
      hash = 97 * hash + Objects.hashCode(this.reg);
      hash = 97 * hash + Objects.hashCode(this.val);
      return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Default<?> other = (Default<?>) obj;
      if (!Objects.equals(this.reg, other.reg)) {
        return false;
      }
      return Objects.equals(this.val, other.val);
    }
    
    @Override
    public String toString() {
      return "Record{" + "reg=" + reg + ", cls=" + cls.getSimpleName() + ", val=" + val + '}';
    }

  } 
  
}
