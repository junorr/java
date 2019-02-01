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

import us.pserver.micron.security.Role;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public class RoleBuilderImpl extends AbstractNamedSetBuilder<Role,Role.RoleBuilder> implements Role.RoleBuilder {
  
  private boolean allowed;
  
  public RoleBuilderImpl() {
    super();
    allowed = false;
  }
  
  @Override
  public boolean isAllowed() {
    return allowed;
  }
  
  @Override
  public Role.RoleBuilder setAllowed(boolean allowed) {
    this.allowed = allowed;
    return this;
  }
  
  @Override
  public Role build() {
    return new RoleImpl(name, allowed, items, created);
  }
  
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    hash = 13 * hash + (this.allowed ? 1 : 0);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    return super.equals(obj) 
        && Role.RoleBuilder.class.isAssignableFrom(obj.getClass()) 
        && this.isAllowed() && ((Role.RoleBuilder) obj).isAllowed();
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    return sb.delete(sb.length() -1, sb.length())
        .append(", allowed=")
        .append(allowed)
        .append("}")
        .toString();
  }
  
  
  
}
