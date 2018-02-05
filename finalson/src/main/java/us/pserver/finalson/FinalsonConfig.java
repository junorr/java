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
import us.pserver.finalson.mapping.ArrayMapping;
import us.pserver.finalson.mapping.ClassMapping;
import us.pserver.finalson.mapping.ColorMapping;
import us.pserver.finalson.mapping.DateMapping;
import us.pserver.finalson.mapping.InstantMapping;
import us.pserver.finalson.mapping.JavaPrimitive;
import us.pserver.finalson.mapping.LocalDateTimeMapping;
import us.pserver.finalson.mapping.PathMapping;
import us.pserver.finalson.mapping.TypeMapping;
import us.pserver.finalson.mapping.ZonedDateTimeMapping;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class FinalsonConfig {

  private final Gson gson;
  
  private final boolean useGetters;
  
  private final Class methodAnnotation;
  
  private final ClassLoader loader;
  
  private final List<TypeMapping> types;
  
  
  public FinalsonConfig() {
    this(new Gson(), FinalsonConfig.class.getClassLoader(), true, null, new CopyOnWriteArrayList<>());
    types.addAll(Arrays.asList(JavaPrimitive.values()));
    types.add(new ClassMapping(loader));
    types.add(new DateMapping());
    types.add(new PathMapping());
    types.add(new ColorMapping());
    types.add(new InstantMapping());
    types.add(new LocalDateTimeMapping());
    types.add(new ZonedDateTimeMapping());
    types.add(new ArrayMapping(this));
  }
  
  
  public FinalsonConfig(Gson gson, ClassLoader ldr, boolean useGetters, Class methodAnnotation, List<TypeMapping> types) {
    this.gson = gson;
    this.useGetters = useGetters;
    this.methodAnnotation = methodAnnotation;
    this.loader = ldr;
    this.types = types;
  }
  
  
  public FinalsonConfig withGson(Gson gson) {
    return new FinalsonConfig(gson, loader, useGetters, methodAnnotation, types);
  }
  
  
  public FinalsonConfig withClassLoader(ClassLoader ldr) {
    return new FinalsonConfig(gson, 
        Match.notNull(ldr).getOrFail("Bad null ClassLoader"), 
        useGetters, methodAnnotation, types
    );
  }
  
  
  public FinalsonConfig usingGetters(boolean use) {
    return new FinalsonConfig(gson, loader, use, methodAnnotation, types);
  }
  
  
  public FinalsonConfig usingMethodAnnotation(Class methodAnnotation) {
    return new FinalsonConfig(gson, loader, useGetters, methodAnnotation, types);
  }
  
  
  public <T> FinalsonConfig appendTypeMapping(Class type, TypeMapping<T> mapping) {
    if(type != null && mapping != null) {
      Optional<TypeMapping> opt = getTypeMappingFor(type);
      if(opt.isPresent()) types.remove(opt.get());
      types.add(mapping);
    }
    return this;
  }
  
  
  public boolean hasTypeMappingFor(Class cls) {
    return types.stream().anyMatch(t->t.accept(cls));
  }
  
  
  public <T> Optional<TypeMapping<T>> getTypeMappingFor(Class<T> cls) {
    return types.stream()
        .filter(t->t.accept(cls))
        .map(t->(TypeMapping<T>)t)
        .findAny();
  }
  
  
  public Gson getGson() {
    return gson;
  }
  
  
  public boolean isUsingGetters() {
    return useGetters;
  }
  
  
  public boolean isUsingMethodAnnotation() {
    return methodAnnotation != null;
  }
  
  
  public Class getMethodAnnotation() {
    return methodAnnotation;
  }
  
}
