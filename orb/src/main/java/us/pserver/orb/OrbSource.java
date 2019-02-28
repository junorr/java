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

package us.pserver.orb;

import us.pserver.orb.bind.MethodBind;
import us.pserver.orb.ds.DataSource;
import us.pserver.orb.impl.OrbSourceBuilder;
import us.pserver.orb.parse.OrbParser;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public interface OrbSource<T> extends Comparable<OrbSource> {

  public DataSource<T> dataSource();
  
  public OrbParser<T> parser();
  
  public MethodBind methodBind();
  
  public int priority();
  
  @Override
  public default int compareTo(OrbSource o) {
    return Integer.compare(priority(), o.priority());
  }
  
  
  
  public static OrbSourceBuilder builder() {
    return new OrbSourceBuilder();
  }
  
}
