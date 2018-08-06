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
import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.log.Log;
import us.pserver.jpx.log.StdLog;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class ProxyClient {

  
  public static void main(String[] args) {
    int port = 11080;
    //Logger.setLog(new StdLog(StdLog.DEFAULT_LOG_FORMAT, System.out, Log.Level.INFO));
    ProxyAuthorization auth = ProxyAuthorization.of("juno", "32132155");
    MyChainedProxy cpx = new MyChainedProxy("localhost", 6060, auth, false);
    Logger.info("Proxy listening on port: %d", port);
    Logger.info("  with chained proxy: %s", cpx);
    HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
        .withPort(port)
        .withChainProxyManager((r,q)->q.add(cpx))
        .withFiltersSource(new HttpFiltersSourceAdapter() {
          public HttpFilters filterRequest(HttpRequest req, ChannelHandlerContext ctx) {
            return new MyHttpFilter(req);
          }
        })
        .start();
  }
  
}
