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

import com.mongodb.client.MongoCursor;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/09/2016
 */
public class XlsCachedResponse extends AbstractCachedResponse {

  
  public XlsCachedResponse(List<String> columns) {
    super(columns);
  }
  
  
  @Override
  public void doResponse(HttpServerExchange hse, MongoCursor<Document> cursor) throws Exception {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/vnd.ms-excel"
    );
    hse.startBlocking();
    WritableWorkbook wb = Workbook.createWorkbook(hse.getOutputStream());
    int shnum = 0;
    WritableSheet sh = wb.createSheet(
        "sheet".concat(String.valueOf(shnum)), shnum++
    );
    writeColumns(sh);
    int row = 1;
    while(cursor.hasNext()) {
      writeRow(sh, row++, cursor.next());
      if(row > 65500) {
        row = 1;
        sh = wb.createSheet(
            "sheet".concat(String.valueOf(shnum)), shnum++
        );
        writeColumns(sh);
      }
    }
    wb.write();
    wb.close();
  }
  
  
  private void writeColumns(WritableSheet sheet) throws Exception {
    for(int i = 0; i < columns.size(); i++) {
      sheet.addCell(new Label(i, 0, columns.get(i)));
    }
  }
  
  
  private void writeRow(WritableSheet sheet, int row, Document doc) throws Exception {
    Object[] keys = doc.keySet().toArray();
    int col = 0;
    for(Object key : keys) {
      if("_id".equals(key.toString()) 
          || "created".equals(key.toString())) {
        continue;
      }
      sheet.addCell(createCell(col++, row, doc.get(key)));
    }//for
  }
  
  
  private WritableCell createCell(int col, int row, Object val) {
    WritableCell cell = null;
    if(val == null) {
      cell = new Label(col, row, "");
    }
    else if(Date.class.isAssignableFrom(val.getClass())) {
      cell = new DateTime(col, row, (Date)val);
    }
    else if(Boolean.class.isAssignableFrom(val.getClass())) {
      cell = new jxl.write.Boolean(col, row, (Boolean)val);
    }
    else if(Number.class.isAssignableFrom(val.getClass())) {
      cell = new jxl.write.Number(col, row, ((Number)val).doubleValue());
    }
    else {
      cell = new Label(col, row, Objects.toString(val));
    }
    return cell;
  }
  
}
