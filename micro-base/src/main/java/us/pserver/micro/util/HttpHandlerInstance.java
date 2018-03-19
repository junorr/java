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

import io.undertow.server.HttpHandler;
import java.lang.reflect.Constructor;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/03/2018
 */
public class HttpHandlerInstance {

  private final Class<HttpHandler> cls;
  
  
  public HttpHandlerInstance(Class<HttpHandler> cls) {
    this.cls = Match.notNull(cls).getOrFail("Bad null Class<HttpHandler>");
  }
  
  
  public Class<HttpHandler> getInstanceClass() {
    return cls;
  }
  
  
  public HttpHandler getInstance() {
    try {
      Constructor<HttpHandler> cct = cls.getDeclaredConstructor(null);
      if(!cct.isAccessible()) {
        cct.setAccessible(true);
      }
      return cct.newInstance(null);
    }
    catch(Exception ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }
  
}
