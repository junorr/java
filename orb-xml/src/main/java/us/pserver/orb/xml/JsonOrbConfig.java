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

package us.pserver.orb.xml;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import us.pserver.orb.TypedStrings;
import us.pserver.orb.OrbConfiguration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/01/2018
 */
public class JsonOrbConfig implements OrbConfiguration {
  
  private final Map<String,Object> map;
  
  public JsonOrbConfig(Gson gson, String str) {
    this.map = gson.fromJson(str, Map.class);
  }
  
  public JsonOrbConfig(Gson gson, Reader reader) {
    this.map = gson.fromJson(reader, Map.class);
  }
  
  @Override
  public TypedStrings getSupportedTypes() {
    return null;
  }

  @Override
  public Map<String, Object> getValuesMap() {
    return map;
  }

  @Override
  public Function<Method, String> getMethodToKeyFunction() {
    return null;
  }
  
  
  
  public static JsonOrbConfig of(String json) {
    Gson gson = new Gson();
    return new JsonOrbConfig(gson, json);
  }
  
  public static JsonOrbConfig of(Gson gson, String json) {
    return new JsonOrbConfig(gson, json);
  }
  
  public static JsonOrbConfig of(Reader json) {
    Gson gson = new Gson();
    return new JsonOrbConfig(gson, json);
  }
  
  public static JsonOrbConfig of(Gson gson, Reader json) {
    return new JsonOrbConfig(gson, json);
  }
  
  public static JsonOrbConfig of(Path json) throws IOException {
    Gson gson = new Gson();
    Reader rdr = Files.newBufferedReader(json);
    return new JsonOrbConfig(gson, rdr);
  }
  
  public static JsonOrbConfig of(Gson gson, Path json) throws IOException {
    Reader rdr = Files.newBufferedReader(json);
    return new JsonOrbConfig(gson, rdr);
  }
  
}
