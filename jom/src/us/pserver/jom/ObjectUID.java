/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import us.pserver.jom.def.MappingUtils;
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


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.uid);
    hash = 97 * hash + Objects.hashCode(this.className);
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
    final ObjectUID other = (ObjectUID) obj;
    if (!Objects.equals(this.uid, other.uid)) {
      return false;
    }
    return Objects.equals(this.className, other.className);
  }


  @Override
  public String toString() {
    return "ObjectUID{" + "uid=" + uid + ", cls=" + className + '}';
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
