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

package us.pserver.finalson.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import us.pserver.tools.fn.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/01/2018
 */
public class LocalDateMapping implements TypeMapping<LocalDate> {
  
  @Override
  public JsonElement toJson(LocalDate obj) {
    return Rethrow.unchecked().apply(()->
        new JsonPrimitive(DateTimeFormatter.ISO_DATE.format(obj))
    );
  }

  @Override
  public boolean accept(Class type) {
    return LocalDate.class.isAssignableFrom(type);
  }

  @Override
  public LocalDate fromJson(JsonElement elt) {
    return Rethrow.unchecked().apply(()->
        LocalDate.from(DateTimeFormatter.ISO_DATE.parse(elt.getAsString()))
    );
  }

}
