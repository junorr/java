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

package us.pserver.jpx;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/08/2018
 */
public class ProxyAuthorization {

  private final String username;
  
  private final byte[] password;
  
  
  public ProxyAuthorization(String username, byte[] password) {
    if(username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException(String.format("Bad username: '%s'", username));
    }
    if(password == null || password.length < 1) {
      throw new IllegalArgumentException(String.format("Bad password: %s", username));
    }
    this.username = username;
    this.password = password;
  }
  
  
  public String getProxyAuthorization() {
    byte[] userbytes = username.concat(":").getBytes(StandardCharsets.UTF_8);
    byte[] authbytes = new byte[userbytes.length + password.length];
    System.arraycopy(userbytes, 0, authbytes, 0, userbytes.length);
    System.arraycopy(password, 0, authbytes, userbytes.length, password.length);
    return "Basic " + Base64.getEncoder().encodeToString(authbytes);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.username);
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
    final ProxyAuthorization other = (ProxyAuthorization) obj;
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "ProxyAuthorization{" + "username=" + username + '}';
  }
  
  
  
  public static ProxyAuthorization of(String username, String password) {
    return new ProxyAuthorization(username, password.getBytes(StandardCharsets.UTF_8));
  }
  
}
