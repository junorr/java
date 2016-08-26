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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class JsonResultSet_Orig {
  
  public static final String FCOLUMNS = "columns";
  
  public static final String FROWS = "rows";
  
  public static final String FTOTAL = "total";
  
  public static final String FLIMIT = "limit";
  
  public static final String FDATA = "data";
  
  public static final String FTYPES = "types";
  
  
  private JsonObject json;
  
  private final ResultSet rset;
  
  
  public JsonResultSet_Orig(ResultSet rs) {
    if(rs == null) {
      throw new IllegalArgumentException("Bad Null ResultSet");
    }
    this.rset = rs;
  }
  
  
  public JsonResultSet_Orig(JsonObject json) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    this.json = json;
    this.rset = null;
  }
  
  
  public JsonObject getJsonObject() throws SQLException {
    if(json == null) {
      this.serialize();
    }
    return json;
  }
  
  
  public String toJson() throws SQLException {
    if(json == null) {
      this.serialize();
    }
    return new GsonBuilder().registerTypeAdapter(
        Double.class, new JsonDouble())
        .setPrettyPrinting()
        .create().toJson(json);
  }
  
  
  public String toPrettyPrintJson() throws SQLException {
    if(json == null) {
      this.serialize();
    }
    return new GsonBuilder().registerTypeAdapter(
        Double.class, new JsonDouble())
        .setPrettyPrinting()
        .create().toJson(json);
  }
  
  
  private JsonResultSet_Orig serialize() throws SQLException {
    if(rset == null && json != null) {
      return this;
    }
    try {
      json = new JsonObject();
      JsonArray data = new JsonArray();
      ResultSetMetaData meta = rset.getMetaData();
      int cols = meta.getColumnCount();
      json.add(FCOLUMNS, this.getColumns(meta));
      json.add(FTYPES, this.getTypes(meta));
      while(rset.next()) {
        JsonObject entry = new JsonObject();
        for(int i = 1; i <= cols; i++) {
          String label = meta.getColumnLabel(i);
          if(rset.getObject(i) != null) {
            entry.add(label, getElement(rset, i));
          }
        }
        data.add(entry);
      }
      json.addProperty(FROWS, data.size());
      json.addProperty(FTOTAL, data.size());
      json.add(FDATA, data);
    }
    catch(Exception ex) {
      throw new SQLException(ex.getMessage(), ex);
    }
    finally {
      rset.close();
    }
    return this;
  }
  
  
  public int getRows() {
    int rows = 0;
    if(json != null) {
      rows = json.get(FROWS).getAsInt();
    }
    return rows;
  }
  
  
  public int getColumns() {
    int cols = 0;
    if(json != null) {
      cols = json.get(FCOLUMNS).getAsJsonArray().size();
    }
    return cols;
  }
  
  
  public JsonResultSet_Orig filter(int col, String value) {
    if(col >= 0 
        && col < getColumns() 
        && value != null 
        && !value.trim().isEmpty()) 
    {
      List<JsonElement> list = this.toList(json.getAsJsonArray(FDATA));
      JsonArray array = new JsonArray();
      String scol = json.getAsJsonArray(FCOLUMNS)
          .get(col).getAsString();
      list.stream().filter(e->
          e.getAsJsonObject().get(scol)
              .getAsString().toLowerCase()
              .startsWith(value.toLowerCase())
      ).forEach(array::add);
      JsonObject obj = new JsonObject();
      obj.addProperty("by", col);
      obj.addProperty("value", value);
      json.add("filter", obj);
      json.addProperty(FROWS, array.size());
      json.add(FDATA, array);
    }
    return this;
  }
  
  
  public JsonResultSet_Orig sort(int col, boolean asc) {
    if(col >= 0 && col < getColumns()) {
      List<JsonElement> list = toList(json.getAsJsonArray(FDATA));
      JsonArray array = new JsonArray();
      String type = json.getAsJsonArray(FTYPES)
          .get(col).getAsString();
      String scol = json.getAsJsonArray(FCOLUMNS)
          .get(col).getAsString();
      list.stream()
          .sorted(new JsonElementComparator(type, scol, asc))
          .forEach(array::add);
      JsonObject obj = new JsonObject();
      obj.addProperty("by", col);
      obj.addProperty("asc", asc);
      json.add("sort", obj);
      json.add(FDATA, array);
    }
    return this;
  }
  
  
  public JsonResultSet_Orig limit(int[] limit) {
    List<JsonElement> list = this.toList(json.getAsJsonArray(FDATA));
    JsonArray array = new JsonArray();
    list.stream()
        .skip(limit[0])
        .limit(limit[1])
        .forEach(array::add);
    JsonArray alim = new JsonArray();
    alim.add(limit[0]);
    alim.add(limit[1]);
    json.add(FLIMIT, alim);
    json.addProperty(FROWS, array.size());
    json.add(FDATA, array);
    return this;
  }
  
  
  private List<JsonElement> toList(JsonArray a) {
    List<JsonElement> list = new ArrayList<>(a.size());
    a.forEach(list::add);
    return list;
  }
  
  
  private JsonArray toJsonArray(List<JsonElement> l) {
    JsonArray a = new JsonArray();
    l.forEach(a::add);
    return a;
  }
  
  
  private JsonArray getColumns(ResultSetMetaData meta) throws SQLException {
    JsonArray array = new JsonArray();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      array.add(meta.getColumnLabel(i));
    }
    return array;
  }
  
  
  public JsonArray getTypes(ResultSetMetaData meta) throws SQLException {
    JsonArray types = new JsonArray();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      String type = "string";
      if(isBoolean(meta.getColumnType(i))) {
        type = "boolean";
      }
      else if(isDateTime(meta.getColumnType(i))) {
        type = "date";
      }
      else if(isDecimal(meta.getColumnType(i))) {
        type = "decimal";
      }
      else if(isNumber(meta.getColumnType(i))) {
        type = "number";
      }
      types.add(type);
    }
    return types;
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
