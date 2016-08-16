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

import br.com.bb.disec.micro.db.SqlQueryFactory;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.util.URIParam;
import io.undertow.server.HttpServerExchange;
import java.nio.charset.Charset;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class GetSqlHandler implements JsonHandler {
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    if(pars.length() < 1) {
      String msg = "Bad Request. No Query Informed";
      Logger.getLogger(this.getClass()).warn(msg);
      hse.setStatusCode(400).setReasonPhrase(msg);
      hse.endExchange();
      return;
    }
    String group = pars.getParam(0);
    String query = pars.getParam(1);
    if(!SqlSourcePool.getDefaultSqlSource().containsSql(group, query)) {
      String msg = "Not Found (query="+ query+ ")";
      Logger.getLogger(this.getClass()).warn(msg);
      hse.setStatusCode(404)
          .setReasonPhrase(msg);
      hse.endExchange();
      return;
    }
    Object[] args = new Object[pars.length() -2];
    for(int i = 0; i < pars.length() -2; i++) {
      args[i] = pars.getObject(i+2);
    }
    String resp = SqlQueryFactory.getDefaultQuery()
        .exec(group, query, args).toPrettyPrintJson() + "\n";
    this.putJsonHeader(hse);
    if(hse.getQueryParameters().containsKey("callback")) {
      resp = hse.getQueryParameters().get("callback").peekFirst()
          + "(" + resp + ")";
    }
    hse.getResponseSender().send(resp, Charset.forName("UTF-8"));
    hse.endExchange();
  }

}
