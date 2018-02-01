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

import java.time.Instant;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2018
 */
public class QueryEntry {

  private final Instant timestamp;
  
  private final String query;
  
  
  public QueryEntry(Instant inst, String query) {
    if(inst == null) {
      throw new IllegalArgumentException("Bad null Instant");
    }
    if(query == null) {
      throw new IllegalArgumentException("Bad null query String");
    }
    this.timestamp = inst;
    this.query = query;
  }
  
  
  public QueryEntry(String query) {
    this(Instant.now(), query);
  }
  
  
  public Instant getTimestamp() {
    return timestamp;
  }
  
  
  public String getQuery() {
    return query;
  }


  @Override
  public String toString() {
    return "QueryEntry{" + "timestamp=" + timestamp + ", query=" + query + '}';
  }
  
}
