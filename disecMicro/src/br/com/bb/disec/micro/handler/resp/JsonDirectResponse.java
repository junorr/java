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

package br.com.bb.disec.micro.handler.resp;

import br.com.bb.disec.micro.channel.JsonChannel;
import br.com.bb.disec.micro.db.SqlObjectType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.nio.channels.Channels;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class JsonDirectResponse implements DirectFormatResponse {
  
  private final SqlObjectType jt = new SqlObjectType();

  
  @Override
  public void doResponse(HttpServerExchange hse, ResultSet rst) throws Exception {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/json; charset=utf-8"
    );
    System.out.println("* Enter JsonDirectResponse.doResponse blocking");
    hse.startBlocking();
    System.out.println("* After blocking call");
    JsonChannel js = new JsonChannel(Channels.newChannel(hse.getOutputStream()));
    long count = 0;
    js.startObject();
    this.writeColumns(js, rst);
    js.nextElement().startArray("data");
    if(rst.next()) {
      while(true) {
        this.writeRow(js, rst);
        count++;
        if(rst.next()) js.nextElement();
        else break;
      }
    }
    js.endArray()
        .nextElement()
        .put("total", count)
        .nextElement()
        .put("count", count)
        .endObject()
        .close();
  }
  
  
  private void writeRow(JsonChannel js, ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    js.startObject();
    for(int i = 1; i <= cols; i++) {
      js.put(meta.getColumnLabel(i), jt.getObject(rs, i));
      if(i < cols) {
        js.nextElement();
      }
    }
    js.endObject();
  }
  
  
  private void writeColumns(JsonChannel js, ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    js.startArray("columns");
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      js.put(meta.getColumnLabel(i));
      if(i < cols) {
        js.nextElement();
      }
    }
    js.endArray();
  }

}
