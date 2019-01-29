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

package us.pserver.micron.security.api;

import java.time.Instant;
import java.util.Collection;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public interface ResourceBuilderApi extends NamedSetBuilder<ResourceApi>, ResourceApi {

  @Override
  public ResourceBuilderApi setName(String name);
  
  @Override
  public ResourceBuilderApi setCreated(Instant created);
  
  @Override
  public ResourceBuilderApi addItem(String item);
  
  @Override
  public ResourceBuilderApi clearItems();
  
  @Override
  public default ResourceBuilderApi addItems(Collection<String> items) {
    items.forEach(this::addItem);
    return this;
  }
  
  public default ResourceBuilderApi addRoles(Collection<RoleApi> roles) {
    roles.forEach(this::addRole);
    return this;
  }
  
  public default ResourceBuilderApi addRole(RoleApi role) {
    addItem(role.getName());
    return this;
  }
  
  public default ResourceBuilderApi clearRoles() {
    return clearItems();
  }
  
}
