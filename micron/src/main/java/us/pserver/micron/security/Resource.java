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
import us.pserver.micron.security.api.ResourceApi;
import us.pserver.micron.security.api.ResourceBuilderApi;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/01/2019
 */
public class Resource extends AbstractNamedSet implements ResourceApi {

  public Resource(String name, Set<String> roles, Instant created) {
    super(name, roles, created);
  }
  
  public Resource(String name, Set<String> roles) {
    super(name, roles);
  }
  
  @Override
  public ResourceBuilder edit() {
    return (ResourceBuilder) new ResourceBuilder()
        .setCreated(created)
        .addItems(items)
        .setName(name);
  }
  
  
  
  public static ResourceBuilder builder() {
    return new ResourceBuilder();
  }
  
  
  
  
  
  public static class ResourceBuilder extends AbstractNamedSetBuilder implements ResourceBuilderApi {
    
    public ResourceBuilder() {
      super();
    }
    
    @Override
    public ResourceBuilder setName(String name) {
      super.setName(name);
      return this;
    }
    
    @Override
    public ResourceBuilder setCreated(Instant created) {
      super.setCreated(created);
      return this;
    }
    
    @Override
    public ResourceBuilder addItem(String item) {
      super.addItem(item);
      return this;
    }
    
    public ResourceBuilder clearItems() {
      super.clearItems();
      return this;
    }
    
    @Override
    public ResourceBuilderApi edit() {
      return this;
    }
    
    @Override
    public Resource build() {
      return new Resource(name, items, created);
    }
    
  }
  
}
