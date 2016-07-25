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

import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.db.SqlStore;
import br.com.bb.disec.micro.db.SqlStorePool;
import br.com.bb.disec.micro.util.URIParam;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class GetSqlHandler implements HttpHandler {
  
  public static final String DEFAULT_DB = "103";
  
  private final SqlStore store;
  
  
  public GetSqlHandler() {
    try {
      store = SqlStorePool.getDefaultStore();
    }
    catch(IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  
  public SqlQuery getQuery() throws IOException, SQLException {
    return new SqlQuery(PoolFactory
        .getPool(DEFAULT_DB).getConnection(), store
    );
  }
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    URIParam pars = new URIParam(hse.getRequestURI());
    hse.getResponseHeaders().put(
        new HttpString("Content-Type"), "application/json; charset=utf-8"
    );
    String query = pars.getParam(0);
    if(!store.queries().containsKey(query)) {
      System.out.println("ERROR: [GetSqlHandler] Query Not Found ("+ query+ ")");
      hse.setStatusCode(404)
          .setReasonPhrase("Not Found ("+ query+ ")");
      hse.endExchange();
      return;
    }
    Object[] args = new Object[pars.length() -1];
    for(int i = 0; i < pars.length() -1; i++) {
      args[i] = pars.getObject(i+1);
    }
    String resp = this.getQuery()
        .exec(query, args).toPrettyPrintJson();
    hse.getResponseSender().send(resp, Charset.forName("UTF-8"));
    hse.endExchange();
  }

}
