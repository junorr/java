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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.handler.response.CachedResponse;
import br.com.bb.disec.micros.handler.response.DirectResponse;
import static br.com.bb.disec.micros.util.JsonConstants.CACHETTL;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import us.pserver.timer.Timer;
import static br.com.bb.disec.micros.util.JsonConstants.NAME;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class PostSqlHandler implements JsonHandler {
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    try {
      //Timer tm = new Timer.Nanos().start();
      JsonObject json = this.parseJson(hse);
      URIParam pars = new URIParam(hse.getRequestURI());
      if(pars.length() < 2) {
        hse.setStatusCode(404)
          .setReasonPhrase("Not Found: Missing Query Group and Name");
        return;
      }
      json.addProperty(GROUP, pars.getParam(0));
      json.addProperty(NAME, pars.getParam(1));
      //System.out.println("* PostSqlHandler parseJson Time: "+ tm.stop());
      //tm.clear().start();
      System.out.println("*** PostSqlHandler.response ***");
      if(json.has(CACHETTL)) {
        new CachedResponse(json).handleRequest(hse);
      } else {
        new DirectResponse(json).handleRequest(hse);
      }
      //System.out.println("* PostSqlHandler result Time: "+ tm.stop());
    }
    catch(Exception e) {
      e.printStackTrace();
      hse.setStatusCode(400)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    finally {
      hse.endExchange();
    }
  }
  
  
  private JsonObject parseJson(HttpServerExchange hse) throws IOException {
    return new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    ).getAsJsonObject();
  }
  
}
