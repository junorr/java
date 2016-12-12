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

package br.com.bb.disec.micro.client;

import br.com.bb.sso.session.CookieName;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;

/**
 * Classe instanciável de um cliente de autenticação. Essa classe implementa a
 * autenticação de um usuário na URL definida pelo construtor ou, caso não seja
 * definida, será usado a URL padrão definida na classe herdada.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public class AuthenticationClient extends AbstractAuthClient {
  
  /**
   * Construtor para criação de um cliente de autenticação com definição do URL 
   * de autenticação do cliente através dos parametros.
   * @param address Endereço de autenticação
   * @param port Porta de autenticação
   * @param context Contexto de autenticação
   */
  public AuthenticationClient(String address, int port, String context) {
    super(address, port, context);
  }
  
  /**
   * Cria uma instancia da classe passando como parametro dados para definição
   * da URL
   * @param address Endereço de autenticação
   * @param port Porta de autenticação
   * @param context Contexto de autenticação
   * @return Objeto instanciado da classe
   */
  public static AuthenticationClient of(String address, int port, String context) {
    return new AuthenticationClient(address, port, context);
  }
  
  /**
   * Cria uma instancia da classe com os valores padrões da URL.
   * @return Objeto instanciado da classe
   */
  public static AuthenticationClient ofDefault() {
    return new AuthenticationClient(DEFAUTL_ADDRESS, DEFAULT_PORT, DEFAULT_CONTEXT);
  }
  
  /**
   * Define o cookie do BBSSO token e a URL de autenticação e realiza a autenticaçã.
   * @param bbssoToken Token de autenticação do usuário gerado pelo autenticador do BBSSO
   * @return this
   * @throws IOException 
   */
  public AuthenticationClient doAuth(String bbssoToken) throws IOException {
    if(bbssoToken != null) {
      Request req = Request.Get(getUriString());
      req.setHeader(
          Headers.COOKIE_STRING, 
          CookieName.BBSSOToken.name() + "="+ bbssoToken+ ";"
      );
      doAuth(req);
    }
    return this;
  }
  
  /**
   * Injeta os tokens de autenticação na requisição e executa a autenticação.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @return Instancia da classe com a autenticação executada
   * @throws IOException 
   */
  @Override
  public AuthenticationClient doAuth(HttpServerExchange hse) throws IOException {
    Request req = Request.Get(getUriString(hse));
    new AuthCookieManager().injectAuthCookies(req, hse);
    return doAuth(req);
  }
  
  /**
   * Realiza a requisição para o URL de autenticação.
   * @param req Requisição
   * @return Instancia da classe com a autenticação executada
   * @throws IOException 
   */
  private AuthenticationClient doAuth(Request req) throws IOException {
    Response resp = req.execute();
    HttpResponse hresp = resp.returnResponse();
    status = hresp.getStatusLine();
    jsonUser = EntityUtils.toString(hresp.getEntity());
    return this;
  }
  
}
