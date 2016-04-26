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

package us.pserver.zerojs.impl;

import java.util.LinkedList;
import java.util.List;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonParser;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public abstract class AbstractJsonParser implements JsonParser {

  protected final List<JsonHandler> handlers;
  
  
  protected AbstractJsonParser() {
    this.handlers = new LinkedList<>();
  }
  
  
  @Override
  public JsonParser addHandler(JsonHandler jsh) {
    if(jsh != null) {
      handlers.add(jsh);
    }
    return this;
  }


  @Override
  public boolean removeHandler(JsonHandler jsh) {
    return handlers.remove(jsh);
  }


  @Override
  public List<JsonHandler> getHandlers() {
    return handlers;
  }

}
