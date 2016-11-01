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

import static br.com.bb.disec.micros.util.JsonConstants.ARGS;
import static br.com.bb.disec.micros.util.JsonConstants.FILTER;
import static br.com.bb.disec.micros.util.JsonConstants.FILTERBY;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import static br.com.bb.disec.micros.util.JsonConstants.NAME;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/09/2016
 */
public class JsonHash {

  private final JsonObject json;
  
  private String chash;
  
  private String fhash;
  
  
  public JsonHash(JsonObject json) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    this.json = json;
  }
  
  
  public JsonObject json() { 
    return json;
  }
  
  
  public String collectionHash() {
    if(chash != null) {
      return chash;
    }
    String input = json.get(GROUP).getAsString()
        + json.get(NAME).getAsString();
    if(json.has(ARGS)) {
      JsonArray args = json.getAsJsonArray(ARGS);
      for(int i = 0; i < args.size(); i++) {
        input += Objects.toString(args.get(i));
      }
    }
    return "h"+ DigestUtils.md5Hex(input);
  }
  
  
  public String filterHash() {
    if(fhash != null) {
      return fhash;
    }
    String hash = "0";
    if(json != null 
        && json.has(FILTERBY) 
        && json.has(FILTER)) {
      JsonArray fby = json.getAsJsonArray(FILTERBY);
      JsonArray fil = json.getAsJsonArray(FILTER);
      for(int i = 0; i < fby.size(); i++) {
        hash += fby.get(i).getAsString()
            + Objects.toString(fil.get(i));
      }
    }
    return "h" + DigestUtils.md5Hex(hash);
  }
  
  
  public static JsonHash hash(JsonObject json) {
    return new JsonHash(json);
  }
  
}
