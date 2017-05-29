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

package br.com.bb.disec.micros.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public class JsonTransformer {

  
  public Object toDocumentQuery(String str) {
    Object val = null;
    if(str.startsWith("!=")) {
      str = str.substring(2);
      val = new Document("$ne", toDocumentQuery(str));
    }
    else if(str.startsWith(">=")) {
      str = str.substring(2);
      val = new Document("$gte", toDocumentQuery(str));
    }
    else if(str.startsWith("<=")) {
      str = str.substring(2);
      val = new Document("$lte", toDocumentQuery(str));
    }
    else if(str.startsWith(">")) {
      str = str.substring(1);
      val = new Document("$gt", toDocumentQuery(str));
    }
    else if(str.startsWith("<")) {
      str = str.substring(1);
      val = new Document("$lt", toDocumentQuery(str));
    }
    else if(str.startsWith("=")) {
      str = str.substring(1);
      val = new Document("$eq", toDocumentQuery(str));
    }
    else if("true".equalsIgnoreCase(str)
        || "false".equalsIgnoreCase(str)) {
      val = Boolean.parseBoolean(str);
    }
    else if(DateParser.isDateString(str)) {
      val = DateParser.parser(str).toDate();
    }
    else {
      try {
      Double d = Double.parseDouble(str);
      val = (str.contains(".") ? d.doubleValue() : d.longValue());
      } catch(NumberFormatException e) {
        val = Pattern.compile("^"+ Pattern.quote(str)+ ".*", Pattern.CASE_INSENSITIVE);
      }
    }
    return val;
  }
  
  
  public Object toJsonValue(String str) {
    Object val = null;
    if("true".equalsIgnoreCase(str)
        || "false".equalsIgnoreCase(str)) {
      val = Boolean.parseBoolean(str);
    }
    else if(DateParser.isDateString(str)) {
      val = DateParser.parser(str).toDate();
    }
    else {
      try {
      Double d = Double.parseDouble(str);
      val = (str.contains(".") ? d.doubleValue() : d.longValue());
      } catch(NumberFormatException e) {
        val = str;
      }
    }
    return val;
  }
  
  
  public List toList(JsonArray array) {
    return (List) JSON.parse(new Gson().toJson(array));
  }
  
  
  public Document toDocument(JsonObject jo) {
    BasicDBObject dob = (BasicDBObject) JSON.parse(new Gson().toJson(jo));
    return new Document(dob);
  }
  
  
  public JsonObject toJsonObject(Document doc) {
    return new JsonParser().parse(JSON.serialize(doc)).getAsJsonObject();
  }
  
  
}
