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

package us.pserver.finalson.construct;

import com.google.gson.JsonElement;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public abstract class AbstractConstructorParam<T> implements ConstructorParam<T> {
  
  protected final Class type;
  
  protected final String name;
  
  protected final int index;
  
  protected AbstractConstructorParam(Class type, String name, int index) {
    this.type = NotNull.of(type).getOrFail("Bad null type Class");
    this.name = NotNull.of(name).getOrFail("Bad null name");
    this.index = index;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public int getIndex() {
    return index;
  }
  
  @Override
  public Class getType() {
    return type;
  }
  
}
