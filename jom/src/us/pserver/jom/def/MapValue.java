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

import java.util.Map;
import java.util.function.Consumer;
import us.pserver.jom.MappedValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public class MapValue extends AbstractMappedValue<Map<String,MappedValue>> {
  
  public MapValue() {
    super();
  }
  
  public MapValue(Map<String,MappedValue> value) {
    super(value, Type.MAP);
  }

  @Override
  public Map<String,MappedValue> asMap() {
    return this.get();
  }

  @Override
  public void ifMap(Consumer<Map<String,MappedValue>> exec) {
    exec.accept(this.get());
  }
  
}
