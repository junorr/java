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

import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.micro.ServerSetup;
import br.com.bb.disec.micro.db.AccessPersistencia;
import br.com.bb.disec.micro.db.CtuPersistencia;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.jwt.JWT;
import br.com.bb.disec.micro.jwt.JWTHeader;
import br.com.bb.disec.micro.jwt.JWTKey;
import br.com.bb.disec.micro.jwt.JWTPayload;
import br.com.bb.disec.micro.util.CookieUserParser;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.bean.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class JWTAuthHandler implements JsonHandler {
  
  public static final String SQL_GROUP = "disecMicro";
  
  public static final String SQL_INSERT_LOG = "insertLog";
  
  public static final String AUTH_CONTEXT = "/jwt";
  
  public static final int DEFAULT_CD_CTU = 99999;
  
  
  public final JWTKey jwtKey;
  
  private final Gson gson;
  
  
  public JWTAuthHandler() {
    gson = new GsonBuilder().setPrettyPrinting().create();
    jwtKey = ServerSetup.instance().config().getJWTKey();
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    User user = null;
    try {
      user = new CookieUserParser().parseHttp(hse);
      JWT jwt = null;
      if(Methods.GET.equals(hse.getRequestMethod())) {
        jwt = this.get(hse, user);
      }
      else if(Methods.POST.equals(hse.getRequestMethod())) {
        jwt = this.post(hse, user);
      }
      //System.out.println("* JWT:\n");
      //System.out.println(gson.toJson(jwt));
      hse.setStatusCode(200).setReasonPhrase("OK");
      hse.getResponseSender().send(jwt.createToken());
    }
    catch(IllegalAccessException e) {
      hse.setStatusCode(401)
          .setReasonPhrase("Unauthorized. "+ e.getMessage());
    }
    catch(Exception e) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. "+ e.getMessage());
    }
    hse.endExchange();
  }
  
  
  private JWT get(HttpServerExchange hse, User user) throws Exception {
    if(!doAuth(hse, user)) {
      log(hse, user, false);
      throw new IllegalAccessException(hse.getRequestURI());
    }
    log(hse, user, true);
    JWTPayload pld = new JWTPayload();
    pld.put("user", gson.toJsonTree(user));
    pld.put("url", this.getAuthURL(hse));
    return new JWT(new JWTHeader(), pld, jwtKey);
  }
  
  
  private JWT post(HttpServerExchange hse, User user) throws Exception {
    JsonElement json = new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    );
    if(!json.isJsonArray()) {
      throw new IllegalStateException("Json Array Expected");
    }
    JsonArray ar = json.getAsJsonArray();
    for(int i = 0; i < ar.size(); i++) {
      URLD url = new URLD(ar.get(i).getAsString());
      if(!doAuth(url, user)) {
        log(url, hse, user, false);
        throw new IllegalAccessException(url.toString());
      }
      log(url, hse, user, true);
    }
    JWTPayload pld = new JWTPayload();
    pld.put("user", gson.toJsonTree(user));
    pld.put("url", ar);
    return new JWT(new JWTHeader(), pld, jwtKey);
  }
  
  
  private String getAuthURL(HttpServerExchange hse) {
    String url = hse.getRequestURL();
    if(url.contains(AUTH_CONTEXT)) {
      int idx = url.indexOf(AUTH_CONTEXT);
      url = url.substring(0, idx)
          + url.substring(idx+AUTH_CONTEXT.length());
    }
    return url;
  }
  
  
  public boolean doAuth(HttpServerExchange hse, User user) throws Exception {
		return doAuth(new URLD(getAuthURL(hse)), user);
	}
  
  
  public boolean doAuth(URLD urld, User user) throws Exception {
		CtuPersistencia ctp = new CtuPersistencia(urld);
		AccessPersistencia acp = new AccessPersistencia();
    //recupera todos os conteúdos da tabela dcr_ctu
    //com base na URL acessada.
    List<IDcrCtu> ctus = ctp.findAll();
    if(ctus == null || ctus.isEmpty()) {
      Logger.getLogger(this.getClass()).info("Nenhum conteúdo encontrado para a URL: "+ urld);
      ctus = Arrays.asList(ctp.getById(DEFAULT_CD_CTU));
    }
    //Verifica autorização de acesso para os conteúdos encontrados.
    boolean access = ctus.get(0).getCdCtu() == DEFAULT_CD_CTU;
    for(IDcrCtu ctu : ctus) {
      access = access || acp.checkAccess(
          user, ctu.getCdCtu()
      );
    }
    return access;
	}
  
  
  public void log(HttpServerExchange hse, User user, boolean auth) throws Exception {
    log(new URLD(hse.getRequestURL()), hse, user, auth);
  }


  public void log(URLD url, HttpServerExchange hse, User user, boolean auth) throws Exception {
    //ip, url, context, uri, cd_usu, uor_depe, uor_eqp, autd
    new SqlQuery(
        PoolFactory.getDefaultPool().getConnection(), 
        SqlSourcePool.pool()
    ).update(
        SQL_GROUP,
        SQL_INSERT_LOG, 
        hse.getConnection().getPeerAddress().toString(), 
        url.getURL(), 
        url.getContext(), 
        url.getURI(),
        user.getChave(),
        user.getUorDepe(),
        user.getUorEquipe(),
        auth
    );
  }

}
