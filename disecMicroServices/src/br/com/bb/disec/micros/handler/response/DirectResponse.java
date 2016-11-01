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

package br.com.bb.disec.micros.handler.response;

import br.com.bb.disec.micros.jiterator.JsonIterator;
import br.com.bb.disec.micros.jiterator.ResultSetJsonIterator;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.OutputStream;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class DirectResponse extends AbstractResponse {
  
  private JsonIterator jiter;
  
  
  public DirectResponse(JsonObject json) {
    super(json);
  }
  
  
  @Override
  public DirectResponse setupCache() throws Exception {
    super.handleRequest(null);
    jiter = new ResultSetJsonIterator(query.getResultSet());
    return this;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    hse.getResponseHeaders().add(
        Headers.CONTENT_TYPE, 
        this.getEncodingFormat().getContentType()
    );
    hse.startBlocking();
    this.setupCache().sendResponse(hse.getOutputStream());
  }
  
  
  @Override
  public void sendResponse(OutputStream out) throws Exception {
    Timer tm = new Timer.Nanos().start();
    try {
      this.getEncoder().encode(jiter, out);
    }
    finally {
      Logger.getLogger(getClass()).info("DIRECT RETRIEVE TIME: "+ tm.lapAndStop());
      query.close();
    }
  }
  
}
