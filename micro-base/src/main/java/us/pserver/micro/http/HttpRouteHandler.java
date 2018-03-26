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
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/03/2018
 */
public class HttpRouteHandler {

  private final HttpRoute route;
  
  private final HttpHandler handler;
  
  
  public HttpRouteHandler(HttpRoute route, HttpHandler handler) {
    this.route = Match.notNull(route).getOrFail("Bad null HttpRoute");
    this.handler = Match.notNull(handler).getOrFail("Bad null HttpHandler");
  }


  public HttpRoute getHttpRoute() {
    return route;
  }


  public HttpHandler getHttpHandler() {
    return handler;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.route);
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
    final HttpRouteHandler other = (HttpRouteHandler) obj;
    if (!Objects.equals(this.route, other.route)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "HttpRouteHandler{" + "route=" + route + ", handler=" + handler + '}';
  }
  
}
