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

import br.com.bb.disec.micro.util.SqlObjectType;
import br.com.bb.disec.micro.util.XlsSender;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class XlsDirectResponse implements DirectFormatResponse {
  
  private final SqlObjectType jt = new SqlObjectType();

  
  @Override
  public void doResponse(HttpServerExchange hse, ResultSet rst) throws Exception {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/vnd.ms-excel"
    );
    XlsSender sender = new XlsSender(hse.getResponseSender());
    sender.openWorkbook();
    this.writeColumns(sender, rst);
    if(rst.next()) {
      while(true) {
        this.writeRow(sender, rst);
        if(rst.next()) sender.nextRow();
        else break;
      }
    }
    sender.closeWorkbook().flush();
  }
  
  
  private void writeRow(XlsSender sender, ResultSet rst) throws SQLException {
    ResultSetMetaData meta = rst.getMetaData();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      sender.put(jt.getObject(rst, i));
    }
  }
  
  
  private void writeColumns(XlsSender sender, ResultSet rst) throws SQLException {
    ResultSetMetaData meta = rst.getMetaData();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      sender.put(meta.getColumnLabel(i));
    }
    sender.nextRow();
  }

}
