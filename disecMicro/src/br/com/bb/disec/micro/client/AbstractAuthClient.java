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

import br.com.bb.sso.bean.User;
import com.google.gson.Gson;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import org.apache.http.HttpStatus;

/**
 * Classe abstrata de um cliente de autenticação. Nesta classe estão contidas 
 * dados default de endereço, porta e contexto além de ações de autenticação de 
 * usuário.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public abstract class AbstractAuthClient extends AbstractClient {
  
  public static final String DEFAUTL_ADDRESS = "http://127.0.0.1";
  
  public static final int DEFAULT_PORT = 9088;
  
  public static final String DEFAULT_CONTEXT = "auth";
  

  protected String jsonUser;
  
  /**
   * Construtor para criação de um cliente de autenticação com definição do URL 
   * de autenticação do cliente através dos parametros.
   * @param address Endereço de autenticação
   * @param port Porta de autenticação
   * @param context Contexto de autenticação
   */
  protected AbstractAuthClient(String address, int port, String context) {
    super(address, port, context);
  }
  
  /**
   * Construtor padrão para criação de um cliente de autenticação com os dados
   * padrões existentes na classe.
   */
  protected AbstractAuthClient() {
    super(DEFAUTL_ADDRESS, DEFAULT_PORT, DEFAULT_CONTEXT);
  }
  
  /**
   * Busca o usuário autenticado. A classe User é referente a classe do BB SSO.
   * @return Usuário
   */
  public User getUser() {
    if(jsonUser == null || jsonUser.length() < 10) {
      throw new IllegalArgumentException("Bad Response. Can not create User");
    }
    return new Gson().fromJson(jsonUser, User.class);
  }
  
  /**
   * Constroi o endereço completo da URL do cliente de autenticação.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @return URL completa
   */
  public String getUriString(HttpServerExchange hse) {
    return this.getUriString() + "/" + hse.getRequestURI();
  }
  
  /**
   * Método que será responsável por fazer todas os passos para autenticar um usuário.
   * @param hse
   * @return
   * @throws IOException 
   */
  public abstract AbstractAuthClient doAuth(HttpServerExchange hse) throws IOException;
  
  /**
   * Verifica se o usuário está autenticado.
   * @return true | false Caso o usuário esteja autenticado | Caso contrário
   */
  public boolean isAuthenticated() {
    return status != null 
        && status.getStatusCode() == HttpStatus.SC_OK;
  }
  
  /**
   * Executa a autenticação do usuário e verifica se ele foi autenticado.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @return true | false Caso o usuário esteja autenticado | Caso contrário
   * @throws IOException 
   */
  public boolean authenticate(HttpServerExchange hse) throws IOException {
    return doAuth(hse).isAuthenticated();
  }
  
}
