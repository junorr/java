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

package br.com.bb.disec.micro.response;

import br.com.bb.disec.micro.util.JsonSender;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2016
 */
public class JsonCachedResponse extends AbstractCachedResponse {

  private final long total;
  
  
  public JsonCachedResponse(List<String> columns, long totalCount) {
    super(columns);
    this.total = totalCount;
  }
  
  
  @Override
  public void doResponse(HttpServerExchange hse, MongoCursor<Document> cur) throws Exception {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/json; charset=utf-8"
    );
    JsonSender js = new JsonSender(hse.getResponseSender());
    js.startObject()
        .put("columns")
        .write(":")
        .write(JSON.serialize(columns));
    js.nextElement()
        .put("total", total)
        .nextElement()
        .startArray("data");
    long count = 0;
    Document next = (cur.hasNext() ? cur.next() : null);
    do {
      if(next != null) {
        js.write(JSON.serialize(next));
        count++;
      }
      next = (cur.hasNext() ? cur.next() : null);
      if(next != null) js.nextElement();
    } while(next != null);
    js.endArray()
        .nextElement()
        .put("count", count)
        .endObject()
        .flush();
  }
  
}
