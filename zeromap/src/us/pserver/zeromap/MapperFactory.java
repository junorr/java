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

package us.pserver.zeromap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import us.pserver.zeromap.mapper.ArrayMapper;
import us.pserver.zeromap.mapper.BooleanMapper;
import us.pserver.zeromap.mapper.CharMapper;
import us.pserver.zeromap.mapper.ClassMapper;
import us.pserver.zeromap.mapper.CollectionMapper;
import us.pserver.zeromap.mapper.DateMapper;
import us.pserver.zeromap.mapper.FileMapper;
import us.pserver.zeromap.mapper.MapMapper;
import us.pserver.zeromap.mapper.NumberMapper;
import us.pserver.zeromap.mapper.ObjectMapper;
import us.pserver.zeromap.mapper.PathMapper;
import us.pserver.zeromap.mapper.PrimitiveArrayMapper;
import us.pserver.zeromap.mapper.StringMapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class MapperFactory {
	
	private final List<Mapper<?>> mappers;
	
	private final List<String> ignored;
	
	private final Map<Class,ObjectBuilder> builders;
	
	
	public MapperFactory() {
		ignored = new ArrayList<>();
		builders = new LinkedHashMap<>();
		mappers = new ArrayList<>();
		mappers.add(new ArrayMapper());
		mappers.add(new BooleanMapper());
		mappers.add(new CharMapper());
		mappers.add(new ClassMapper());
		mappers.add(new CollectionMapper());
		mappers.add(new DateMapper());
		mappers.add(new FileMapper());
		mappers.add(new MapMapper());
		mappers.add(new NumberMapper());
		mappers.add(new PathMapper());
		mappers.add(new PrimitiveArrayMapper());
		mappers.add(new StringMapper());
	}
	
	
	public static MapperFactory factory() {
		return new MapperFactory();
	}
	
	
	public MapperFactory ignore(String field) {
		if(field != null) {
			ignored.add(field);
		}
		return this;
	}
	
	
	public MapperFactory ignore(Class field) {
		return ignore((field != null 
				? field.getName() : null)
		);
	}
	
	
	public List<String> ignoredList() {
		return ignored;
	}
	
	
	public Map<Class,ObjectBuilder> builders() {
		return builders;
	}
	
	
	public MapperFactory register(Class cls, ObjectBuilder ob) {
		if(cls != null && ob != null) {
			builders.put(cls, ob);
		}
		return this;
	}
	
	
	public List<Mapper<?>> mappers() {
		return mappers;
	}
	
	
	public MapperFactory register(Mapper<?> mp) {
		if(mp != null) {
			mappers.add(mp);
		}
		return this;
	}
	
	
	public MapperFactory unregister(Mapper<?> mp) {
		if(mp != null) {
			mappers.remove(mp);
		}
		return this;
	}

	
	public boolean contains(Mapper<?> mp) {
		boolean cont = false;
		if(mp != null) {
			cont = mappers.contains(mp);
		}
		return cont;
	}

	
  public <T> Mapper<T> mapper(Class<T> cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Class must be not null");
    }
		Optional<Mapper<?>> opt = mappers.stream().filter(
				m->m.canHandle(cls)).findFirst();
    final Mapper<T> mpr;
    if(opt.isPresent()) {
      mpr = (Mapper<T>) opt.get();
    }
    else {
      mpr = (Mapper<T>) new ObjectMapper();
    }
    return mpr;
  }
  
}
