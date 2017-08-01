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

package br.com.bb.disec.micro.util;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class URIParam {

  private final String uri;
  
  private final List<String> params;
  
  
  private URIParam(List<String> params) {
    if(params == null || params.isEmpty()) {
      throw new IllegalArgumentException("Bad list params: "+ params);
    }
    this.params = params;
    StringBuilder uri = new StringBuilder("/");
    params.forEach(s->uri.append(s).append("/"));
    this.uri = uri.toString();
  }
  
  
  public URIParam(String uri) {
    if(uri == null || uri.trim().isEmpty() 
        || !uri.trim().contains("/")) {
      throw new IllegalArgumentException("Bad URI: "+ uri);
    }
    this.uri = uri;
    params = Arrays.asList(this.uri.startsWith("/") 
        ? this.uri.substring(1).split("/") 
        : this.uri.split("/")
    );
  }
  
  
  public int length() {
    return params.size() -1;
  }
  
  
  public String getURI() {
    return uri;
  }
  
  
  public String getContext() {
    return params.get(0);
  }
  
  
  public String getParam(int index) {
    if(index < 0 || index > params.size() -2) {
      return null;
    }
    return params.get(index+1);
  }
  
  
  public boolean isNumber(int index) {
    Number n = this.getNumber(index);
    return n != null && n.doubleValue() != Double.NaN;
  }
  
  
  public Number getNumber(int index) {
    try {
      return Double.parseDouble(getParam(index));
    } catch(NumberFormatException e) {
      return Double.NaN;
    }
  }
  
  
  public Boolean getBoolean(int index) {
    return Boolean.parseBoolean(getParam(index));
  }
  
  
  public Object getObject(int index) {
    String param = getParam(index);
    if(param == null) return null;
    try {
      return Double.parseDouble(param);
    } 
    catch(NumberFormatException e) {
      if(param.equalsIgnoreCase("true")
          || param.equalsIgnoreCase("false")) {
        return getBoolean(index);
      }
      return param;
    }
  }
  
  
  public Object[] getObjectArgs() {
    Object[] args = new Object[this.length()];
    for(int i = 0; i < this.length(); i++) {
      args[i] = this.getObject(i);
    }
    return args;
  }
  
  
  public URIParam shift(int pars) {
    for(int i = 1; i < pars && i < params.size(); i++) {
      params.remove(i);
    }
    return new URIParam(params);
  }
  
}
