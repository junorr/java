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

package br.com.bb.disec.micro.jwt;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2016
 */
public class JWTObject implements JWTElement {
  
  private final JsonObject json;
  
  
  public JWTObject() {
    this(new JsonObject());
  }
  
  
  public JWTObject(JsonObject obj) {
    Objects.requireNonNull(obj, "Bad Null JsonObject");
    this.json = obj;
  }
  
  
  public JsonObject json() {
    return json;
  }
  
  
  public JsonElement get(String key) {
    Objects.requireNonNull(key, "Bad Null Key");
    return json.get(key);
  }
  
  
  public String getString(String key) {
    JsonElement el = get(key);
    return (el == null ? null : el.getAsString());
  }
  
  
  public JWTObject put(String key, Boolean val) {
    Objects.requireNonNull(key, "Bad Null Key");
    Objects.requireNonNull(val, "Bad Null Value");
    json.addProperty(key, val);
    return this;
  }
  
  
  
  public JWTObject put(String key, String val) {
    Objects.requireNonNull(key, "Bad Null Key");
    Objects.requireNonNull(val, "Bad Null Value");
    json.addProperty(key, val);
    return this;
  }
  
  
  public JWTObject put(String key, Character val) {
    Objects.requireNonNull(key, "Bad Null Key");
    Objects.requireNonNull(val, "Bad Null Value");
    json.addProperty(key, val);
    return this;
  }
  
  
  public JWTObject put(String key, Number val) {
    Objects.requireNonNull(key, "Bad Null Key");
    Objects.requireNonNull(val, "Bad Null Value");
    json.addProperty(key, val);
    return this;
  }
  
  
  public JWTObject put(String key, JsonElement val) {
    Objects.requireNonNull(key, "Bad Null Key");
    Objects.requireNonNull(val, "Bad Null Value");
    json.add(key, val);
    return this;
  }
  
  
  @Override
  public String toJson() {
    return new Gson().toJson(json);
  }
  

  @Override
  public String encodeBase64() {
    return Base64.getEncoder()
        .encodeToString(this.toJson()
            .getBytes(StandardCharsets.UTF_8)
        );
  }
  
  
  public static JWTObject fromBase64(String b64) {
    Objects.requireNonNull(b64, "Bad Null Base64 String");
    JWTObject obj = new JWTObject();
    String dec = new String(
        Base64.getDecoder().decode(b64), 
        StandardCharsets.UTF_8
    );
    return new JWTObject(new Gson().fromJson(dec, JsonObject.class));
  }

}
