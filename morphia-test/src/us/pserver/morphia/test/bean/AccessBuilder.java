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

import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class AccessBuilder {

  private final UserBuilder builder;
  
  private final String name;
  
  private final Transaction perm;
  
  private final boolean auth;
  

  private AccessBuilder(UserBuilder builder, String name, Transaction perm, boolean auth) {
    this.builder = builder;
    this.name = name;
    this.perm = perm;
    this.auth = auth;
  }
  
  
  public AccessBuilder(UserBuilder builder) {
    this.builder = NotNull.of(builder).getOrFail("UserBuilder");
    this.name = null;
    this.perm = null;
    this.auth = false;
  }
  
  
  public AccessBuilder withName(String name) {
    return new AccessBuilder(builder, name, perm, auth);
  }
  
  
  public AccessBuilder authorize(Transaction trc) {
    return new AccessBuilder(builder, name, trc, true);
  }
  
  
  public AccessBuilder deny(Transaction trc) {
    return new AccessBuilder(builder, name, trc, false);
  }
  
  
  public UserBuilder insert() {
    return builder.addAccess(new Access(name, perm, auth));
  }
  
}
