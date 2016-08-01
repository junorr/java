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

package br.com.bb.disec.micro.cache;

import br.com.bb.sso.bean.User;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class UserCache {

  public static final String NAME_AUTH_USERS = "authenticated-users";
  
  public static final String USU_SIM_CHAVE = "usu-sim-chave";
  
  public static final UserCache INSTANCE = new UserCache();

  
  private final IMap<String,User> users;
  
  
  public UserCache() {
    users = PublicCache.getHazelcastInstance().getMap(NAME_AUTH_USERS);
  }
  
  
  public static IMap<String,User> getUsers() {
    return INSTANCE.users;
  }
  
  
  public static User get(String key) {
    return INSTANCE.users.get(key);
  }
  
  
  public static boolean contains(String key) {
    return INSTANCE.users.containsKey(key);
  }
  
}
