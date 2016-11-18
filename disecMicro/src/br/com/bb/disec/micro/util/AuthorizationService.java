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

package br.com.bb.disec.micro.util;

import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.micro.db.AccessPersistencia;
import br.com.bb.disec.micro.db.CtuPersistencia;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.bean.User;
import io.undertow.server.HttpServerExchange;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/11/2016
 */
public class AuthorizationService {

  public static final String SQL_GROUP = "disecMicro";
  
  public static final String SQL_INSERT_LOG = "insertLog";
  
  public static final String AUTH_CONTEXT = "/jwt";
  
  public static final int DEFAULT_CD_CTU = 99999;
  
  
  private final User user;
  
  private final AccessPersistencia acss;
  
  private URLD unauth;
  
  
  public AuthorizationService(User usr) {
    Objects.requireNonNull(usr, "Bad Null User");
    this.user = usr;
    acss = new AccessPersistencia();
    unauth = null;
  }
  
  
  public User getUser() {
    return user;
  }
  
  
  public URLD getUnauthorizedURL() {
    return unauth;
  }
  
  
  public boolean hasUnauthorizedURL() {
    return unauth != null;
  }
  
  
  public boolean authorize(HttpServerExchange hse, List<URLD> urls) throws Exception {
    Objects.requireNonNull(hse, "Bad Null HttpServerExchange");
    Objects.requireNonNull(urls, "Bad Null List<URLD>");
    unauth = null;
    boolean access = true;
    for(URLD url : urls) {
      access = access && authorize(hse, url);
    }
    return access;
	}
  
  
  public boolean authorize(HttpServerExchange hse, URLD urld) throws Exception {
    Objects.requireNonNull(urld, "Bad Null URLD");
    Objects.requireNonNull(hse, "Bad Null HttpServerExchange");
		CtuPersistencia ctp = new CtuPersistencia(urld);
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
      access = access || acss.checkAccess(
          user, ctu.getCdCtu()
      );
    }
    this.log(urld, hse, access);
    if(!access) unauth = urld;
    return access;
	}
  

  private void log(URLD url, HttpServerExchange hse, boolean auth) throws Exception {
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
