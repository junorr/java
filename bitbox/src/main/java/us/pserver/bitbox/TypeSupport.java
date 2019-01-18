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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2018
 */
public interface TypeSupport<T> {
  
  public Class classType();
  
  public int classID();

  public BiConsumer<T,DynamicByteBuffer> serialize();
  
  public Function<ByteBuffer,T> deserialize();
  
  public Set<Supplier> mappingMethods();
  
  
  
  public static <U> TypeSupport<U> of(Class<U> classType, BiConsumer<U,DynamicByteBuffer> serialize, Function<ByteBuffer,U> deserialize) {
    return new DefTypeSupport(classType, classType.getName().hashCode(), serialize, deserialize, Collections.EMPTY_SET);
  }
  
  public static <U> TypeSupport<U> of(Class<U> classType, BiConsumer<U,DynamicByteBuffer> serialize, Function<ByteBuffer,U> deserialize, Collection<Supplier> mappingMethods) {
    return new DefTypeSupport(classType, classType.getName().hashCode(), serialize, deserialize, mappingMethods);
  }
  
  public static <U> TypeSupport<U> of(Class<U> classType, int classID, BiConsumer<U,DynamicByteBuffer> serialize, Function<ByteBuffer,U> deserialize, Collection<Supplier> mappingMethods) {
    return new DefTypeSupport(classType, classID, serialize, deserialize, mappingMethods);
  }
  
  
  
  
  
  static class DefTypeSupport<T> implements TypeSupport<T> {
    
    private final Class classType;
    
    private final int classID;
    
    private final BiConsumer<T,DynamicByteBuffer> serialize;
    
    private final Function<ByteBuffer,T> deserialize;
    
    private final Set<Supplier> mappingMethods;
    
    public DefTypeSupport(Class<T> classType, int classID, BiConsumer<T,DynamicByteBuffer> serialize, Function<ByteBuffer,T> deserialize, Collection<Supplier> mappingMethods) {
      this.classType = Objects.requireNonNull(classType);
      this.classID = classID;
      this.serialize = Objects.requireNonNull(serialize);
      this.deserialize = Objects.requireNonNull(deserialize);
      this.mappingMethods = new HashSet<>(mappingMethods);
    }
    
    @Override
    public Class<T> classType() {
      return classType;
    }
    
    @Override
    public int classID() {
      return classID;
    }

    @Override
    public BiConsumer<T,DynamicByteBuffer> serialize() {
      return serialize;
    }
    
    @Override
    public Set<Supplier> mappingMethods() {
      return mappingMethods;
    }
    
    @Override
    public Function<ByteBuffer,T> deserialize() {
      return deserialize;
    }
    
    @Override
    public int hashCode() {
      int hash = 7;
      hash = 47 * hash + Objects.hashCode(this.deserialize);
      hash = 47 * hash + Objects.hashCode(this.serialize);
      hash = 47 * hash + Objects.hashCode(this.mappingMethods);
      return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
      if(this == obj) {
        return true;
      }
      if(obj == null) {
        return false;
      }
      if(getClass() != obj.getClass()) {
        return false;
      }
      final DefTypeSupport<?> other = (DefTypeSupport<?>) obj;
      if(!Objects.equals(this.serialize, other.serialize)) {
        return false;
      }
      if(!Objects.equals(this.deserialize, other.deserialize)) {
        return false;
      }
      return Objects.equals(this.mappingMethods, other.mappingMethods);
    }
    
    @Override
    public String toString() {
      return "DefTypeSupport{" + "serialize=" + serialize + ", deserialize=" + deserialize + ", mappingMethods=" + mappingMethods + '}';
    }
    
  }
  
}
