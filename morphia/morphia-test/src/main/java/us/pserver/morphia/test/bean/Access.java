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

package us.pserver.morphia.test.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Access {

  private final String name;
  
  private final List<Transaction> transactions;
  
  
  private Access() {
    this(null, Collections.EMPTY_LIST);
  }
  
  
  public Access(String name) {
    this(name, new ArrayList<>());
  }
  
  
  public Access(String name, List<Transaction> transactions) {
    if(transactions == null) {
      throw new NullPointerException("Transaction list can not be null");
    }
    this.name = name != null ? name : "";
    this.transactions = notNull("Transaction list", transactions);
  }
  
  
  public List<Transaction> transactions() {
    return transactions;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  private <T> T notNull(String name, T obj) {
    if(obj == null) {
      throw new NullPointerException(name + " can not be null");
    }
    return obj;
  }
  
  
  public boolean tryGrant(Access acs) {
    return this.name.equals(notNull("Access", acs).name) 
        && acs.transactions.stream().allMatch(
            t->transactions.stream().anyMatch(tr->tr == t));
  }
  
  
  public boolean tryGrant(String name, List<Transaction> trs) {
    return tryGrant(new Access(name,
        notNull("Transaction array", trs))
    );
  }
  
  
  public boolean tryGrant(List<Transaction> trs) {
    return tryGrant(new Access(null,
        notNull("Transaction array", trs))
    );
  }
  
  
  public void grant(String name, List<Transaction> trs) throws IllegalAccessException {
    if(!tryGrant(name, trs)) {
      throw new IllegalAccessException();
    }
  }
  
  
  public void grant(List<Transaction> trs) throws IllegalAccessException {
    if(!tryGrant(trs)) {
      throw new IllegalAccessException();
    }
  }
  
  
  public void grant(Access acs) throws IllegalAccessException {
    if(!tryGrant(acs)) {
      throw new IllegalAccessException();
    }
  }
  
}
