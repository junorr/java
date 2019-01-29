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

import java.time.Instant;
import java.util.Set;
import us.pserver.micron.security.api.GroupApi;
import us.pserver.micron.security.api.GroupBuilderApi;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class Group extends AbstractNamedSet implements GroupApi {

  public Group(String name, Set<String> members, Instant created) {
    super(name, members, created);
  }
  
  public Group(String name, Set<String> members) {
    super(name, members);
  }
  
  @Override
  public GroupBuilder edit() {
    return (GroupBuilder) new GroupBuilder()
        .setCreated(created)
        .addItems(items)
        .setName(name);
  }
  
  
  
  public static GroupBuilder builder() {
    return new GroupBuilder();
  }
  
  
  
  
  
  public static class GroupBuilder extends AbstractNamedSetBuilder implements GroupBuilderApi {
    
    public GroupBuilder() {
      super();
    }
    
    @Override
    public GroupBuilder setName(String name) {
      super.setName(name);
      return this;
    }
    
    @Override
    public GroupBuilder setCreated(Instant created) {
      super.setCreated(created);
      return this;
    }
    
    @Override
    public GroupBuilder addItem(String item) {
      super.addItem(item);
      return this;
    }
    
    @Override
    public GroupBuilder clearItems() {
      super.clearItems();
      return this;
    }
    
    @Override
    public GroupBuilderApi edit() {
      return this;
    }
    
    @Override
    public GroupApi build() {
      return new Group(name, items, created);
    }
    
  }
  
}
