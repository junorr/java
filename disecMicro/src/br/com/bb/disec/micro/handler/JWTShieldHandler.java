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

import br.com.bb.disec.micro.ServerSetup;
import br.com.bb.disec.micro.jwt.JWT;
import br.com.bb.disec.micro.jwt.JWTKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;


/**
 * Um handler que pode ser usado para fazer as validações do JWT da requisição.
 * Valida o conteúdo do JWT e verifica se a requisição na URL é autorizada para
 * o JWT.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/08/2016
 */
public class JWTShieldHandler implements HttpHandler {
  
  public static final String AUTH_EXCEPTIONS_FILE = "/resources/auth-exclude.json";
  
  
  private final HttpHandler next;
  
  private final JsonArray exceptions;
  
  private final JWTKey jwtKey;
  
  /**
   * Construtor padrão com inicialização dos atributos da classe.
   * @param next Próximo handler a ser chamado no caso de encadeamento de handlers
   */
  public JWTShieldHandler(HttpHandler next) {
    this.next = next;
    exceptions = readExcludes();
    jwtKey = ServerSetup.instance().config().getJWTKey();
  }
  
  /**
   * Ler o arquivo padrão de exclusão de URLs.
   * @return JSON array das URLs de exclusão
   */
  private JsonArray readExcludes() {
    return ServerSetup.instance()
        .loader()
        .loadJson(AUTH_EXCEPTIONS_FILE)
        .getAsJsonArray();
  }
  
  /**
   * Pega o próximo handler.
   * @return handler
   */
  public HttpHandler getNext() {
    return next;
  }
  
  /**
   * Valida o token JWT e verifica se a requisição à URL está de acordo com as URLs
   * autorizadas pelo token JWT da requisição.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    boolean donext = true;
    if(!isUriExcluded(hse.getRequestMethod(), hse.getRequestURI())) {
      try {
        if(!hse.getRequestHeaders().contains(Headers.AUTHORIZATION)) {
          throw new IllegalStateException("Authorization Header Missing");
        }
        String token = hse.getRequestHeaders().getFirst(Headers.AUTHORIZATION);
        if(token.contains("Bearer ")) {
          token = token.replace("Bearer ", "");
        }
        JWT jwt = JWT.fromBase64(token);
        //System.out.println("* jwt.verifySign: "+ jwt.verifySign(jwtKey));
        //System.out.println("* jwt.isExpired.: "+ jwt.isExpired());
        //System.out.println("* jwt.url.......: "+ jwt.getPayload().get("url"));
        //System.out.println("* hse.url.......: "+ hse.getRequestURL());
        boolean urlmatch = false;
        if(jwt.getPayload().get("url").isJsonArray()) {
          JsonArray ar = jwt.getPayload().get("url").getAsJsonArray();
          for(int i = 0; i < ar.size(); i++) {
            String url = ar.get(i).getAsString();
            urlmatch = urlmatch 
                || url.equals(hse.getRequestURL()) 
                || hse.getRequestURL().startsWith(url);
            if(urlmatch) break;
          }
        }
        else {
          String url = jwt.getPayload().get("url").getAsString();
          urlmatch = urlmatch 
              || url.equals(hse.getRequestURL()) 
              || hse.getRequestURL().startsWith(url);
        }
        //System.out.println("* urlmatch......: "+ urlmatch);
        String error = null;
        if(!jwt.verifySign(jwtKey)) error = "Bad Signature";
        if(jwt.isExpired()) error = "JWT Expired";
        if(!urlmatch) error = hse.getRequestURL();
        if(error != null) {
          throw new IllegalAccessException(error);
        }
      }
      catch(Exception e) {
        //e.printStackTrace();
        //System.out.println("# "+ e.toString());
        String reason = "Unauthorized";
        if(e.getMessage() != null && !e.getMessage().isEmpty()) {
          reason += ": "+ e.getMessage();
        }
        hse.setStatusCode(401)
            .setReasonPhrase(reason)
            .endExchange();
        donext = false;
      }
    }
    if(donext && next != null) {
      next.handleRequest(hse);
    }
  }
  
  /**
   * Verifica se a URL da requisição está no arquivo de exclusão (auth-exclude.json).
   * @param meth Método de requisição
   * @param uri URL de requisição
   * @return true | false Caso esteja | Caso contrário
   */
  private boolean isUriExcluded(HttpString meth, String uri) {
    if(exceptions == null 
        || exceptions.size() < 1) {
      return false;
    }
    boolean excluded = false;
    for(JsonElement e : exceptions) {
      if(e.isJsonObject()) {
        JsonObject obj = e.getAsJsonObject();
        excluded = obj.has("method") && obj.has("uri")
            && meth.equalToString(obj.get("method").getAsString())
            && matchUri(uri, obj.get("uri").getAsString());
      }
      else {
        excluded = matchUri(uri, e.getAsString());
      }
      if(excluded) break;
    }
    return excluded;
  }

  /**
   * Verifica se duas URLs dão match.
   * @param uri URL da requisição
   * @param exclude URL do arquivo de exclusão
   * @return true | false Caso dê match | Caso contrário
   */
  private boolean matchUri(String uri, String exclude) {
		if(uri == null || exclude == null) 
      return false;
    String suri = uri.toLowerCase();
    boolean match = false;
    exclude = exclude.toLowerCase();
    if(exclude.startsWith("*")) {
      match = suri.endsWith(exclude.replace("*", ""));
    }
    else if(exclude.endsWith("*")) {
      match = suri.startsWith(exclude.replace("*", ""));
    }
    else if(exclude.contains("*")) {
      String[] split = exclude.split("\\*");
      match = suri.startsWith(split[0]);
      if(split.length > 1) {
        match = match && suri.endsWith(split[1]);
      }
    }
    else {
      match = suri.contains(exclude);
    }
		return match;
	}

}
