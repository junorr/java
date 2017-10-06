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

package us.pserver.jom.def;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import us.pserver.jom.MappedValue;
import us.pserver.jom.Mapper;
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
    return maps.stream().anyMatch(m->m.canMap(cls));
  }
  
  private Optional<Mapper> getMapper(Class cls) {
    return maps.stream().filter(m->m.canMap(cls)).findFirst();
  }
  
  @Override
  public MappedValue map(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    Optional<Mapper> mapper = getMapper(obj.getClass());
    if(mapper.isPresent()) {
      //System.out.println(" - "+ o +" - mapper: "+ maps.stream().filter(m->m.canMap(o.getClass())).findFirst());
      return mapper.get().map(obj);
    }
    else {
      Map<String,MappedValue> map = new SerializableHashMap<>();
      Field[] fs = Reflector.of(obj).fields();
      for(Field f : fs) {
        Object of = Reflector.of(obj).selectField(f.getName()).get();
        if(of != null) {
          //HERE, OBJECT_STORE QUEUE<STORE_UNIT>
          map.put(f.getName(), map(of));
        }
      }
      return MappedValue.of(map);
    }
  }
  
  @Override
  public Object unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(value).failIfNull("Bad null value");
    Optional<Mapper> mapper = this.getMapper(cls);
    if(mapper.isPresent()) {
      //System.out.println(" - "+ o +" - unmapper: "+ maps.stream().filter(m->m.canMap(cls)).findFirst());
      return mapper.get().unmap(cls, value);
    }
    else {
      Map<String,MappedValue> map = value.asMap();
      Object cob = Reflector.of(cls).create();
      Field[] fs = Reflector.of(cls).fields();
      for(Field f : fs) {
        if(map.containsKey(f.getName())) {
          MappedValue val = map.get(f.getName());
          //System.out.println(" - set: "+ f.getName()+ " = "+ val.getClass());
          //System.out.println(" - field.unmapped: "+ unmap(f.getType(), val));
          Reflector.of(cob).selectField(f.getName()).set(unmap(f.getType(), val));
        }
      }
      return cob;
    }
  }
  
}
