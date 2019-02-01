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

import java.util.List;
import java.util.Optional;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import us.pserver.micron.security.impl.SecurityImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public interface Security {
  
  public Ignite ignite();
  
  public IgniteCache<String,User> getUserCache();
  
  public IgniteCache<String,Group> getGroupCache();
  
  public IgniteCache<String,Role> getRoleCache();
  
  public IgniteCache<String,Resource> getResourceCache();
  
  public List<User> getUserByEmail(String email);
  
  public Optional<User> authenticateUser(String name, char[] password);
  
  public boolean authorize(String resource, User user);
  
  public boolean authorize(Resource res, User user);
  
  
  
  public static Security create(Ignite ignite) {
    return new SecurityImpl(ignite);
  }
  
}