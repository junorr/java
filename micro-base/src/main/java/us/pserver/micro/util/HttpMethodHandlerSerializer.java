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

package us.pserver.micro.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.undertow.server.HttpHandler;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import us.pserver.micro.http.HttpMethod;
import us.pserver.micro.http.HttpMethodHandler;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class HttpMethodHandlerSerializer implements JsonDeserializer<HttpMethodHandler>, JsonSerializer<HttpMethodHandler> {
  
  public static final String HTTP_METHOD = "http.method";
  
  public static final String HTTP_HANDLER = "http.handler";
  
  public List<HttpMethod> getJsonHttpMethods(JsonObject job) {
    if(!job.has(HTTP_METHOD)) {
      throw new JsonParseException(String.format(
          "Missing mandatory property {\"%s\"}", HTTP_METHOD));
    }
    List<HttpMethod> meths = new ArrayList<>();
    if(job.get(HTTP_METHOD).isJsonArray()) {
      job.getAsJsonArray(HTTP_METHOD).forEach(e->
        meths.add(HttpMethod.fromString(e.getAsString()))
      );
    }
    else {
      meths.add(HttpMethod.fromString(job.get(HTTP_METHOD).getAsString()));
    }
    return meths;
  }

  @Override
  public HttpMethodHandler deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
    JsonElement handlerType;
    List<HttpMethod> meths;
    if(je.isJsonObject()) {
      JsonObject job = (JsonObject) je;
      meths = getJsonHttpMethods(job);
      handlerType = job.get(HTTP_HANDLER);
    }
    else {
      meths = HttpMethod.listMethods();
      handlerType = je;
    }
    Class cls = jdc.deserialize(handlerType, Class.class);
    HttpHandler handler = new HttpHandlerInstance(cls).create();
    return new HttpMethodHandler(meths, handler);
  }

  @Override
  public JsonElement serialize(HttpMethodHandler handler, Type type, JsonSerializationContext jsc) {
    JsonObject job = new JsonObject();
    JsonElement handlerType = jsc.serialize(handler.getHttpHandler().getClass());
    JsonArray methods = new JsonArray();
    handler.getHttpMethods().forEach(m->methods.add(new JsonPrimitive(m.toString())));
    job.add(HTTP_HANDLER, handlerType);
    job.add(HTTP_METHOD, methods);
    return job;
  }

}
