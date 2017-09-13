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
import java.util.Objects;
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
  
  private String className;
  
  private final transient Object obj;
  
  private final transient Hash hash;
  
  
  public ObjectUID(Hash hash, Object obj) {
    this.hash = NotNull.of(hash).getOrFail("Bad null Hash");
    this.obj = NotNull.of(obj).getOrFail("Bad null object");
    this.className = obj.getClass().getName();
    this.calcUID(obj);
    this.uid = hash.get();
  }
  
  public ObjectUID(Object obj) {
    this(Hash.sha1(), obj);
  }
  
  public String getUID() {
    return this.uid;
  }
  
  public Object getObject() {
    return this.obj;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  private void calcUID(Object obj) {
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
          calcUID(of);
        }
      }
    }
  }
  
  
  public static ObjectUID of(Object obj) {
    return new ObjectUID(obj);
  }
  
  public static ObjectUID of(Hash hash, Object obj) {
    return new ObjectUID(hash, obj);
  }
  
}
