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

import java.util.List;
import static us.pserver.chk.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/07/2014
 */
public class Authenticator {

  private CredentialsSource source;
  
  
  public Authenticator(CredentialsSource cs) {
    nullarg(CredentialsSource.class, cs);
    source = cs;
  }
  
  
  public boolean authenticate(Credentials cred) throws AuthenticationException {
    if(cred == null || cred.getUser() == null)
      throw new AuthenticationException(
          "Invalid Credentials ["+ cred+ "]");
    
    List<Credentials> cs = source.getCredentials();
    if(cs.isEmpty())
      throw new AuthenticationException("Empty CredentialsSource");
    
    for(Credentials c : cs) {
      if(c != null && c.authenticate(cred))
        return true;
    }
    throw new AuthenticationException("Authentication Failed");
  }
  
}
