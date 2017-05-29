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

package br.com.bb.disec.micros.coder;

import br.com.bb.disec.micros.channel.JsonChannel;
import br.com.bb.disec.micros.jiterator.JsonIterator;
import static br.com.bb.disec.micros.util.JsonConstants.COLUMNS;
import static br.com.bb.disec.micros.util.JsonConstants.COUNT;
import static br.com.bb.disec.micros.util.JsonConstants.DATA;
import static br.com.bb.disec.micros.util.JsonConstants.METADATA;
import static br.com.bb.disec.micros.util.JsonConstants.TOTAL;
import com.mongodb.util.JSON;
import java.io.OutputStream;
import java.nio.channels.Channels;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2016
 */
public class JsonEncoder implements Encoder {
  
  private final String metadata;
  
  
  public JsonEncoder() {
    metadata = null;
  }
  
  
  public JsonEncoder(String metadata) {
    this.metadata = metadata;
  }

  
  @Override
  public void encode(JsonIterator jiter, OutputStream out) throws Exception {
    JsonChannel channel = new JsonChannel(
        Channels.newChannel(out)
    );
    if(!jiter.hasNext()) {
      channel.close();
      return;
    }
    Document doc = jiter.next();
    this.writeColumns(channel, doc);
    if(metadata != null) {
      channel.nextElement().put(METADATA, metadata);
    }
    channel.nextElement()
        .put(TOTAL, jiter.total())
        .nextElement()
        .startArray(DATA);
    long count = 0;
    do {
      channel.write(JSON.serialize(doc));
      count++;
      doc = (jiter.hasNext() ? jiter.next() : null);
      if(doc != null) { 
        channel.nextElement();
      }
    } 
    while(doc != null);
    channel.endArray()
        .nextElement()
        .put(COUNT, count)
        .endObject()
        .close();
  }
  
  
  private void writeColumns(JsonChannel channel, Document doc) throws Exception {
    if(doc == null) return;
    Object[] keys = doc.keySet().toArray();
    channel.startObject()
        .put(COLUMNS).write(":")
        .write(JSON.serialize(keys));
  }
  
}
