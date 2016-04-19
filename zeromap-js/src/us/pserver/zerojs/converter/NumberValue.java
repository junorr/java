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

import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.exception.JsonParseException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public class NumberValue extends AbstractJsonValue<Number> {

  
  public NumberValue(String value) {
    super(value);
  }
  
  
  @Override
  public Number getValue() throws JsonParseException {
    if(this.value == null) {
      try {
        if(stringValue.contains(".")) {
          return Double.parseDouble(stringValue);
        } else {
          return Long.parseLong(stringValue);
        }
      }
      catch(NumberFormatException e) {
        throw new JsonParseException(e.getMessage(), e.getCause());
      }
    }
    return this.value;
  }


  @Override
  public void notify(JsonHandler jsh) throws JsonParseException {
    if(jsh != null) {
      jsh.value(this.getValue());
    }
  }

}
