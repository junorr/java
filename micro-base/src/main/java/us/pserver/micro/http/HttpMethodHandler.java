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

import io.undertow.server.HttpHandler;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/03/2018
 */
public class HttpMethodHandler {

  private final List<HttpMethod> methods;
  
  private final HttpHandler handler;
  
  
  private HttpMethodHandler() {
    methods = null;
    handler = null;
  }
  
  
  public HttpMethodHandler(List<HttpMethod> meth, HttpHandler hand) {
    this.methods = Match.notNull(meth).getOrFail("Bad null HttpMethod list");
    this.handler = Match.notNull(hand).getOrFail("Bad null HttpHandler");
  }


  public List<HttpMethod> getHttpMethods() {
    return methods;
  }


  public HttpHandler getHttpHandler() {
    return handler;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.methods);
    hash = 97 * hash + Objects.hashCode(this.handler);
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
    final HttpMethodHandler other = (HttpMethodHandler) obj;
    if (!Objects.equals(this.methods, other.methods)) {
      return false;
    }
    if (!Objects.equals(this.handler, other.handler)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "HttpMethodHandler{" + "methods=" + methods + ", handler=" + handler + '}';
  }
  
}
