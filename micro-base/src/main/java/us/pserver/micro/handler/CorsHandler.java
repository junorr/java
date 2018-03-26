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

package us.pserver.micro.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 * Um handler que pode ser usado para fazer o tratamento dos headers da requisição
 * e resposta. Neste objeto também está encapsulado a opção de encadeamento de handlers.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2016
 */
public class CorsHandler implements HttpHandler {
  
  public static final String AC_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  
  public static final String AC_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
  
  public static final String AC_ALLOW_METHOD = "Access-Control-Allow-Methods";
  
  public static final String AC_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  
  public static final String AC_REQUEST_METHOD = "Access-Control-Request-Method";
  
  public static final String AC_REQUEST_HEADERS = "Access-Control-Request-Headers";
  
  public static final String AC_MAX_AGE = "Access-Control-Max-Age";
  
  public static final String HEADER_ORIGIN = "origin";
  
  
  private final HttpHandler next;
  
  /**
   * Construtor padrão da classe.
   * @param next Próximo handler a ser chamado no caso de encadeamento de handlers
   */
  public CorsHandler(HttpHandler next) {
    this.next = next;
  }
  
  /**
   * Trata o header da resposta a partir do header da requisição. Verifica o conteúdo
   * do header da requisição e define qual será o conteúdo do header da resposta.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    HeaderMap hds = hse.getRequestHeaders();
    if(hds.contains(HEADER_ORIGIN)) {
      hse.getResponseHeaders().put(
          new HttpString(AC_ALLOW_ORIGIN), hds.getFirst(HEADER_ORIGIN)
      );
    }
    hse.getResponseHeaders().put(
        new HttpString(AC_ALLOW_CREDENTIALS), Boolean.TRUE.toString()
    );
    if(hds.contains(AC_REQUEST_METHOD)) {
      hse.getResponseHeaders().put(
          new HttpString(AC_ALLOW_METHOD), 
          hds.getFirst(AC_REQUEST_METHOD)
      );
    }
    if(hds.contains(AC_REQUEST_HEADERS)) {
      hse.getResponseHeaders().put(
          new HttpString(AC_ALLOW_HEADERS), 
          (!hds.getFirst(AC_REQUEST_HEADERS).contains("thorization") 
              ? hds.getFirst(AC_REQUEST_HEADERS) + ",authorization" 
              : hds.getFirst(AC_REQUEST_HEADERS))
      );
    }
    hse.getResponseHeaders().put(new HttpString(AC_MAX_AGE), 86400);
    if(Methods.OPTIONS.equals(hse.getRequestMethod())) {
      hse.endExchange();
    }
    else if(next != null) {
      next.handleRequest(hse);
    }
  }

}
