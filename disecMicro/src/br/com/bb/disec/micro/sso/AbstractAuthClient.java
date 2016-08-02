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

package br.com.bb.disec.micro.sso;

import br.com.bb.sso.bean.User;
import com.google.gson.Gson;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public abstract class AbstractAuthClient {
  
  public static final String DEFAUTL_ADDRESS = "http://127.0.0.1";
  
  public static final int DEFAULT_PORT = 9088;
  
  public static final String DEFAULT_CONTEXT = "/auth";
  

  protected final String address;
  
  protected final int port;
  
  protected String jsonUser;
  
  protected StatusLine status;
  
  
  protected AbstractAuthClient(String address, int port) {
    if(address == null || address.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Address: "+ address);
    }
    if(port <= 0 || port > 65535) {
      throw new IllegalArgumentException("Bad Port: "+ port);
    }
    this.address = address;
    this.port = port;
  }
  
  
  public String getUriString() {
    return new StringBuilder()
        .append(address)
        .append(":")
        .append(port)
        .append(DEFAULT_CONTEXT)
        .toString();
  }
  
  
  public int getResponseCode() {
    return (status != null ? status.getStatusCode() : 0);
  }
  
  
  public String getResponsePhrase() {
    return (status != null ? status.getReasonPhrase() : null);
  }
  
  
  public User getUser() {
    if(jsonUser == null || jsonUser.length() < 10) {
      throw new IllegalArgumentException("Bad Response. Can not create User");
    }
    return new Gson().fromJson(jsonUser, User.class);
  }
  
  
  public abstract AbstractAuthClient doAuth() throws IOException;
  
  
  public boolean isAuthenticated() {
    return status != null 
        && status.getStatusCode() == HttpStatus.SC_OK;
  }
  
  
  public boolean authenticate() throws IOException {
    return doAuth().isAuthenticated();
  }
  
}
