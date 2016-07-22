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

package br.com.bb.disec.micro.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class JsonResultSet {
  
  private final JsonObject json;
  
  
  public JsonResultSet(ResultSet rs) throws SQLException {
    json = this.serialize(rs);
  }
  
  
  public JsonObject getJson() {
    return json;
  }
  
  
  public String toJson() {
    return new Gson().toJson(json);
  }
  
  
  public String toPrettyPrintJson() {
    return new GsonBuilder().registerTypeAdapter(
        Double.class, new JsonDouble())
        .setPrettyPrinting()
        .create().toJson(json);
  }
  
  
  private JsonObject serialize(ResultSet r) throws SQLException {
    try {
      JsonObject obj = new JsonObject();
      JsonArray data = new JsonArray();
      ResultSetMetaData meta = r.getMetaData();
      int cols = meta.getColumnCount();
      obj.addProperty("columns", cols);
      while(r.next()) {
        JsonObject entry = new JsonObject();
        for(int i = 1; i <= cols; i++) {
          String label = meta.getColumnLabel(i);
          entry.add(label, getElement(r, i));
        }
        data.add(entry);
      }
      obj.addProperty("rows", data.size());
      obj.add("data", data);
      return obj;
    }
    catch(Exception ex) {
      throw new SQLException(ex.getMessage(), ex);
    }
    finally {
      r.close();
    }
  }
  
  
  private JsonElement getElement(ResultSet r, int col) throws SQLException {
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
  
  
  private boolean isNumber(int type) {
    return type == Types.BIGINT
        || type == Types.INTEGER
        || type == Types.NUMERIC
        || type == Types.SMALLINT
        || type == Types.TINYINT;
  }
  
  
  private boolean isDecimal(int type) {
    return type == Types.DECIMAL
        || type == Types.DOUBLE
        || type == Types.FLOAT
        || type == Types.REAL;
  }
  
  
  private boolean isDateTime(int type) {
    return type == Types.DATE
        || type == Types.TIME
        || type == Types.TIMESTAMP_WITH_TIMEZONE
        || type == Types.TIME_WITH_TIMEZONE
        || type == Types.TIMESTAMP;
  }
  
  
  private boolean isBoolean(int type) {
    return type == Types.BOOLEAN;
  }
  
}
