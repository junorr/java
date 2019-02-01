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

package us.pserver.micron.security;

import java.time.LocalDate;
import us.pserver.micron.security.impl.UserBuilderImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public interface User extends NamedBean {

  public String getFullName();
  
  public String getEmail();
  
  public LocalDate getBirth();
  
  public String getHash();
  
  public boolean authenticate(String name, String hash);
  
  public boolean authenticate(String name, char[] password);
  
  public boolean authenticate(User user);
  
  @Override
  public UserBuilder edit();
  
  
  
  public static UserBuilder builder() {
    return new UserBuilderImpl();
  }
  
  
  
  
  
  
  public interface UserBuilder extends NamedBean.NamedBeanBuilder<User, UserBuilder> {

    public String getFullName();

    public UserBuilder setFullName(String fullName);

    public String getEmail();

    public UserBuilder setEmail(String email);

    public String getHash();

    public UserBuilder setHash(String hash);

    public UserBuilder setPassword(char[] password);

    public LocalDate getBirth();

    public UserBuilder setBirth(LocalDate birth);

  }
  
}
