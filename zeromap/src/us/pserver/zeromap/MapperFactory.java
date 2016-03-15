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

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import us.pserver.zeromap.mapper.ArrayMapper;
import us.pserver.zeromap.mapper.BooleanMapper;
import us.pserver.zeromap.mapper.CharMapper;
import us.pserver.zeromap.mapper.ClassMapper;
import us.pserver.zeromap.mapper.CollectionMapper;
import us.pserver.zeromap.mapper.DateMapper;
import us.pserver.zeromap.mapper.FileMapper;
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
public abstract class MapperFactory {

  public static <T> Mapper<T> mapper(Class<T> cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Class must be not null");
    }
    Mapper<T> map = null;
    if(Number.class.isAssignableFrom(cls)
        || byte.class == cls
        || short.class == cls 
        || int.class == cls 
        || long.class == cls 
        || float.class == cls
        || double.class == cls) {
      map = (Mapper<T>) new NumberMapper((Class<? extends Number>) cls);
    }
    else if(Boolean.class.isAssignableFrom(cls)
        || boolean.class == cls) {
      map = (Mapper<T>) new BooleanMapper();
    }
    else if(CharSequence.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new StringMapper();
    }
    else if(Character.class.isAssignableFrom(cls)
				|| char.class == cls) {
      map = (Mapper<T>) new CharMapper();
    }
    else if(Date.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new DateMapper();
    }
    else if(File.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new FileMapper();
    }
    else if(Path.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new PathMapper();
    }
    else if(Collection.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new CollectionMapper();
    }
    else if(Class.class.isAssignableFrom(cls)) {
      map = (Mapper<T>) new ClassMapper();
    }
    else if(cls.isArray()) {
			if(cls.getComponentType().isPrimitive()) {
				map = (Mapper<T>) new PrimitiveArrayMapper(cls);
			} else {
				map = (Mapper<T>) new ArrayMapper<>();
			}
    }
    else {
			map = new ObjectMapper();
			/*
      throw new IllegalArgumentException(
          "Unknown Mapper type for: "+ cls
      );
			*/
    }
    return map;
  }
  
}
