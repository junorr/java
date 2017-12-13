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

package us.pserver.finalson.strategy;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class ParamNameMatchStrategy implements Predicate<MethodHandleInfo> {

  private final List<String> names;
  
  public ParamNameMatchStrategy(List<String> names) {
    this.names = NotNull.of(names).getOrFail("Bad null names List");
  }
  
  @Override
  public boolean test(MethodHandleInfo mhi) {
    Stream<String> snames = (names.size() > 2 
        ? names.parallelStream() : names.stream())
        .sorted();
    Stream<String> spars = (mhi.getParameters().size() > 2 
        ? mhi.getParameters().parallelStream() : mhi.getParameters().stream())
        .map(Parameter::getName).sorted();
    return spars.allMatch(p->snames.anyMatch(n->p.equals(n)));
  }
  
}
