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

package us.pserver.jose.query.op;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2017
 */
public final class BooleanInArray extends BooleanOperation {
  
  final List<Boolean> array;
  
  public BooleanInArray() { 
    super(); 
    this.array = Collections.EMPTY_LIST;
  }
  
  public BooleanInArray(List<Boolean> array) { 
    super(); 
    if(array == null) {
      throw new IllegalArgumentException("Bad Null Array");
    }
    this.array = array;
  }

  public List<Boolean> array() {
    return array;
  }

  @Override
  public boolean apply(Boolean other) {
    return other != null 
        && array.stream().anyMatch(b->other.booleanValue() == b.booleanValue());
  }

}
