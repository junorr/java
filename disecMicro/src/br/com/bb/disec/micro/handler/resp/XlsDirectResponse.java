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

import br.com.bb.disec.micro.db.SqlObjectType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class XlsDirectResponse implements DirectFormatResponse {
  
  private final SqlObjectType sot = new SqlObjectType();

  
  @Override
  public void doResponse(HttpServerExchange hse, ResultSet rst) throws Exception {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, "application/vnd.ms-excel"
    );
    hse.startBlocking();
    WritableWorkbook wb = Workbook.createWorkbook(hse.getOutputStream());
    int shnum = 0;
    WritableSheet sh = wb.createSheet(
        "sheet".concat(String.valueOf(shnum)), shnum++
    );
    writeColumns(sh, rst);
    int row = 1;
    while(rst.next()) {
      writeRow(sh, row++, rst);
      if(row > 65500) {
        row = 1;
        sh = wb.createSheet(
            "sheet".concat(String.valueOf(shnum)), shnum++
        );
        writeColumns(sh, rst);
      }
    }
    wb.write();
    wb.close();
  }
  
  
  private void writeColumns(WritableSheet sheet, ResultSet rst) throws Exception {
    ResultSetMetaData meta = rst.getMetaData();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      sheet.addCell(new Label(i-1, 0, meta.getColumnLabel(i)));
    }
  }

  
  private void writeRow(WritableSheet sheet, int row, ResultSet rst) throws Exception {
    int ncols = rst.getMetaData().getColumnCount();
    for(int i = 0; i < ncols; i++) {
      sheet.addCell(createCell(i, row, rst));
    }
  }
  
  
  private WritableCell createCell(int col, int row, ResultSet rst) throws SQLException {
    ResultSetMetaData meta = rst.getMetaData();
    int coltype = meta.getColumnType(col+1);
    WritableCell cell = null;
    if(rst.getObject(col+1) == null) {
      cell = new Label(col, row, "");
    }
    else if(sot.isDateTime(coltype)) {
      cell = new DateTime(col, row, new Date(rst.getDate(col+1).getTime()));
    }
    else if(sot.isBoolean(coltype)) {
      cell = new jxl.write.Boolean(col, row, rst.getBoolean(col+1));
    }
    else if(sot.isNumber(coltype) || sot.isDecimal(coltype)) {
      cell = new jxl.write.Number(col, row, rst.getDouble(col+1));
    }
    else {
      cell = new Label(col, row, rst.getString(col+1));
    }
    return cell;
  }
  
}
