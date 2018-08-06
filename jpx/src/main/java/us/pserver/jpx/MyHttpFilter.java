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

package us.pserver.jpx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFiltersAdapter;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class MyHttpFilter extends HttpFiltersAdapter {

  public MyHttpFilter(HttpRequest originalRequest) {
    super(originalRequest);
  }


  public MyHttpFilter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
    super(originalRequest, ctx);
  }

  
  @Override
  public HttpResponse clientToProxyRequest(HttpObject obj) {
    //Logger.debug("----------------------------------------");
    Logger.debug("Request Received: %s", obj);
    //Logger.debug("obj.class = %s", obj.getClass());
    DefaultHttpRequest req = (DefaultHttpRequest) obj;
    return null;
  }
  
  
  @Override
  public HttpObject serverToProxyResponse(HttpObject obj) {
    //Logger.debug("----------------------------------------");
    //Logger.debug("Request Received: %s", obj);
    Logger.debug("obj.class = %s", obj.getClass());
    return obj;
  }
  
}
