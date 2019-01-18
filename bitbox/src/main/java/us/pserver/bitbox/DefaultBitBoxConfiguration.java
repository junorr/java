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

package us.pserver.bitbox;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import us.pserver.bitbox.util.Region;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2018
 */
public class DefaultBitBoxConfiguration implements BitBoxConfiguration {
  
  private final Map<Class,TypeSupport> support;
  
  private volatile BufferAlloc malloc;
  
  private volatile boolean useGetters;
  
  public DefaultBitBoxConfiguration() {
    this.support = new ConcurrentHashMap<>();
    this.malloc = BufferAlloc.HEAP_ALLOC;
    this.useGetters = true;
    TypeSupport<String> string = TypeSupport.of(
        String.class, 
        (s,b) -> b.put(StandardCharsets.UTF_8.encode(s)), 
        b -> StandardCharsets.UTF_8.decode(b).toString()
    );
    DefaultBitBoxConfiguration.this.addTypeSupport(string);
    TypeSupport<Instant> instant = TypeSupport.of(
        Instant.class, 
        (i,b) -> b.putLong(i.toEpochMilli()), 
        b -> Instant.ofEpochMilli(b.getLong())
    );
    DefaultBitBoxConfiguration.this.addTypeSupport(instant);
    TypeSupport<Date> date = TypeSupport.of(
        Date.class, 
        (d,b) -> b.putLong(d.getTime()), 
        b -> new Date(b.getLong())
    );
    DefaultBitBoxConfiguration.this.addTypeSupport(date);
    TypeSupport<Region> region = TypeSupport.of(
        Region.class, 
        (Region r, DynamicByteBuffer b) -> b.putInt(r.offset()).putInt(r.length()), 
        b -> Region.of(b.getInt(), b.getInt())
    );
    DefaultBitBoxConfiguration.this.addTypeSupport(region);
    TypeSupport<Region> region = TypeSupport.of(
        Region.class, 
        (Region r, DynamicByteBuffer b) -> b.putInt(r.offset()).putInt(r.length()), 
        b -> Region.of(b.getInt(), b.getInt())
    );
    DefaultBitBoxConfiguration.this.addTypeSupport(region);
  }
  
  @Override
  public ByteBuffer alloc(int size) {
    return malloc.alloc(size);
  }
  
  @Override
  public BitBoxConfiguration addTypeSupport(TypeSupport ts) {
    if(ts != null) {
      Optional<TypeSupport> opt = support.values().stream().filter(t -> t.classID() == ts.classID()).findAny();
      if(opt.isPresent()) {
        throw new IllegalArgumentException(String.format("Class ID clash: %s(%d)", opt.get().classType().getName(), ts.classID()));
      }
      support.put(ts.classType(), ts);
    }
    return this;
  }
  
  @Override
  public BitBoxConfiguration setBufferAlloc(BufferAlloc alloc) {
    if(alloc != null) {
      this.malloc = alloc;
    }
    return this;
  }
  
  @Override
  public BufferAlloc getBufferAlloc() {
    return malloc;
  }
  
  @Override
  public BitBoxConfiguration setUseGetters(boolean use) {
    this.useGetters = use;
    return this;
  }
  
  @Override
  public boolean isUseGetters() {
    return this.useGetters;
  }
  
  @Override
  public Optional<TypeSupport> getTypeSupport(Class cls) {
    return Optional.ofNullable(support.get(cls));
  }
  
  @Override
  public Optional<TypeSupport> removeTypeSupport(Class cls) {
    return Optional.ofNullable(support.remove(cls));
  }
  
}
