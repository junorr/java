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

package us.pserver.micro.http;

import io.undertow.util.HttpString;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/03/2018
 */
public class HttpMethod {
  
  public static final String DELETE_STRING = "DELETE";
  
  public static final String GET_STRING = "GET";
  
  public static final String HEAD_STRING = "HEAD";
  
  public static final String OPTIONS_STRING = "OPTIONS";
  
  public static final String PATCH_STRING = "PATCH";
  
  public static final String POST_STRING = "POST";
  
  public static final String PUT_STRING = "PUT";
  
  
  public static final HttpMethod DELETE = new HttpMethod(DELETE_STRING);
  
  public static final HttpMethod GET = new HttpMethod(GET_STRING);
  
  public static final HttpMethod HEAD = new HttpMethod(HEAD_STRING);
  
  public static final HttpMethod OPTIONS = new HttpMethod(OPTIONS_STRING);
  
  public static final HttpMethod PATCH = new HttpMethod(PATCH_STRING);
  
  public static final HttpMethod POST = new HttpMethod(POST_STRING);
  
  public static final HttpMethod PUT = new HttpMethod(PUT_STRING);
  

  private final HttpString method;
  
  public HttpMethod(String method) {
    this(new HttpString(
        Match.notEmpty(method).getOrFail("Bad null method String"))
    );
  }
  
  public HttpMethod(HttpString method) {
    this.method = Match.notNull(method).getOrFail("Bad null HttpString");
  }
  
  
  public HttpString toHttpString() {
    return method;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + Objects.hashCode(this.method);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final HttpMethod other = (HttpMethod) obj;
    if (!Objects.equals(this.method, other.method)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return method.toString();
  }
  
  
  public static HttpMethod fromString(String meth) {
    switch(Match.notEmpty(meth).getOrFail("Bad empty method string").toUpperCase()) {
      case DELETE_STRING:
        return DELETE;
      case HEAD_STRING:
        return HEAD;
      case OPTIONS_STRING:
        return OPTIONS;
      case PATCH_STRING:
        return PATCH;
      case POST_STRING:
        return POST;
      case PUT_STRING:
        return PUT;
      default:
        return GET;
    }
  }
  
  
  public static List<HttpMethod> listMethods() {
    return Arrays.asList(methodsArray());
  }

  
  public static HttpMethod[] methodsArray() {
    return new HttpMethod[] {DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT};
  }

}
