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

package gson.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class BeanBuilder {

  private final Map<String, Object> props;
  
  
  public BeanBuilder() {
    props = new HashMap<>();
  }
  
  
  public BeanBuilder put(String name, Object value) {
    if(name != null && value != null) {
      props.put(name, value);
    }
    return this;
  }
  
  
  public Object remove(String name) {
    Object val = null;
    if(name != null && props.containsKey(name)) {
      val = props.remove(name);
    }
    return val;
  }
  
  
  public Map<String, Object> properties() {
    return props;
  }
  
  
  public List<String> keys() {
    List<String> keys = Collections.EMPTY_LIST;
    if(!props.isEmpty()) {
      keys = new ArrayList<>(props.keySet());
    }
    return keys;
  }
  
  
  public List values() {
    List vals = Collections.EMPTY_LIST;
    if(!props.isEmpty()) {
      vals = new ArrayList<>(props.values());
    }
    return vals;
  }
  
  
  public <T> T build(Class<T> cls) throws BuildingException {
    T t = null;
    if(!props.isEmpty()) {
      Reflector ref = new Reflector();
      ref.on(cls);
      t = (T) ref.create();
      ref.on(t);
      props.forEach((k,v)->{
        //System.out.println("[BeanBuilder].prop{"+ k+ ": "+ v+ "}");
        if(ref.field(k).isFieldPresent() && v != null) {
          ref.set(v);
        }
      });
    }
    return t;
  }
  
}
