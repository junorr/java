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

package br.com.bb.disec.micro.handler.encode;

import br.com.bb.disec.micro.channel.JsonChannel;
import br.com.bb.disec.micro.jiterator.JsonIterator;
import com.mongodb.util.JSON;
import io.undertow.server.HttpServerExchange;
import java.nio.channels.Channels;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2016
 */
public class JsonEncodingHandler extends AbstractEncodingHandler {

  private final long total;
  
  
  public JsonEncodingHandler(JsonIterator ji, long totalCount) {
    super(ji, EncodingFormat.JSON);
    this.total = totalCount;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(!jiter.hasNext()) {
      hse.endExchange();
      return;
    }
    super.handleRequest(hse);
    hse.startBlocking();
    JsonChannel channel = new JsonChannel(Channels.newChannel(hse.getOutputStream()));
    Document doc = jiter.next();
    this.writeColumns(channel, doc);
    channel.nextElement()
        .put("total", total)
        .nextElement()
        .startArray("data");
    long count = 0;
    do {
      channel.write(JSON.serialize(doc));
      count++;
      if((doc = jiter.next()) != null) 
        channel.nextElement();
    } 
    while(doc != null);
    channel.endArray()
        .nextElement()
        .put("count", count)
        .endObject()
        .close();
  }
  
  
  private void writeColumns(JsonChannel channel, Document doc) throws Exception {
    if(doc == null) return;
    Object[] keys = doc.keySet().toArray();
    channel.startObject()
        .put("columns").write(":")
        .write(JSON.serialize(keys));
  }
  
}
