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

import br.com.bb.disec.micros.db.Infinispan;
import br.com.bb.disec.micros.db.mongo.MongoCache;
import br.com.bb.disec.micros.util.JsonHash;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.OutputStream;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public class CachedResponse extends AbstractResponse {
  
  private final MongoCache cache;
  
  private final JsonHash hash;
  
  
  public CachedResponse(JsonObject json) {
    super(json);
    hash = new JsonHash(json);
    this.cache = MongoCache.builder(json).build();
  }
  
  
  public MongoCache cache() {
    return cache;
  }
  
  
  public JsonObject json() {
    return json;
  }
  
  
  /**
   * Verifica se existe cache no MongoDB para a query
   * requisitada, criando o cache se necessário.
   * @return Esta instância de CachedResponse.
   * @throws Exception 
   */
  @Override
  public CachedResponse setupCache() throws Exception {
    if(!Infinispan.cache().containsKey(hash.collectionHash())) {
      super.handleRequest(null);
      Timer t = new Timer.Nanos().start();
      cache.doCache(query.getResultSet());
      Logger.getLogger(getClass()).info("CACHE BUILD TIME: "+ t.lapAndStop());
      query.close();
    }
    return this;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    this.setupCache();
    //Timer tm = new Timer.Nanos().start();
    this.sendResponse(hse);
    //System.out.println("* CachedResultHandler sendResponse Time: "+ tm.stop());
  }
  
  
  public void sendResponse(HttpServerExchange hse) throws Exception {
    hse.getResponseHeaders().add(
        Headers.CONTENT_TYPE, 
        this.getEncodingFormat().getContentType()
    );
    hse.startBlocking();
    this.sendResponse(hse.getOutputStream());
    hse.endExchange();
  }
  
  
  @Override
  public void sendResponse(OutputStream out) throws Exception {
    this.getEncoder().encode(cache.getCached(), out);
  }
  
}
