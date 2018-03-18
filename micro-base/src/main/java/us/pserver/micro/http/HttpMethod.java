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
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/03/2018
 */
public class HttpMethod {
  
  public static final HttpMethod GET = new HttpMethod("GET");
  
  public static final HttpMethod POST = new HttpMethod("POST");
  
  public static final HttpMethod PUT = new HttpMethod("PUT");
  
  public static final HttpMethod DELETE = new HttpMethod("DELETE");
  
  public static final HttpMethod HEAD = new HttpMethod("HEAD");
  
  public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
  

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
  public String toString() {
    return method.toString();
  }
  
}
