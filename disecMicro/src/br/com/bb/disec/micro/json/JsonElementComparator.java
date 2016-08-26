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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.function.BiFunction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2016
 */
public class JsonElementComparator implements Comparator<JsonElement> {
  
  private final String type;
  
  private final String col;
  
  private final boolean asc;
  
  private BiFunction<JsonElement,JsonElement,Integer> func;
  
  
  public JsonElementComparator(String type, String col, boolean asc) {
    if(type == null) {
      throw new IllegalArgumentException("Bad Null Type");
    }
    if(col == null) {
      throw new IllegalArgumentException("Bad Null Column Name");
    }
    this.type = type;
    this.col = col;
    this.asc = asc;
  }
  
  
  public String getType() {
    return type;
  }
  
  
  public String getColumn() {
    return col;
  }
  
  
  public boolean isBoolean() {
    return "boolean".equals(type);
  }
  

  public boolean isDateTime() {
    return "date".equals(type);
  }
  

  public boolean isNumber() {
    return "number".equals(type);
  }
  

  public boolean isDecimal() {
    return "decimal".equals(type);
  }
  

  public BiFunction<JsonElement,JsonElement,Integer> getFunction(JsonElement o1, JsonElement o2) {
    BiFunction<JsonElement,JsonElement,Integer> f = null;
    if(isNumber() || isDecimal()) {
      f = (j1,j2)->Double.compare(
          j1.getAsJsonObject().get(col).getAsDouble(), 
          j2.getAsJsonObject().get(col).getAsDouble());
    }
    else if(isBoolean()) {
      f = (j1,j2)->Boolean.compare(
          j1.getAsJsonObject().get(col).getAsBoolean(), 
          j2.getAsJsonObject().get(col).getAsBoolean());
    }
    else if(isDateTime()) {
      f = (j1,j2)->getDate(j1.getAsJsonObject())
          .compareTo(getDate(j2.getAsJsonObject()));
    }
    else {
      f = (j1,j2)->j1.getAsJsonObject()
          .get(col).getAsString()
          .compareTo(j2.getAsJsonObject()
              .get(col).getAsString());
    }
    return f;
  }
  
  
  @Override
  public int compare(JsonElement o1, JsonElement o2) {
    int res = 0;
    if(o1.getAsJsonObject().has(col) 
        && !o2.getAsJsonObject().has(col)) {
      res = -1;
    }
    else if(!o1.getAsJsonObject().has(col) 
        && o2.getAsJsonObject().has(col)) {
      res = 1;
    }
    else {
      if(func == null) {
        func = this.getFunction(o1, o2);
      }
      res = func.apply(o1, o2);
    }
    Gson gson = new Gson();
    System.out.println("* compare: "+ (res * (asc ? 1 : -1)));
    System.out.println("  - "+ gson.toJson(o1));
    System.out.println("  - "+ gson.toJson(o2));
    return res * (asc ? 1 : -1);
  }
  
  
  private Date getDate(JsonObject o) {
    try {
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      return df.parse(o.get(col).getAsString());
    } catch(ParseException e) {
      throw new RuntimeException(e);
    }
  }

}
