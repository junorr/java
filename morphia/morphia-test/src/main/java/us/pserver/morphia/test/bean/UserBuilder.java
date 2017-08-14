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
import java.util.List;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class UserBuilder {

  private String name;
  
  private byte[] password;
  
  private List<Access> access;
  
  private Access temp;
  
  
  public UserBuilder() {
    access = new ArrayList<>();
  }
  
  
  public UserBuilder withName(String name) {
    this.name = name;
    return this;
  }
  
  
  public UserBuilder withPassword(String pass) {
    this.password = new UTF8String(pass).getBytes();
    return this;
  }
  
  
  public UserBuilder addAccess(Access acs) {
    this.access.add(NotNull.of(acs).getOrFail("Bad null Access"));
    return this;
  }
  
  
  public UserBuilder createAccess(String name) {
    this.temp = new Access(name);
    return this;
  }
  
  
  public UserBuilder addTransaction(Transaction t) {
    if(this.temp == null) createAccess(null);
    this.temp.transactions().add(t);
    return this;
  }
  
  
  public UserBuilder addAccess() {
    this.access.add(NotNull.of(temp).getOrFail("Bad null Access"));
    return this;
  }
  
  
  public User create() {
    MessageDigest
  }
  
}
