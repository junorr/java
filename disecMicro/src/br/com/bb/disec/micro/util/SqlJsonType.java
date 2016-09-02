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

package br.com.bb.disec.micro.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2016
 */
public class SqlJsonType {

  
  public JsonElement getElement(ResultSet r, int col) throws SQLException {
    JsonElement elt;
    int type = r.getMetaData().getColumnType(col);
    if(isNumber(type)) {
      elt = new JsonPrimitive(r.getLong(col));
    } 
    else if(isDecimal(type)) {
      elt = new JsonPrimitive(r.getDouble(col));
    }
    else if(isBoolean(type)) {
      elt = new JsonPrimitive(r.getBoolean(col));
    }
    else if(isDateTime(type)) {
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      Timestamp ts = r.getTimestamp(col);
      elt = new JsonPrimitive(df.format(new Date(ts.getTime())));
    }
    else {
      elt = new JsonPrimitive(r.getString(col));
    }
    return elt;
  }
  
  
  public Object getObject(ResultSet r, int col) throws SQLException {
    Object obj;
    int type = r.getMetaData().getColumnType(col);
    if(isNumber(type)) {
      obj = r.getLong(col);
    } 
    else if(isDecimal(type)) {
      obj = r.getDouble(col);
    }
    else if(isBoolean(type)) {
      obj = r.getBoolean(col);
    }
    else if(isDateTime(type)) {
      obj = DateFormatter.of(r.getTimestamp(col)).toIsoDateTime();
    }
    else {
      obj = r.getString(col);
    }
    return obj;
  }
  
  
  public boolean isNumber(int type) {
    return type == Types.BIGINT
        || type == Types.INTEGER
        || type == Types.NUMERIC
        || type == Types.SMALLINT
        || type == Types.TINYINT;
  }
  
  
  public boolean isDecimal(int type) {
    return type == Types.DECIMAL
        || type == Types.DOUBLE
        || type == Types.FLOAT
        || type == Types.REAL;
  }
  
  
  public boolean isDateTime(int type) {
    return type == Types.DATE
        || type == Types.TIME
        || type == Types.TIMESTAMP_WITH_TIMEZONE
        || type == Types.TIME_WITH_TIMEZONE
        || type == Types.TIMESTAMP;
  }
  
  
  public boolean isBoolean(int type) {
    return type == Types.BOOLEAN;
  }
  
}
