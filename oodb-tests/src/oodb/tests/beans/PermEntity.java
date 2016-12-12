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

import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public interface PermEntity {
  
  public static enum Type {
    OWNER, GROUP, OTHER;
  }
  
  
  public Type getType();
  
  public Set<Permission> getPermissions();
  
  
  public static Set<PermEntity> fromCode(int cd) {
    Set<PermEntity> prs = new HashSet<>();
    int o = cd >> 6;
    int g = (cd >> 3) & 0b111;
    int a = cd & 0b111;
    if(o == 0) {
      throw new IllegalArgumentException("Bad Owner Permission: "
          + o+ " (PermCode: "+ cd+ ")"
      );
    }
    prs.add(new PermOwner().add(Permission.fromCode(o)));
    prs.add(new PermGroup().add(Permission.fromCode(g)));
    prs.add(new PermOther().add(Permission.fromCode(a)));
    return prs;
  }
  
  
  public static Set<PermEntity> fromCode(String code) {
    Set<PermEntity> prs = new HashSet<>();
    if(code.length() != 3) {
      throw new IllegalArgumentException("Bad PermissionEntity Code: "+ code);
    }
    int o = Integer.parseInt(code.substring(0, 1));
    int g = Integer.parseInt(code.substring(1, 2));
    int a = Integer.parseInt(code.substring(2, 3));
    prs.add(new PermOwner().add(Permission.fromCode(o)));
    prs.add(new PermGroup().add(Permission.fromCode(g)));
    prs.add(new PermOther().add(Permission.fromCode(a)));
    return prs;
  }
  
  
  public static int toCode(Set<PermEntity> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    Iterator<PermEntity> it = prs.iterator();
    int o = 0, g = 0, a = 0;
    while(it.hasNext()) {
      PermEntity e = it.next();
      switch(e.getType()) {
        case OWNER:
          o = Permission.toCode(e.getPermissions());
          break;
        case GROUP:
          g = Permission.toCode(e.getPermissions());
          break;
        case OTHER:
          a = Permission.toCode(e.getPermissions());
          break;
      }
    }
    return o << 6 | g << 3 | a;
  }
  
  
  public static String toStringCode(Set<PermEntity> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    Iterator<PermEntity> it = prs.iterator();
    char o = 0, g = 0, a = 0;
    while(it.hasNext()) {
      PermEntity e = it.next();
      switch(e.getType()) {
        case OWNER:
          o = String.valueOf(Permission.toCode(e.getPermissions())).charAt(0);
          break;
        case GROUP:
          g = String.valueOf(Permission.toCode(e.getPermissions())).charAt(0);
          break;
        case OTHER:
          a = String.valueOf(Permission.toCode(e.getPermissions())).charAt(0);
          break;
      }
    }
    return String.valueOf(o) + g + a;
  }
  
  
  public static String toString(Set<PermEntity> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    Iterator<PermEntity> it = prs.iterator();
    String o = "", g = "", a = "";
    while(it.hasNext()) {
      PermEntity e = it.next();
      switch(e.getType()) {
        case OWNER:
          o = Permission.toString(e.getPermissions());
          break;
        case GROUP:
          g = Permission.toString(e.getPermissions());
          break;
        case OTHER:
          a = Permission.toString(e.getPermissions());
          break;
      }
    }
    return o + g + a;
  }
  
  
  public static Set<PermEntity> fromPosix(Set<PosixFilePermission> prs) {
    Set<PermEntity> set = new HashSet<>();
    Iterator<PosixFilePermission> it = prs.iterator();
    while(it.hasNext()) {
      
    }
  }
  
}
