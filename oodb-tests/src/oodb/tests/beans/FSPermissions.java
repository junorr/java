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

package oodb.tests.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class FSPermissions implements IFSPermissions {

  private final List<PermEntity> perms;
  
  
  public FSPermissions(Set<PermEntity> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    perms = new ArrayList<>(prs);
  }


  @Override
  public Set<Permission> owner() {
    return perms.stream()
        .filter(pe->pe.getType() == PermEntity.Type.OWNER)
        .findFirst()
        .orElse(new PermOwner())
        .getPermissions();
  }


  @Override
  public Set<Permission> group() {
    return perms.stream()
        .filter(pe->pe.getType() == PermEntity.Type.GROUP)
        .findFirst()
        .orElse(new PermOwner())
        .getPermissions();
  }


  @Override
  public Set<Permission> others() {
    return perms.stream()
        .filter(pe->pe.getType() == PermEntity.Type.OTHER)
        .findFirst()
        .orElse(new PermOwner())
        .getPermissions();
  }


  @Override
  public List<PermEntity> permissions() {
    return perms;
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    perms.forEach(p->sb.append(p).append("\n"));
    return sb.toString();
  }
  
}
