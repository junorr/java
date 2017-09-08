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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/09/2017
 */
public class ObjectStore {
  
  private final MapProvider mprov;
  
  private final ObjectMapper mapper;
  
  
  public ObjectStore(MapProvider mpv) {
    this.mprov = NotNull.of(mpv).getOrFail("Bad null MapProvider");
    this.mapper = new ObjectMapper();
  }
  
  
  public MapProvider getMapProvider() {
    return mprov;
  }
  
  
  public ObjectUID store(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    Class cls = obj.getClass();
    if(MappingUtils.isNativeSupported(cls)) {
      throw new IllegalArgumentException("Object must be a POJO");
    }
    else {
      ObjectUID uid = ObjectUID.of(obj);
      mprov.apply("uid-class").put(uid.getUID(), cls.getName());
      Field[] fs = Reflector.of(obj).fields();
      for(Field f : fs) {
        Object of = Reflector.of(obj).selectField(f.getName()).get();
        if(of != null) {
          mprov.get(f).put(mapper.map(of), uid);
        }
      }
      return uid;
    }
  }
  
  @Override
  public Object unmap(Class cls, Object obj) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(obj).failIfNull("Bad null object");
    if(MappingUtils.isNativeSupported(cls)) {
      //System.out.println(" - "+ o +" - unmapper: "+ maps.stream().filter(m->m.canMap(cls)).findFirst());
      return maps.stream().filter(m->m.canMap(cls)).findFirst().get().unmap(cls, obj);
    }
    else {
      Map map = (Map) obj;
      Object cob = Reflector.of(cls).create();
      Field[] fs = Reflector.of(cls).fields();
      for(Field f : fs) {
        if(map.containsKey(f.getName())) {
          Object of = map.get(f.getName());
          //System.out.println(" - set: "+ f.getName()+ " = "+ of.getClass());
          //System.out.println(" - field.unmapped: "+ unmap(f.getType(), of));
          Reflector.of(cob).selectField(f.getName()).set(unmap(f.getType(), of));
        }
      }
      return cob;
    }
  }
  
}
