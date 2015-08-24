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

package us.pserver.xprops.transformer;

import java.util.Objects;
import us.pserver.tools.Valid;

/**
 * Abstract implementation for StringTransformer, 
 * provides default behavior for the
 * <code>toString( T )</code> method with
 * <code>Objects::toString( Object )</code>.
 * @author Juno Roesler - juno@pserver.us
 */
public abstract class AbstractStringTransformer<T> implements StringTransformer<T> {

  @Override
  public String toString(T obj) throws IllegalArgumentException {
    return Objects.toString(Valid.off(obj)
        .forNull().getOrFail("Invalid Object to Transform: ")
    );
  }

}