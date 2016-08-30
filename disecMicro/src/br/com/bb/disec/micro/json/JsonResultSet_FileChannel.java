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

import br.com.bb.disec.micro.util.JsonFileChannel;
import br.com.bb.disec.micro.util.JsonFileWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class JsonResultSet_FileChannel {
  /*
  public static final String FCOLUMNS = "columns";
  
  public static final String FROWS = "rows";
  
  public static final String FTOTAL = "total";
  
  public static final String FLIMIT = "limit";
  
  public static final String FDATA = "data";
  
  public static final String FTYPES = "types";
  
  
  private final JsonFileChannel channel;
  
  private JsonObject json;
  
  private int count, columns;
  
  private final ResultSet rset;
  
  
  public JsonResultSet_FileChannel(ResultSet rs) throws IOException {
    if(rs == null) {
      throw new IllegalArgumentException("Bad Null ResultSet");
    }
    this.rset = rs;
    channel = JsonFileChannel.temp();
    json = 
  }
  
  
  public JsonResultSet_FileChannel(JsonFileChannel channel) {
    if(channel == null) {
      throw new IllegalArgumentException("Bad Null JsonFileChannel");
    }
    this.rset = null;
    this.channel = channel;
  }
  
  
  public JsonFileWriter getJsonFileWriter() throws SQLException, IOException {
    if(json == null) {
      this.serialize();
    }
    return json;
  }
  
  
  private String measure() throws SQLException {
    JsonObject obj = new JsonObject();
    ResultSetMetaData meta = rset.getMetaData();
    for(int i = 1; i <= columns; i++) {
      if(rset.next()) {
        String label = meta.getColumnLabel(i);
        int type = meta.getColumnType(i);
        if(isBoolean(type)) {
          obj.addProperty(label, rset.getBoolean(i));
        }
        else if(isDateTime(type)) {
          obj.addProperty(label, Objects.toString(
              LocalDateTime.ofInstant(
                  Instant.ofEpochMilli(
                      rset.getTimestamp(i).getTime()), 
                  ZoneId.systemDefault()
              )
          ));
        }
        else if(isDecimal(type)) {
          obj.addProperty(label, rset.getDouble(i));
        }
        else if(isNumber(type)) {
          obj.addProperty(label, rset.getLong(i));
        }
        else {
          obj.addProperty(label, rset.getString(i));
        }
      }//next
    }//for
    return new Gson().toJson(obj);
  }
  
  
  private JsonObject createHeader() {
    JsonObject obj = new JsonObject();
    obj.addProperty("json-start", Long.MAX_VALUE);
    obj.addProperty("data-start", Long.MAX_VALUE);
    obj.addProperty("entries-count", Long.MAX_VALUE);
    obj.addProperty("entry-length", Long.MAX_VALUE);
    obj.addProperty("trash", "0");
    return obj;
  }
  
  
  private JsonResultSet_FileChannel serialize() throws SQLException, IOException {
    if(rset == null && json != null) {
      return this;
    }
    try {
      json = JsonFileWriter.temp();
      ResultSetMetaData meta = rset.getMetaData();
      columns = meta.getColumnCount();
      json.startObject()
          .startArray(FCOLUMNS);
      this.putColumns(meta);
      json.endArray()
          .nextElement()
          .startArray(FTYPES);
      this.putTypes(meta);
      json.endArray()
          .nextElement()
          .startArray(FDATA);
      count = 0;
      while(rset.next()) {
        if(count > 0) {
          json.nextElement();
        }
        json.startObject();
        for(int i = 1; i <= columns; i++) {
          if(rset.getObject(i) != null) {
            this.putElement(rset, i);
            if(i < columns) json.nextElement();
          }
        }
        json.endObject();
        count++;
      }
      json.endArray()
          .nextElement()
          .put(FROWS, count)
          .nextElement()
          .put(FTOTAL, count)
          .endObject();
    }
    finally {
      rset.close();
    }
    return this;
  }
  
  
  public int getRows() {
    return count;
  }
  
  
  public int getColumns() {
    return columns;
  }
  
  
  public JsonResultSet_FileChannel filter(int col, String value) {
    if(col >= 0 
        && col < getColumns() 
        && value != null 
        && !value.trim().isEmpty()) 
    {
      /*
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
  
  
  public JsonResultSet_FileChannel sort(int col, boolean asc) {
    if(col >= 0 && col < getColumns()) {
      /*
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
  
  
  public JsonResultSet_FileChannel limit(int[] limit) {
    /*
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
  
  
  private void putColumns(ResultSetMetaData meta) throws SQLException, IOException {
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      json.put(meta.getColumnLabel(i));
      if(i < cols) json.nextElement();
    }
  }
  
  
  public void putTypes(ResultSetMetaData meta) throws SQLException, IOException {
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
      json.put(type);
      if(i < cols) json.nextElement();
    }
  }
  
  
  private void putElement(ResultSet r, int col) throws SQLException, IOException {
    int type = r.getMetaData().getColumnType(col);
    String label = r.getMetaData().getColumnLabel(col);
    if(isNumber(type)) {
      json.put(label, r.getLong(col));
    } 
    else if(isDecimal(type)) {
      DecimalFormat df = new DecimalFormat("0.00########");
      df.setNegativePrefix("-");
      df.getDecimalFormatSymbols().setDecimalSeparator('.');
      json.put(label, df.format(r.getDouble(col)));
    }
    else if(isBoolean(type)) {
      json.put(label, r.getBoolean(col));
    }
    else if(isDateTime(type)) {
      json.put(label, r.getTimestamp(col));
    }
    else {
      json.put(label, r.getString(col));
    }
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
  */
}
