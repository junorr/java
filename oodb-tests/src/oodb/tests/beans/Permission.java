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

import java.util.HashSet;
import java.util.Iterator;
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
  
  
  public static Set<Permission> fromCode(int cd) {
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
  
  
  public static int toCode(Set<Permission> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    int code = 0;
    Iterator<Permission> it = prs.iterator();
    while(it.hasNext()) {
      code += it.next().getCode();
    }
    return code;
  }
  
  
  public static String toString(Set<Permission> prs) {
    Objects.requireNonNull(prs, "Bad Null Permission Set");
    int code = Permission.toCode(prs);
    String str = "---";
    switch(code) {
      case 0:
        str = "---";
        break;
      case 1: 
        str = "--x";
        break;
      case 2:
        str = "-w-";
        break;
      case 3:
        str = "-wx";
        break;
      case 4:
        str = "r--";
        break;
      case 5:
        str = "r-x";
        break;
      case 6:
        str = "rw-";
        break;
      case 7:
        str = "rwx";
        break;
    }
    return str;
  }
    
}
