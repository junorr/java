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

package br.com.bb.disec.micro.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public class SqlHandler implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    if(Methods.GET.equals(hse.getRequestMethod())) {
      new GetSqlHandler().handleRequest(hse);
    }
    else if(Methods.POST.equals(hse.getRequestMethod())) {
      new PostSqlHandler().handleRequest(hse);
    }
    else if(Methods.OPTIONS.equals(hse.getRequestMethod())) {
      System.out.println("* Access-Control-Request-Method : "+ hse.getRequestHeaders().getFirst("Access-Control-Request-Method"));
      System.out.println("* Access-Control-Request-Headers: "+ hse.getRequestHeaders().getFirst("Access-Control-Request-Headers"));
      System.out.println("* Origin: "+ hse.getRequestHeaders().getFirst("origin"));
      hse.getResponseHeaders().put(
          new HttpString("Access-Control-Allow-Origin"), hse.getRequestHeaders().getFirst("origin")
      );
      hse.getResponseHeaders().put(
          new HttpString("Access-Control-Allow-Credentials"), "true"
      );
      hse.getResponseHeaders().put(
          new HttpString("Access-Control-Allow-Method"), hse.getRequestHeaders().getFirst("Access-Control-Request-Method")
      );
      hse.getResponseHeaders().put(
          new HttpString("Access-Control-Allow-Headers"), hse.getRequestHeaders().getFirst("Access-Control-Request-Headers")
      );
      hse.setStatusCode(200);
      hse.endExchange();
    }
    else {
      hse.setStatusCode(400).setReasonPhrase("Bad Request");
      hse.endExchange();
    }
  }
  
}
