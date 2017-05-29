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

package br.com.bb.disec.micro.db;

import java.io.IOException;

/**
 * Interface com padronização de métodos para manipulação de queries SQL.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public interface SqlSource {

  /**
   * Busca uma query SQL a partir do grupo que ela está inserido e o nome atribuído
   * a query.
   * @param group Grupo da query
   * @param name Nome da query
   * @return query
   * @throws IOException 
   */
  public String getSql(String group, String name) throws IOException;
  
  /**
   * Verifica se uma query SQL existe a partir do grupo que ela está inserido e
   * o nome atribuído a query.
   * @param group Grupo da query
   * @param name Nome da query
   * @return true | false Caso exista | Caso contrário
   * @throws IOException 
   */
  public boolean containsSql(String group, String name) throws IOException;
  
}
