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

package us.pserver.dbone.index;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2017
 */
public class AcessorIndexBuilder<T, V extends Comparable<V>> implements IndexBuilder<T,V> {
  
  private final String name;

  private final Function<T, V> acessor;

  public AcessorIndexBuilder(String name, Function<T, V> acessor) {
    this.name = NotNull.of(name).getOrFail("Bad null name");
    this.acessor = NotNull.of(acessor).getOrFail("Bad null acessor Function");
  }

  @Override
  public List<Index<V>> build(T object, Record rec) {
    return Arrays.asList(Index.of(name, acessor.apply(object), rec));
  }

  @Override
  public String name() {
    return name;
  }

}
