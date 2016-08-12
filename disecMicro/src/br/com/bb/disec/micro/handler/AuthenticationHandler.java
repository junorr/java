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

import br.com.bb.disec.bean.DcrCtu;
import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.micro.db.AccessPersistencia;
import br.com.bb.disec.micro.db.CtuPersistencia;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.bean.User;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class AuthenticationHandler extends StringPostHandler implements JsonHandler, AuthHttpHandler {
  
  private final AuthHttpHandler cookieHandler;
  
  private final AuthHttpHandler usuSimHandler;
  
  private User user;
  
  
  public AuthenticationHandler() {
    cookieHandler = new CookieAuthHandler();
    usuSimHandler = new SimAuthHandler();
  }
  
  
  @Override
  public User getAuthUser() {
    return user;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    User user = null;
    if(Methods.GET.equals(hse.getRequestMethod())) {
      cookieHandler.handleRequest(hse);
      user = cookieHandler.getAuthUser();
    }
    else if(Methods.POST.equals(hse.getRequestMethod())) {
      usuSimHandler.handleRequest(hse);
      user = usuSimHandler.getAuthUser();
    }
    if(!doAuth(hse)) {
      hse.setStatusCode(401).setReasonPhrase("Unauthorized");
      hse.endExchange();
    }
  }
  
  
  public boolean doAuth(HttpServerExchange hse) throws SQLException {
    URLD urld = new URLD(hse.getRequestURL());
		CtuPersistencia ctp = new CtuPersistencia(urld);
		AccessPersistencia acp = new AccessPersistencia();
    //recupera todos os conteúdos da tabela dcr_ctu
    //com base na URL acessada.
    List<IDcrCtu> ctus = ctp.findAll();
    if(ctus == null || ctus.isEmpty()) {
      Logger.getLogger(this.getClass()).info("Nenhum conteúdo encontrado para a URL: %s"+ urld);
      ctus = Arrays.asList((IDcrCtu)new DcrCtu().setCdCtu(99999));
    }
    //Verifica autorização de acesso para os conteúdos encontrados.
    boolean access = false;
    for(IDcrCtu ctu : ctus) {
      access = access || acp.checkAccess(
          user, ctu.getCdCtu()
      );
    }
    return access;
	}

}
