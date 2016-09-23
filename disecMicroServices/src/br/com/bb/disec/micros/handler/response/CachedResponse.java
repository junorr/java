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

import br.com.bb.disec.micros.db.mongo.MongoCache;
import br.com.bb.disec.micros.db.mongo.MongoFilter;
import br.com.bb.disec.micros.db.mongo.MongoMetaData;
import br.com.bb.disec.micros.jiterator.MongoJsonIterator;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.bson.Document;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public class CachedResponse extends AbstractResponse {
  
  public static final String DEFAULT_DB = "micro";
  
  
  private MongoMetaData meta;
  
  
  public CachedResponse() {
    
  }
  
  
  public MongoCache cache() {
    return cache;
  }
  
  
  @Override
  public void send(HttpServerExchange hse, JsonObject json) throws Exception {
    this.init(hse, json);
    Timer tm = new Timer.Nanos().start();
    cache.setup(json);
    System.out.println("* CachedResponse cache.setup() Time: "+ tm.stop());
    if(!cache.isCachedCollection()) {
      super.send(hse, json);
      Timer t = new Timer.Nanos().start();
      cache.doCache(query.getResultSet());
      Logger.getLogger(getClass()).info("CACHE BUILD TIME: "+ t.lapAndStop());
      query.close();
    }
    //Timer tm = new Timer.Nanos().start();
    tm.clear().start();
    this.sendCached(hse);
    System.out.println("* CachedResultHandler sendResponse Time: "+ tm.stop());
    //Logger.getLogger(getClass()).info("CACHE RETRIEVE TIME: "+ tm.lapAndStop());
  }
  
  
  private void sendCached(HttpServerExchange hse) throws Exception {
    Timer tm = new Timer.Nanos().start();
    hse.getResponseHeaders().add(
        Headers.CONTENT_TYPE, 
        this.getEncodingFormat().getContentType()
    );
    System.out.println("* CachedResultHandler set header Time: "+ tm.stop());
    tm.clear().start();
    MongoFilter mop = new MongoFilter(cache);
    FindIterable<Document> result = mop.apply(json);
    System.out.println("* CachedResultHandler apply filter Time: "+ tm.stop());
    tm.clear().start();
    MongoJsonIterator jiter = new MongoJsonIterator(result.iterator(), mop.total());
    this.getEncoder().encode(jiter, hse.getOutputStream());
    System.out.println("* CachedResultHandler stream response Time: "+ tm.stop());
    hse.endExchange();
  }
  
}
