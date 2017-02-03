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

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;

/**
 * Cliente HTTP de autenticação com o microserviço para 
 * simular um ususário a partir da matrícula/chave.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public class SimAuthClient extends AbstractAuthClient {
  
  /**
   * Contexto do microserviço de simulação de usuários (DEFAULT_CONTEXT = "simulate").
   */
  public static final String DEFAULT_CONTEXT = "simulate";
  

  private final String chave;
  
  private String tokenSSO;
  

  /**
   * Construtor padrão.
   * @param address {String}  Endereço HTTP do microserviço de autenticação.
   * @param port    {int}     Porta do microserviço de autenticação.
   * @param context {String}  Contexto do microserviço de autenticação.
   * @param chave   {String}  Chave/matrícula do usuário a ser simulado.
   */
  public SimAuthClient(String address, int port, String context, String chave) {
    super(address, port, context);
    if(chave == null || chave.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad User Key: "+ chave);
    }
    this.chave = chave;
  }
  
  
  /**
   * Retorna uma nova instância do cliente de autenticação SimAuthClient.
   * @param address {String}  Endereço HTTP do microserviço de autenticação.
   * @param port    {int}     Porta do microserviço de autenticação.
   * @param context {String}  Contexto do microserviço de autenticação.
   * @param chave   {String}  Chave/matrícula do usuário a ser simulado.
   * @return {SimAuthClient} nova instância do cliente de autenticação.
   */
  public static SimAuthClient of(String address, int port, String context, String chave) {
    return new SimAuthClient(address, port, context, chave);
  }
  
  
  /**
   * Retorna uma nova instância do cliente de autenticação SimAuthClient, 
   * com endereço, porta e contexto padrão do microserviço 
   * (http://127.0.0.1:9088/simulate).
   * @param chave   {String}  Chave/matrícula do usuário a ser simulado.
   * @return {SimAuthClient} nova instância do cliente de autenticação.
   */
  public static SimAuthClient ofDefault(String chave) {
    return new SimAuthClient(DEFAUTL_ADDRESS, DEFAULT_PORT, DEFAULT_CONTEXT, chave);
  }
  
  
  /**
   * Retorna o token BBSSO de autenticação na intranet.
   * @return {String} Token BBSSO.
   */
  public String getBBSSOToken() {
    return tokenSSO;
  }
  
  
  /**
   * Realiza uma tentatica de autenticação no microserviço.
   * @return {SimeAuthClient} Referência à propria instância.
   * @throws IOException Em case de erro na autenticação. 
   * Ex: servidor não encontrado.
   */
  public SimAuthClient doAuth() throws IOException {
    return this.doAuth(null);
  }
  
  
  /**
   * Realiza uma tentatica de autenticação no microserviço.
   * @param hse Não usado, pode ser null. Sobrescreve o 
   * método da classe pai, porém não usa o argumento.
   * @return {SimeAuthClient} Referência à propria instância.
   * @throws IOException Em case de erro na autenticação. 
   * Ex: servidor não encontrado.
   */
  @Override
  public SimAuthClient doAuth(HttpServerExchange hse) throws IOException {
    jsonUser = null;
    tokenSSO = null;
    status = null;
    Response resp = Request.Get(getUriString()+"/"+chave).execute();
    HttpResponse hresp = resp.returnResponse();
    status = hresp.getStatusLine();
    if(status.getStatusCode() == 200) {
      tokenSSO = hresp.getFirstHeader(Headers.SET_COOKIE_STRING).getValue().split("=")[1];
      jsonUser = EntityUtils.toString(hresp.getEntity());
    } else {
      throw new IOException("Cannot authenticate against: "+ getUriString()
          + ".\n\t[Server Response]=> "+ status.toString());
    }
    return this;
  }
  
}
