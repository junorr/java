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

package us.pserver.finalson.handles;

import java.lang.reflect.Parameter;
import java.util.function.BiFunction;
import us.pserver.finalson.mapping.AcceptableType;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2018
 */
@FunctionalInterface
public interface ParameterMatch extends BiFunction<Parameter, JsonProperty, Boolean> {

  public static ParameterMatch matchByType() {
    return (p,j)->AcceptableType.isCompatible(j.getJson(), p.getType());
  }
  
  public static ParameterMatch matchByName() {
    return (p,j)->p.getName().equals(j.getName());
  }
  
  public static ParameterMatch matchByNameAndType() {
    return (p,j)->(matchByName().apply(p, j) && matchByType().apply(p, j));
  }
  
  public static ParameterMatch matchByNameOrType() {
    return (p,j)->(matchByName().apply(p, j) || matchByType().apply(p, j));
  }
  
  public static ParameterMatch matchByNameTypeFallback() {
    return (p,j)->(matchByNameAndType().apply(p, j) || matchByNameOrType().apply(p, j));
  }
  
  public static ParameterMatch matchNone() {
    return (p,j)->Boolean.FALSE;
  }
  
  public static ParameterMatch matchAll() {
    return (p,j)->Boolean.TRUE;
  }
  
}
