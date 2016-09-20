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
import br.com.bb.disec.micro.db.AccessPersistencia;
import br.com.bb.disec.micro.db.CtuPersistencia;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.util.CookieUserParser;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.bean.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class AuthenticationServerHandler implements JsonHandler {
  
  public static final String SQL_GROUP = "disecMicro";
  
  public static final String SQL_INSERT_LOG = "insertLog";
  
  public static final int DEFAULT_CD_CTU = 99999;
  
  private final Gson gson;
  
  
  public AuthenticationServerHandler() {
    gson = new GsonBuilder().setPrettyPrinting().create();
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
      if(doAuth(hse, user)) {
        this.log(hse, user, true);
        hse.setStatusCode(200).setReasonPhrase("OK");
        this.putJsonHeader(hse);
        hse.getResponseSender().send(gson.toJson(user));
      }
      else {
        this.log(hse, user, false);
        hse.setStatusCode(401).setReasonPhrase("Unauthorized");
      }
    }
    catch(IOException e) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. "+ e.getMessage());
    }
    hse.endExchange();
  }
  
  
  private String getAuthURL(HttpServerExchange hse) {
    String url = hse.getRequestURL();
    if(url.contains("auth/")) {
      int idx = url.indexOf("auth/");
      url = url.substring(0, idx)
          + url.substring(idx+5);
    }
    return url;
  }
  
  
  public boolean doAuth(HttpServerExchange hse, User user) throws Exception {
    URLD urld = new URLD(getAuthURL(hse));
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
    //ip, url, context, uri, cd_usu, uor_depe, uor_eqp, autd
    URLD url = new URLD(this.getAuthURL(hse));
    new SqlQuery(
        PoolFactory.getDefaultPool().getConnection(), 
        SqlSourcePool.getDefaultSqlSource()
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
