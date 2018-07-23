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

package us.pserver.ignite.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class RuledResource {

  private String name;
  
  private List<AccessRule> rules;


  public RuledResource(String name) {
    this.name = name;
    this.rules = new ArrayList<>();
  }
  
  public RuledResource(String name, List<AccessRule> rules) {
    this(name);
    this.rules.addAll(rules);
  }
  
  public RuledResource(String name, AccessRule ... rules) {
    this(name, Arrays.asList(rules));
  }
  
  public String getName() {
    return name;
  }
  
  public RuledResource setName(String name) {
    this.name = name;
    return this;
  }
  
  public List<AccessRule> getRules() {
    return rules;
  }
  
  public RuledResource setRules(List<AccessRule> rules) {
    this.rules = rules;
    return this;
  }
  
  public boolean hasPermission(User usr, Permission prm) {
    return rules.stream().anyMatch(r->r.hasPermission(usr, prm));
  }
  
  public boolean hasUserPermission(String username, Permission prm) {
    return rules.stream().anyMatch(r->r.hasPermission(username, prm));
  }
  
  public boolean hasPermission(Group grp, Permission prm) {
    return hasGroupPermission(grp.getName(), prm);
  }
  
  public boolean hasGroupPermission(String groupname, Permission prm) {
    Optional<AccessRule> opt = rules.stream()
        .filter(r->r.getGroup().getName().equals(groupname))
        .findAny();
    return opt.isPresent() 
        && opt.get().getEnumPerms().contains(prm);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + Objects.hashCode(this.name);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RuledResource other = (RuledResource) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "Resource{" + "name=" + name + ", rules=" + rules + '}';
  }
  
}
