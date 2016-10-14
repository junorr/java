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

import br.com.bb.disec.micros.channel.CsvChannel;
import br.com.bb.disec.micros.jiterator.JsonIterator;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Objects;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/09/2016
 */
public class CsvEncoder implements Encoder {
  
  
  @Override
  public void encode(JsonIterator jiter, OutputStream out) throws Exception {
    CsvChannel channel = new CsvChannel(
        Channels.newChannel(out)
    );
    if(!jiter.hasNext()) {
      channel.close();
      return;
    }
    Document doc = jiter.next();
    this.writeColumns(channel, doc);
    channel.newLine();
    do {
      this.writeDocument(channel, doc);
      doc = (jiter.hasNext() ? jiter.next() : null);
      if(doc != null) channel.newLine();
    } 
    while(doc != null);
    channel.close();
  }
  
  
  private void writeColumns(CsvChannel channel, Document doc) throws Exception {
    if(doc == null) return;
    Object[] keys = doc.keySet().toArray();
    for(int i = 0; i < keys.length; i++) {
      if(!"_id".equals(keys[i].toString())
          && !"created".equals(keys[i].toString())) {
        channel.put(Objects.toString(keys[i]));
        if(i < keys.length -1) {
          channel.nextElement();
        }
      }
    }
  }
  
  
  private void writeDocument(CsvChannel channel, Document doc) throws Exception {
    if(doc == null) return;
    Object[] keys = doc.keySet().toArray();
    for(int i = 0; i < keys.length; i++) {
      if(!"_id".equals(keys[i].toString())
          && !"created".equals(keys[i].toString())) {
        channel.put(doc.get(keys[i]));
        if(i < keys.length -1) {
          channel.nextElement();
        }
      }
    }
  }

}
