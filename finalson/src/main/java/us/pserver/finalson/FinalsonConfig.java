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
  
  
  public FinalsonConfig() {
    this(new Gson(), FinalsonConfig.class.getClassLoader(), true, false);
  }
  
  
  public FinalsonConfig(Gson gson, ClassLoader ldr, boolean useGetters, boolean useMethodAnnotation) {
    this.gson = gson;
    this.useGetters = useGetters;
    this.useMethodAnnotation = useMethodAnnotation;
    this.loader = ldr;
  }
  
  
  public FinalsonConfig withGson(Gson gson) {
    return new FinalsonConfig(gson, loader, useGetters, useMethodAnnotation);
  }
  
  
  public FinalsonConfig withClassLoader(ClassLoader ldr) {
    return new FinalsonConfig(gson, 
        NotNull.of(ldr).getOrFail("Bad null ClassLoader"), 
        useGetters, useMethodAnnotation
    );
  }
  
  
  public FinalsonConfig useGetters(boolean use) {
    return new FinalsonConfig(gson, loader, use, useMethodAnnotation);
  }
  
  
  public FinalsonConfig useMethodAnnotation(boolean use) {
    return new FinalsonConfig(gson, loader, useGetters, use);
  }
  
  
  public Gson getGson() {
    return gson;
  }
  
  
  public boolean isUseGetters() {
    return useGetters;
  }
  
  
  public boolean isUseMethodAnnotation() {
    return useMethodAnnotation;
  }
  
}
