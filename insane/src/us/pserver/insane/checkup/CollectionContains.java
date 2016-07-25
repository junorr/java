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

package us.pserver.insane.checkup;

import java.util.Arrays;
import java.util.Collection;
import us.pserver.insane.SanityCheck;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public class CollectionContains implements SanityCheck<Collection> {

  private final Object[] elts;
  
  
  public CollectionContains(Object ... elt) {
    this.elts = elt;
  }
  
  
  @Override
  public boolean test(Collection col) {
    return Arrays.asList(elts).stream().allMatch(
        e -> col.stream().anyMatch(o -> o.equals(e))
    );
  }
  
  
  @Override
  public String failMessage() {
    return String.format("Collection must contains all elements in %s", Arrays.toString(elts));
  }
  
}
