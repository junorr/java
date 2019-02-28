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

package us.pserver.orb.types;

import java.util.Arrays;
import java.util.List;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public abstract class AbstractTypeString<T> implements TypeString<T> {
  
  protected final List<Class> types;
  
  public AbstractTypeString(Class ... types) {
    this(Arrays.asList(Match.notEmpty(types).getOrFail()));
  }
  
  public AbstractTypeString(List<Class> types) {
    this.types = Match.notEmpty(types).getOrFail();
  }
  
  @Override
  public boolean isTypeOf(Class type) {
    return types.stream().anyMatch(c->type.isAssignableFrom(c));
  }
  
  @Override 
  public abstract T apply(String str) throws TypeStringException;
  
}
