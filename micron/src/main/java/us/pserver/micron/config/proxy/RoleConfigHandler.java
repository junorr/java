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

package us.pserver.micron.config.proxy;

import io.helidon.config.Config;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Role;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public class RoleConfigHandler implements InvocationHandler, Role {
  
  private final Config cfg;
  
  public RoleConfigHandler(Config cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null Config");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    switch(method.getName()) {
      case "getName":
        return getName();
      case "getItems":
      case "getGroups":
        return getItems();
      case "containsGroup":
        return containsGroup((Group) args[0]);
      case "hashCode":
        return hashCode();
      case "equals":
        return equals(args[0]);
      case "toString":
        return toString();
      default:
        throw new UnsupportedOperationException("Unknown method: " + getMethodString(method, args));
    }
  }
    
    
  private String getMethodString(Method meth, Object[] args) {
    StringBuilder msg = new StringBuilder(meth.getName())
        .append("( ");
    if(args == null || args.length < 1) {
      return msg.append(")").toString();
    }
    for(int i = 0; i < args.length; i++) {
      msg.append(args[i].getClass().getSimpleName()).append(", ");
    }
    return msg.delete(msg.length() - 2, msg.length()).append(" )").toString();
  }
  
  
  @Override
  public String getName() {
    return cfg.get("name").asString().get();
  }


  @Override
  public Instant getCreated() {
    return cfg.get("created").as(Instant.class).orElse(Instant.now());
  }
  
  
  @Override
  public Set<String> getItems() {
    return new HashSet<>(cfg.get("groups").asList(String.class).orElse(Collections.EMPTY_LIST));
  }
  
  
  @Override
  public boolean isAllowed() {
    return cfg.get("allowed").asBoolean().orElse(Boolean.FALSE);
  }
  
  
  @Override
  public boolean containsGroup(Group group) {
    return getItems().contains(group.getName());
  }
  

  @Override
  public Role.RoleBuilder edit() {
    return Role.builder()
        .setName(getName())
        .addItems(getItems())
        .setAllowed(isAllowed())
        .setCreated(getCreated());
  }
  
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.getName());
    hash = 47 * hash + Objects.hashCode(this.getItems());
    hash = 47 * hash + Objects.hashCode(this.isAllowed());
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
    if (!Role.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Role other = (Role) obj;
    if (!Objects.equals(this.getName(), other.getName())) {
      return false;
    }
    if (!Objects.equals(this.isAllowed(), other.isAllowed())) {
      return false;
    }
    return Objects.equals(this.getItems(), other.getItems());
  }
  
  @Override
  public String toString() {
    return "Role{ name=" + getName() + ", allowed=" + isAllowed() + ", items=" + getItems() + ", created=" + getCreated() + " }";
  }
  
}
