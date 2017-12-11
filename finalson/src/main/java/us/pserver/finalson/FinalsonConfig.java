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

package us.pserver.finalson;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import us.pserver.finalson.json.ClassType;
import us.pserver.finalson.json.ColorType;
import us.pserver.finalson.json.DateType;
import us.pserver.finalson.json.JavaType;
import us.pserver.finalson.json.JsonType;
import us.pserver.finalson.json.PathType;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class FinalsonConfig {

  private final Gson gson;
  
  private final boolean useGetters;
  
  private final boolean useMethodAnnotation;
  
  private final ClassLoader loader;
  
  private final List<JsonType> types;
  
  
  public FinalsonConfig() {
    this(new Gson(), FinalsonConfig.class.getClassLoader(), true, false, new CopyOnWriteArrayList<>());
    types.addAll(Arrays.asList(JavaType.values()));
    types.add(new ClassType(loader));
    types.add(new DateType());
    types.add(new PathType());
    types.add(new ColorType());
  }
  
  
  public FinalsonConfig(Gson gson, ClassLoader ldr, boolean useGetters, boolean useMethodAnnotation, List<JsonType> types) {
    this.gson = gson;
    this.useGetters = useGetters;
    this.useMethodAnnotation = useMethodAnnotation;
    this.loader = ldr;
    this.types = types;
  }
  
  
  public FinalsonConfig withGson(Gson gson) {
    return new FinalsonConfig(gson, loader, useGetters, useMethodAnnotation, types);
  }
  
  
  public FinalsonConfig withClassLoader(ClassLoader ldr) {
    return new FinalsonConfig(gson, 
        NotNull.of(ldr).getOrFail("Bad null ClassLoader"), 
        useGetters, useMethodAnnotation, types
    );
  }
  
  
  public FinalsonConfig setUseGetters(boolean use) {
    return new FinalsonConfig(gson, loader, use, useMethodAnnotation, types);
  }
  
  
  public FinalsonConfig setUseMethodAnnotation(boolean use) {
    return new FinalsonConfig(gson, loader, useGetters, use, types);
  }
  
  
  public <T> FinalsonConfig appendJsonType(JsonType<T> type) {
    if(type != null) {
      types.add(type);
    }
    return this;
  }
  
  
  public boolean hasJsonType(Class cls) {
    return types.stream().anyMatch(t->t.is(cls));
  }
  
  
  public Optional<JsonType> getJsonType(Class cls) {
    return types.stream().filter(t->t.is(cls)).findAny();
  }
  
  
  public Gson getGson() {
    return gson;
  }
  
  
  public boolean useGetters() {
    return useGetters;
  }
  
  
  public boolean useMethodAnnotation() {
    return useMethodAnnotation;
  }
  
}
