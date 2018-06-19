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

package us.pserver.dbone.serial;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class ExtendedSerializationService extends AbstractSerializationService {
  
  private final SerializationService sconf;
  
  public ExtendedSerializationService(SerializationService cfg, BiFunction<Class,Class,Boolean> ctest, IntFunction<ByteBuffer> allocpolicy, Map<Class,Serializer> serialmap, Map<Class,Deserializer> deserialmap) {
    super(ctest, allocpolicy, serialmap, deserialmap);
    this.sconf = Objects.requireNonNull(cfg, "Bad null SerializationService");
  }
  
  @Override
  public <T> Serializer<T> getSerializer(Class<T> cls) {
    Optional<Serializer> opt = serials.entrySet().stream()
        .filter(e->ctest.apply(e.getKey(), cls))
        .map(e->e.getValue())
        .findAny();
    if(opt.isPresent()) {
      return opt.get();
    }
    else {
      return sconf.getSerializer(cls);
    }
  }
  
  @Override
  public <T> Deserializer<T> getDeserializer(Class<T> cls) {
    Optional<Deserializer> opt = deserials.entrySet().stream()
        .filter(e->ctest.apply(e.getKey(), cls))
        .map(e->e.getValue())
        .findAny();
    if(opt.isPresent()) {
      return opt.get();
    }
    else {
      return sconf.getDeserializer(cls);
    }
  }
  
  
  public static Builder builder(SerializationService cfg) {
    return new Builder(cfg);
  }
  
  
  
  
  
  public static final class Builder {
    
    private final Map<Class,Serializer> serials;
    
    private final Map<Class,Deserializer> deserials;
    
    private BiFunction<Class,Class,Boolean> ctest;
    
    private IntFunction<ByteBuffer> alloc;
    
    private final SerializationService sconf;
    
    
    public Builder(SerializationService cfg) {
      this.sconf = Objects.requireNonNull(cfg, "Bad null SerializationService");
      this.serials = new HashMap<>();
      this.deserials = new HashMap<>();
      this.ctest = (c,t)->c.isAssignableFrom(t);
      this.alloc = ByteBuffer::allocate;
    }
    
    public <T> Builder addSerializer(Class<T> cls, Serializer<T> ser) {
      serials.put(
          Objects.requireNonNull(cls, "Bad null Class"), 
          Objects.requireNonNull(ser, "Bad null Serializer")
      );
      return this;
    }
    
    public Builder putSerializerMap(Map<Class,Serializer> sers) {
      serials.putAll(Objects.requireNonNull(sers, "Bad null Serializers Map"));
      return this;
    }
    
    public <T> Builder addDeserializer(Class<T> cls, Deserializer<T> ser) {
      deserials.put(
          Objects.requireNonNull(cls, "Bad null Class"), 
          Objects.requireNonNull(ser, "Bad null Deserializer")
      );
      return this;
    }
    
    public Builder putDeserializerMap(Map<Class,Deserializer> sers) {
      deserials.putAll(Objects.requireNonNull(sers, "Bad null Deserializers Map"));
      return this;
    }
    
    public Map<Class,Serializer> getSerializerMap() {
      return serials;
    }
    
    public Map<Class,Deserializer> getDeserializerMap() {
      return deserials;
    }
    
    public Builder setByteBufferAllocPolicy(IntFunction<ByteBuffer> alloc) {
      this.alloc = Objects.requireNonNull(alloc, "Bad null ByteBuffer allocation policy");
      return this;
    }
    
    public IntFunction<ByteBuffer> getByteBufferAllocPolicy() {
      return alloc;
    }
    
    public Builder setClassComparator(BiFunction<Class,Class,Boolean> ctest) {
      this.ctest = Objects.requireNonNull(ctest, "Bad null Class Predicate");
      return this;
    }
    
    public BiFunction<Class,Class,Boolean> getClassComparator() {
      return ctest;
    }
    
    public SerializationService getSerializationService() {
      return sconf;
    }
    
    public ExtendedSerializationService build() {
      return new ExtendedSerializationService(sconf, ctest, alloc, serials, deserials);
    }
    
  }
  
}
