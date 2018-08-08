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

import java.util.Objects;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/08/2018
 */
public class ProxyConfiguration {
  
  public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
  
  public static final int DEFAULT_PROXY_PORT = 11080;
  
  
  private final int bufferSize;
  
  private final int proxyPort;
  
  private final String targetUri;
  
  private final String userAgent;
  
  private final String chainedProxyHost;
  
  private final int chainedProxyPort;
  
  private final CryptKey key;
  
  private final ProxyAuthorization auth;
  
  
  public ProxyConfiguration() {
    this(DEFAULT_BUFFER_SIZE, DEFAULT_PROXY_PORT, null, null, null, 0, null, null);
  }
  
  public ProxyConfiguration(int bufferSize, int proxyPort, String targetUri, String userAgent, String chainedProxyHost, int chainedProxyPort, CryptKey key, ProxyAuthorization auth) {
    this.bufferSize = bufferSize;
    this.proxyPort = proxyPort;
    this.targetUri = targetUri;
    this.userAgent = userAgent;
    this.chainedProxyHost = chainedProxyHost;
    this.chainedProxyPort = chainedProxyPort;
    this.key = key;
    this.auth = auth;
  }
  
  
  public ProxyConfiguration withBufferSize(int bfs) {
    if(bfs < 1) throw new IllegalArgumentException("Bad buffer size: "+ bfs);
    return new ProxyConfiguration(bfs, proxyPort, targetUri, userAgent, chainedProxyHost, chainedProxyPort, key, auth);
  }
  
  public ProxyConfiguration withProxyPort(int port) {
    if(port < 1) throw new IllegalArgumentException("Bad proxy port: "+ port);
    return new ProxyConfiguration(bufferSize, port, targetUri, userAgent, chainedProxyHost, chainedProxyPort, key, auth);
  }
  
  public ProxyConfiguration withTargetUri(String target) {
    return new ProxyConfiguration(bufferSize, proxyPort, Objects.requireNonNull(target), userAgent, chainedProxyHost, chainedProxyPort, key, auth);
  }
  
  public ProxyConfiguration withChainedProxyHost(String host) {
    return new ProxyConfiguration(bufferSize, proxyPort, targetUri, userAgent, Objects.requireNonNull(host), chainedProxyPort, key, auth);
  }
  
  public ProxyConfiguration withChainedProxyPort(int port) {
    if(port < 1) throw new IllegalArgumentException("Bad server port: "+ port);
    return new ProxyConfiguration(bufferSize, proxyPort, targetUri, userAgent, chainedProxyHost, port, key, auth);
  }
  
  public ProxyConfiguration withCryptAlgorithm(CryptAlgorithm algo) {
    CryptKey key = CryptKey.createRandomKey(Objects.requireNonNull(algo));
    return new ProxyConfiguration(bufferSize, proxyPort, targetUri, userAgent, chainedProxyHost, chainedProxyPort, key, auth);
  }
  
  public ProxyConfiguration withCryptKey(CryptKey key) {
    return new ProxyConfiguration(bufferSize, proxyPort, targetUri, userAgent, chainedProxyHost, chainedProxyPort, Objects.requireNonNull(key), auth);
  }
  
  public ProxyConfiguration withChainedProxyAuthorization(ProxyAuthorization auth) {
    return new ProxyConfiguration(bufferSize, proxyPort, targetUri, userAgent, chainedProxyHost, chainedProxyPort, key, auth);
  }
  
  
  public int getBufferSize() {
    return bufferSize;
  }
  
  public int getProxyPort() {
    return proxyPort;
  }
  
  public String getTargetUri() {
    return targetUri;
  }
  
  public String getUserAgent() {
    return userAgent;
  }
  
  public String getChainedProxyHost() {
    return chainedProxyHost;
  }
  
  public int getChainedProxyPort() {
    return chainedProxyPort;
  }
  
  public CryptKey getCryptKey() {
    return key;
  }
  
  public ProxyAuthorization getChainedProxyAuthorization() {
    return auth;
  }
  
  
  public ProxyConfiguration validateBufferSize() {
    if(bufferSize < 1)
      throw new IllegalStateException("Bad buffer size: "+ bufferSize);
    return this;
  }
  
  public ProxyConfiguration validateProxyPort() {
    if(proxyPort < 1)
      throw new IllegalStateException("Bad proxy port: "+ proxyPort);
    return this;
  }
  
  public ProxyConfiguration validateTargetUri() {
    if(targetUri == null || targetUri.trim().isEmpty())
      throw new IllegalStateException("Bad target URI: "+ targetUri);
    return this;
  }
  
  public ProxyConfiguration validateChainedProxyHost() {
    if(chainedProxyHost == null || chainedProxyHost.trim().isEmpty())
      throw new IllegalStateException("Bad chained proxy host: "+ chainedProxyHost);
    return this;
  }
  
  public ProxyConfiguration validateChainedProxyPort() {
    if(chainedProxyPort < 1)
      throw new IllegalStateException("Bad chained proxy port: "+ chainedProxyPort);
    return this;
  }
  
  public ProxyConfiguration validateCryptAlgorithm() {
    if(key == null)
      throw new IllegalStateException("Bad crypt algorithm: "+ key);
    return this;
  }
  
  public boolean hasChainedProxyAuthorization() {
    return auth != null;
  }
  
  public boolean hasUserAgent() {
    return userAgent != null;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + this.proxyPort;
    hash = 61 * hash + this.bufferSize;
    hash = 61 * hash + Objects.hashCode(this.targetUri);
    hash = 61 * hash + Objects.hashCode(this.userAgent);
    hash = 61 * hash + Objects.hashCode(this.chainedProxyHost);
    hash = 61 * hash + this.chainedProxyPort;
    hash = 61 * hash + Objects.hashCode(this.key);
    hash = 61 * hash + Objects.hashCode(this.auth);
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
    final ProxyConfiguration other = (ProxyConfiguration) obj;
    if (this.bufferSize != other.bufferSize) {
      return false;
    }
    if (this.proxyPort != other.proxyPort) {
      return false;
    }
    if (this.chainedProxyPort != other.chainedProxyPort) {
      return false;
    }
    if (!Objects.equals(this.targetUri, other.targetUri)) {
      return false;
    }
    if (!Objects.equals(this.userAgent, other.userAgent)) {
      return false;
    }
    if (!Objects.equals(this.chainedProxyHost, other.chainedProxyHost)) {
      return false;
    }
    if (this.key != other.key) {
      return false;
    }
    if (!Objects.equals(this.auth, other.auth)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "ProxyConfiguration{\n" + "  - proxyPort=" + proxyPort + "  - bufferSize=" + bufferSize + "\n  - targetUri=" + targetUri + "\n  - userAgent=" + userAgent + "\n  - chainedProxyHost=" + chainedProxyHost + "\n  - chainedProxyPort=" + chainedProxyPort + "\n  - algo=" + key + "\n  - auth=" + auth + "\n}";
  }
  
}
