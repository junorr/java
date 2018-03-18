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

package us.pserver.micro.http;

import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/03/2018
 */
public class HttpRoute {

  private final HttpMethod method;
  
  private final String uri;
  
  
  public HttpRoute(HttpMethod meth, String uri) {
    this.method = Match.notNull(meth).getOrFail("Bad null HttpMethod");
    this.uri = Match.notNull(uri).getOrFail("Bad null uri String");
  }
  
  
  public HttpMethod getHttpMethod() {
    return method;
  }
  
  
  public String getURI() {
    return uri;
  }
  

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.method);
    hash = 13 * hash + Objects.hashCode(this.uri);
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
    final HttpRoute other = (HttpRoute) obj;
    if (!Objects.equals(this.uri, other.uri)) {
      return false;
    }
    if (!Objects.equals(this.method, other.method)) {
      return false;
    }
    return true;
  }
  
  
  @Override
  public String toString() {
    return method.toString() 
        + (uri.startsWith("/") ? " " : " /") 
        + uri;
  }
  
}
