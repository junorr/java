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

package us.pserver.rob.container;

import java.util.Arrays;
import java.util.Objects;
import us.pserver.cdr.StringByteConverter;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.nullstr;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/07/2014
 */
public class Credentials {

  private String user;
  
  private byte[] pswd;
  
  
  public Credentials() {
    user = null;
    pswd = null;
  }
  
  
  public Credentials(String user, byte[] pswd) {
    nullstr(user);
    nullarray(pswd);
    this.user = user;
    this.pswd = pswd;
  }
  
  
  public Credentials(String user, StringBuffer pwd) {
    nullstr(user);
    this.user = user;
    setPassword(pwd);
  }
  
  
  public Credentials setUser(String u) {
    if(u != null) user = u;
    return this;
  }
  
  
  public Credentials setPassword(byte[] p) {
    if(p != null && p.length > 0)
      pswd = p;
    return this;
  }
  
  
  public Credentials setPassword(StringBuffer pwd) {
    nullarg(StringBuffer.class, pwd);
    nullstr(pwd.toString());
    StringByteConverter cv = new StringByteConverter();
    pswd = cv.convert(pwd.toString());
    for(int i = 0; i < pwd.length(); i++) {
      pwd.deleteCharAt(i);
    }
    pwd = null;
    return this;
  }
  
  
  public String getUser() {
    return user;
  }
  
  
  public boolean authenticate(Credentials c) {
    return c != null && c.getUser() != null
        && c.user.equals(user)
        && equals(pswd, c.pswd);
  }
  
  
  public static boolean equals(byte[] c1, byte[] c2) {
    if(c1 == null || c2 == null)
      return false;
    if(c1.length != c2.length)
      return false;
    boolean eq = true;
    for(int i = 0; i < c1.length; i++) {
      eq = eq && c1[i] == c2[i];
    }
    return eq;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.user);
    hash = 79 * hash + Arrays.hashCode(this.pswd);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Credentials other = (Credentials) obj;
    if (!Objects.equals(this.user, other.user)) {
      return false;
    }
    if (!equals(this.pswd, other.pswd)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "Credentials{ user = " + user + " }";
  }
  
}
