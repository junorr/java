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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public class DateMapping implements TypeMapping<Date> {

  public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
  
  public static final String DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*";
  
  public static final String TIME_PATTERN = "\\d{2}:\\d{2}:\\d{2}.*";
  
  public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  
  @Override
  public JsonElement toJson(Date obj) {
    return new JsonPrimitive(DEFAULT_DATE_FORMAT.format(obj));
  }
  
  @Override
  public Date fromJson(JsonElement elt) {
    try {
      return DEFAULT_DATE_FORMAT.parse(elt.getAsString());
    } catch (ParseException ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }
  
  @Override
  public boolean accept(Class cls) {
    return Date.class.isAssignableFrom(cls);
  }
  
  
  public static boolean isJsonDate(JsonElement elt) {
    return elt.getAsString().matches(DATE_TIME_PATTERN)
        || elt.getAsString().matches(DATE_PATTERN);
  }
  
  
  public static boolean isJsonTime(JsonElement elt) {
    return elt.getAsString().matches(TIME_PATTERN);
  }
  
  
  public static boolean isAnyDateType(Class cls) {
    return Date.class.isAssignableFrom(cls)
        || TemporalAccessor.class.isAssignableFrom(cls);
  }
  
}
