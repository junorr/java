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
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.JsonObject;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/02/2017
 */
public class URIHitsCounter implements HttpHandler {
  
  public static final String SQL_GROUP = "disecMicro";
  
  public static final String SQL_QUERY = "insertHits";
  
  public static final AtomicLong preSecs = new AtomicLong(0);
  
  public static final AtomicInteger counter = new AtomicInteger(0);
  
  public static final long startTime = System.currentTimeMillis();
  
  public static final Map<String,Integer> uriHits = new ConcurrentHashMap<>();
  
  
  private final HttpHandler next;
  
  
  public URIHitsCounter(HttpHandler next) {
    this.next = next;
  }


  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    String uri = hse.getRequestURI();
    String urikey = uri.substring(1);
    if(uri.toLowerCase().startsWith("/jwt")) return;
    
    long time = System.currentTimeMillis();
    long secs = (time - startTime) / 1000;
    counter.incrementAndGet();
    System.out.println("* secs: "+ secs);
    System.out.println("* hits: "+ counter.get());
    int top = 1;
    if(uriHits.containsKey(urikey)) {
      top = uriHits.get(urikey) + 1;
    }
    uriHits.put(urikey, top);
    if(uri.startsWith("/hits")) {
      this.get(hse, secs);
      return;
    }

    if(secs - preSecs.get() >= 60) {
      System.out.println(">>>> recording...");
      preSecs.set(secs);
      Optional<Entry<String,Integer>> opt = uriHits.entrySet()
          .stream().max((a,b)->a.getValue()-b.getValue());
      if(opt.isPresent()) {
        new SqlQuery(
            PoolFactory.getDefaultPool().getConnection(), 
            SqlSourcePool.pool()
        ).update(
            SQL_GROUP,
            SQL_QUERY, 
            //total_hits, top_uri, hits_uri, seconds
            counter.get(),
            opt.get().getKey(),
            opt.get().getValue(),
            secs
        );
      }
    }//if
    if(next != null) {
      next.handleRequest(hse);
    }
  }
  
  
  private void get(HttpServerExchange hse, long secs) throws Exception {
    JsonObject json = new JsonObject();
    int hits = counter.get();
    URIParam pars = new URIParam(hse.getRequestURI());
    System.out.println("* pars.len = "+ pars.length());
    if(pars.length() > 0) {
    System.out.println("* uri = "+ pars.getParam(0));
      hits = 0;
      if(uriHits.containsKey(pars.getParam(0))) {
        hits = uriHits.get(pars.getParam(0));
      }
    }
    json.addProperty("hits", hits);
    json.addProperty("secs", secs);
    hse.getResponseSender().send(json.toString());
    hse.endExchange();
  }
  
}
