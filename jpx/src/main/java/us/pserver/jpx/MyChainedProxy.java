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

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import java.net.InetSocketAddress;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import org.littleshoot.proxy.ChainedProxyAdapter;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/08/2018
 */
public class MyChainedProxy extends ChainedProxyAdapter {
  
  public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
  
  private final String proxyHost;
  
  private final int proxyPort;
  
  private final ProxyAuthorization auth;
  
  private final boolean encrypt;
  
  public MyChainedProxy(String proxyAddress, int proxyPort, ProxyAuthorization auth, boolean requiresEncryption) {
    this.proxyHost = Objects.requireNonNull(proxyAddress);
    if(proxyPort <= 0) {
      throw new IllegalArgumentException("Bad proxy port: "+ proxyPort);
    }
    this.proxyPort = proxyPort;
    this.encrypt = requiresEncryption;
    this.auth = auth;
  }
  
  public MyChainedProxy(String proxyAddress, int proxyPort) {
    this(proxyAddress, proxyPort, null, false);
  }
  
  @Override
  public void filterRequest(HttpObject obj) {
    DefaultHttpRequest req = (DefaultHttpRequest) obj;
    if(auth != null) {
      req.headers().add(PROXY_AUTHORIZATION, auth.getProxyAuthorization());
    }
    if(req.headers().contains("Via")) {
      req.headers().remove("Via");
    }
    Logger.debug("chained proxy request: %s", req);
  }
  
  @Override
  public InetSocketAddress getChainedProxyAddress() {
    return new InetSocketAddress(proxyHost, proxyPort);
  }
  
  @Override
  public boolean requiresEncryption() {
    return encrypt;
  }
  
  @Override
  public SSLEngine newSslEngine() {
    try {
      return SSLContext.getDefault().createSSLEngine();
    }
    catch(Exception e) {
      Logger.debug(e);
      throw new RuntimeException(e.toString(), e);
    }
  }

  @Override
  public SSLEngine newSslEngine(String peerHost, int peerPort) {
    try {
      return SSLContext.getDefault().createSSLEngine(peerHost, peerPort);
    }
    catch(Exception e) {
      Logger.debug(e);
      throw new RuntimeException(e.toString(), e);
    }
  }


  @Override
  public String toString() {
    return "MyChainedProxy{" + "proxyHost=" + proxyHost + ", proxyPort=" + proxyPort + ", auth=" + auth + ", encrypt=" + encrypt + '}';
  }

}
