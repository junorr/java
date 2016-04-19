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

package us.pserver.zerojs.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.exception.JsonParseException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/04/2016
 */
public class DateValue extends AbstractJsonValue<Date> {

  public DateValue(String value) {
    super(value);
  }
  
  
  @Override
  public Date getValue() throws JsonParseException {
    if(this.value == null) {
      if(stringValue.length() == 24 
          && stringValue.contains("T") 
          && stringValue.contains("Z")) {
        return parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      }
      else if(stringValue.length() == 10
          && stringValue.contains("-")) {
        return parse("yyyy-MM-dd");
      }
      else if(stringValue.length() == 21 
          && stringValue.contains("T")) {
        return parse("yyyy-MM-dd'T'HH:mm:ss");
      }
      else if(stringValue.length() == 21 
          && stringValue.contains(" ")) {
        return parse("yyyy-MM-dd HH:mm:ss");
      }
      else if(stringValue.length() == 16 
          && stringValue.contains(" ")) {
        return parse("yyyy-MM-dd HH:mm");
      }
      else {
        throw new JsonParseException(
            "Unparseable Date value: "+ stringValue
        );
      }
    }
    return value;
  }
  
  
  private Date parse(String format) throws JsonParseException {
    try {
      return new SimpleDateFormat(format).parse(stringValue);
    }
    catch(ParseException e) {
      throw new JsonParseException(e.getMessage(), e);
    }
  }


  @Override
  public void notify(JsonHandler jsh) throws JsonParseException {
    if(jsh != null) {
      jsh.value(this.getValue());
    }
  }
  
}
