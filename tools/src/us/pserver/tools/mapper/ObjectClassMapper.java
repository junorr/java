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

import java.util.Map;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/09/2017
 */
public class ObjectClassMapper extends ObjectMapper {
  
  public static final String KEY_TYPE = "@type";

  public ObjectClassMapper() {
    super();
  }
  
  @Override
  public MappedValue map(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    MappedValue mop = super.map(obj);
    if(MappedValue.Type.MAP == mop.getType()) {
      mop.asMap().put(KEY_TYPE, super.map(obj.getClass()));
    }
    return mop;
  }
  
  public <T> T unmap(MappedValue value) {
    NotNull.of(value).failIfNull("Bad null value");
    if(MappedValue.Type.MAP == value.getType()) {
      Map<String,MappedValue> map = value.asMap();
      Class cls = (Class) super.unmap(Class.class, map.get(KEY_TYPE));
      return (T) super.unmap(cls, value);
    }
    else {
      return (T) super.unmap(value.get().getClass(), value);
    }
  }
  
}
