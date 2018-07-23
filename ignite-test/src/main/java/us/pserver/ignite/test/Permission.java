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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public enum Permission {

  NOPERM(0), EXEC(1), WRITE(2), READ(4);
  
  private Permission(int code) {
    this.code = code;
  }
  
  public int getCode() {
    return this.code;
  }
  
  private final int code;
  
  
  
  public static Set<Permission> fromPosixBin(int cd) {
    Set<Permission> prs = new HashSet<>();
    switch(cd) {
      case 0:
        prs.add(NOPERM);
        break;
      case 1: 
        prs.add(EXEC);
        break;
      case 2:
        prs.add(WRITE);
        break;
      case 3:
        prs.add(EXEC);
        prs.add(WRITE);
        break;
      case 4:
        prs.add(READ);
        break;
      case 5:
        prs.add(EXEC);
        prs.add(READ);
        break;
      case 6:
        prs.add(WRITE);
        prs.add(READ);
        break;
      case 7:
        prs.add(EXEC);
        prs.add(WRITE);
        prs.add(READ);
        break;
      default:
        throw new IllegalArgumentException("Bad Permission Code: "+ cd);
    }
    return prs;
  }
  
  
  public static int toPosixBin(Set<Permission> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    return prs.stream()
        .map(Permission::getCode)
        .reduce(0, Integer::sum);
  }
  
  
  public static Set<Permission> fromPosixString(String str) {
    if(str == null || str.length() != 3) {
      throw new IllegalArgumentException("Bad Posix String: "+ str);
    }
    Set<Permission> prs = new HashSet<>();
    switch(str) {
      case "--x":
        prs.add(EXEC);
        break;
      case "-w-":
        prs.add(WRITE);
        break;
      case "-wx":
        prs.add(WRITE);
        prs.add(EXEC);
        break;
      case "r--":
        prs.add(READ);
        break;
      case "r-x":
        prs.add(READ);
        prs.add(EXEC);
        break;
      case "rw-":
        prs.add(READ);
        prs.add(WRITE);
        break;
      case "rwx":
        prs.add(READ);
        prs.add(WRITE);
        prs.add(EXEC);
        break;
      default:
        prs.add(NOPERM);
        break;
    }
    return prs;
  }
  
  
  public static String toPosixString(Set<Permission> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    int code = Permission.toPosixBin(prs);
    switch(code) {
      case 1:
        return "--x";
      case 2:
        return "-w-";
      case 3:
        return "-wx";
      case 4:
        return "r--";
      case 5:
        return "r-x";
      case 6:
        return "rw-";
      case 7:
        return "rwx";
      default:
        return "---";
    }
  }
  
}
