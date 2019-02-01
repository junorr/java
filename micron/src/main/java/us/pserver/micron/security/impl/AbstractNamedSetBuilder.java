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

package us.pserver.micron.security.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import us.pserver.micron.security.NamedSet;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public abstract class AbstractNamedSetBuilder<T extends NamedSet, B extends NamedSet.NamedSetBuilder<T,B>> 
  implements NamedSet.NamedSetBuilder<T,B> {

  protected String name;

  protected Set<String> items;

  protected Instant created;

  public AbstractNamedSetBuilder() {
    this.name = null;
    this.created = Instant.now();
    this.items = new HashSet<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public B setName(String name) {
    this.name = name;
    return (B) this;
  }


  @Override
  public Instant getCreated() {
    return created;
  }

  @Override
  public B setCreated(Instant created) {
    this.created = created;
    return (B) this;
  }
  
  @Override
  public Set<String> getItems() {
    return items;
  }

  @Override
  public B addItem(String item) {
    this.items.add(Match.notEmpty(item).getOrFail("Bad null/empty item"));
    return (B) this;
  }

  @Override
  public B clearItems() {
    this.items.clear();
    return (B) this;
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.items);
    hash = 67 * hash + Objects.hashCode(this.created);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final AbstractNamedSetBuilder<?, ?> other = (AbstractNamedSetBuilder<?, ?>) obj;
    if(!Objects.equals(this.name, other.name)) {
      return false;
    }
    if(!Objects.equals(this.items, other.items)) {
      return false;
    }
    return Objects.equals(this.created, other.created);
  }
  
  @Override
  public String toString() {
    return name + "Builder{" + "name=" + name + ", items=" + items + ", created=" + created + '}';
  }

}
