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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/09/2017
 */
public class ObjectMapper extends AbstractMapper {
  
  protected final List<Mapper> maps;
  
  public ObjectMapper() {
    super(Object.class);
    this.maps = new ArrayList<>();
    maps.add(new StringMapper());
    maps.add(new NumberMapper());
    maps.add(new BooleanMapper());
    maps.add(new DateMapper());
    maps.add(new InstantMapper());
    maps.add(new LocalDateTimeMapper());
    maps.add(new ZonedDateTimeMapper());
    maps.add(new PathMapper());
    maps.add(new ClassMapper());
    maps.add(new ByteArrayMapper());
    maps.add(new ByteBufferMapper());
    maps.add(new ArrayMapper(this));
    maps.add(new ListMapper(this));
    maps.add(new SetMapper(this));
  }
  
  public List<Mapper> mappers() {
    return maps;
  }
  
  @Override
  public boolean canMap(Class cls) {
    return true;
  }
  
  public Map<String,Object> toMap(Object o) {
    return (Map) map(o);
  }

  @Override
  public Object map(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    if(MappingUtils.isNativeSupported(obj.getClass())) {
      //System.out.println(" - "+ o +" - mapper: "+ maps.stream().filter(m->m.canMap(o.getClass())).findFirst());
      return maps.stream().filter(m->m.canMap(obj.getClass())).findFirst().get().map(obj);
    }
    else {
      Map<String,Object> map = new SerializableMap<>();
      Field[] fs = Reflector.of(obj).fields();
      for(Field f : fs) {
        Object of = Reflector.of(obj).selectField(f.getName()).get();
        if(of != null) {
          map.put(f.getName(), map(of));
        }
      }
      return map;
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
