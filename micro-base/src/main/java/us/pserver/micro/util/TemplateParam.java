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

import io.undertow.server.HttpServerExchange;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import us.pserver.orb.Orb;
import us.pserver.orb.TypedMap;
import us.pserver.orb.TypedStrings;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/03/2018
 */
public class TemplateParam {

  private final URIParam pars;
  
  private final List<String> template;
  
  
  public TemplateParam(URIParam pars, String template) {
    this.pars = Match.notNull(pars)
        .getOrFail("Bad null URIParam");
    String stemp = Match.notEmpty(template)
            .getOrFail("Bad template string");
    if(stemp.startsWith("/")) {
      stemp = stemp.substring(1);
    }
    if(stemp.endsWith("/")) {
      stemp = stemp.substring(0, stemp.length() -1);
    }
    this.template = Arrays.asList(stemp.split("/"));
  }
  
  
  public boolean contains(String param) {
    return template.contains(param);
  }
  
  
  public Optional<String> get(String param) {
    int idx = template.indexOf(param);
    return Optional.ofNullable(idx < 0 ? null : pars.getParam(idx));
  }
  
  
  public TypedMap getAsTypedMap() {
    return new TypedMap(getParamsMap(), new TypedStrings());
  }
  
  
  private boolean isURLEncoded(String str) {
    return str != null 
        && !str.trim().isEmpty() 
        && str.contains("%");
  }
  
  
  private String getURLDecoded(String str) {
    try {
      return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
    }
    catch(UnsupportedEncodingException e) {
      return str;
    }
  }
  
  
  private Map<String,Object> getParamsMap() {
    Map<String,Object> parMap = new TreeMap<>();
    for(int i = 0; i < template.size(); i++) {
      if(pars.length() > i) {
        String value = pars.getParam(i);
        if(isURLEncoded(value)) {
          value = getURLDecoded(value);
        }
        parMap.put(template.get(i), value);
      }
    }
    return parMap;
  }
  
  
  public <T> T getAsMappedProxy(Class<T> cls) {
    Map<String,Object> parMap = new TreeMap<>();
    for(int i = 0; i < template.size(); i++) {
      parMap.put(template.get(i), pars.getParam(i));
    }
    return new Orb(getParamsMap(), new TypedStrings(), Orb.GETTER_AS_DASH_KEY).create(cls);
  }
  
  
  public static TemplateParam of(String template, HttpServerExchange hse) {
    return new URIParam(hse.getRequestURI()).getTemplateParam(template);
  }
  
}
