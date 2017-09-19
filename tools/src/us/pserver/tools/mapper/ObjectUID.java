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

package us.pserver.tools.mapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import us.pserver.tools.Hash;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2017
 */
public class ObjectUID implements Serializable {

  private final String uid;
  
  private final String className;
  
  
  private ObjectUID(String cname, String uid) {
    this.uid = uid;
    this.className = cname;
  }
  
  public String getHash() {
    return this.uid;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  
  public static Builder builder() {
    return new Builder(null, null);
  }
  
  
  
  
  
  public static class Builder {
    
    public static final Supplier<Hash> DEFAULT_HASH_SHA1 = Hash::sha1;
    
    private final String uid;
    
    private final String className;
    
    
    public Builder(String uid, String className) {
      this.uid = uid;
      this.className = className;
    }
    
    
    public Builder withHash(String uid) {
      return new Builder(NotNull.of(uid).getOrFail("Bad null UID"), this.className);
    }
    
    
    public Builder withClass(Class cls) {
      return new Builder(this.uid, NotNull.of(cls).getOrFail("Bad null Class").getName());
    }
    
    
    public Builder withClassName(String cname) {
      return new Builder(this.uid, NotNull.of(cname).getOrFail("Bad null class name"));
    }
    
    
    public Builder of(Object obj) {
      NotNull.of(obj).failIfNull();
      Hash hash = DEFAULT_HASH_SHA1.get();
      calcUID(hash, obj);
      return withClass(obj.getClass()).withHash(hash.get());
    }
    
    
    public Builder of(String className, Map<String,MappedValue> map) {
      NotNull.of(map).failIfNull();
      Hash hash = DEFAULT_HASH_SHA1.get();
      calcUID(hash, map);
      return withClassName(className).withHash(hash.get());
    }
    
    
    public Builder of(Hash hash, Object obj) {
      NotNull.of(obj).failIfNull();
      calcUID(NotNull.of(hash).getOrFail("Bad null Hash"), obj);
      return withClass(obj.getClass()).withHash(hash.get());
    }
    
    
    public Builder of(Hash hash, String className, Map<String,MappedValue> map) {
      NotNull.of(map).failIfNull();
      calcUID(NotNull.of(hash).getOrFail("Bad null Hash"), map);
      return withClassName(className).withHash(hash.get());
    }
    
    
    public ObjectUID build() {
      return new ObjectUID(this.className, this.uid);
    }
    
    
    private void calcUID(Hash hash, Object obj) {
      if(MappingUtils.isNativeSupported(obj.getClass())) {
        hash.put(obj.getClass().getName());
        hash.put(Objects.toString(obj));
      }
      else {
        hash.put(obj.getClass().getName());
        Field[] fs = Reflector.of(obj).fields();
        for(Field f : fs) {
          Object of = Reflector.of(obj).selectField(f.getName()).get();
          if(of != null) {
            calcUID(hash, of);
          }
        }
      }
    }
    
    
    private void calcUID(Hash hash, Map<String,MappedValue> map) {
      Set<Map.Entry<String,MappedValue>> set = NotNull.of(map).getOrFail("Bad null Map").entrySet();
      for(Map.Entry<String,MappedValue> e : set) {
        hash.put(e.getKey());
        if(MappedValue.Type.MAP == e.getValue().getType()) {
          calcUID(hash, e.getValue().asMap());
        }
        else {
          hash.put(e.getValue().toString());
        }
      }
    }
    
  }
  
}
