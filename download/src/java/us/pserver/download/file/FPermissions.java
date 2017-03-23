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

package us.pserver.download.file;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class FPermissions implements IFPermissions {

  private final Set<FPermission> owner;
  
  private final Set<FPermission> group;
  
  private final Set<FPermission> others;
  
  
  public FPermissions(Set<FPermission> owner, Set<FPermission> group, Set<FPermission> others) {
    Objects.requireNonNull(owner, "Bad Null Owner Permissions");
    Objects.requireNonNull(owner, "Bad Null Group Permissions");
    Objects.requireNonNull(owner, "Bad Null Others Permissions");
    this.owner = owner;
    this.group = group;
    this.others = others;
  }
  
  
  public FPermissions(Set<FPermission> owner, Set<FPermission> group) {
    Objects.requireNonNull(owner, "Bad Null Owner Permissions");
    Objects.requireNonNull(owner, "Bad Null Group Permissions");
    this.owner = owner;
    this.group = group;
    this.others = Collections.EMPTY_SET;
  }
  
  
  public FPermissions(Set<FPermission> owner) {
    Objects.requireNonNull(owner, "Bad Null Owner Permissions");
    this.owner = owner;
    this.group = Collections.EMPTY_SET;
    this.others = Collections.EMPTY_SET;
  }
  

  @Override
  public Set<FPermission> owner() {
    return owner;
  }


  @Override
  public Set<FPermission> group() {
    return group;
  }


  @Override
  public Set<FPermission> others() {
    return others;
  }

  
  @Override
  public int toPosixBin() {
    return FPermission.toPosixBin(owner) << 6 
        | FPermission.toPosixBin(group) << 3 
        | FPermission.toPosixBin(others);
  }
  
  
  @Override
  public String toPosixCode() {
    return String.valueOf(FPermission.toPosixBin(owner))
        .concat(String.valueOf(FPermission.toPosixBin(group)))
        .concat(String.valueOf(FPermission.toPosixBin(others)));
  }
  
  
  @Override
  public String toPosixString() {
    return FPermission.toPosixString(owner) 
        + FPermission.toPosixString(group)
        + FPermission.toPosixString(others);
  }
  

  @Override
  public String toString() {
    return toPosixString();
  }
  
}
