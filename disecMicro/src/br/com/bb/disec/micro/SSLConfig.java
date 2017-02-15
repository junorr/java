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

package br.com.bb.disec.micro;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/02/2017
 */
public class SSLConfig {
  
  public static final String DEFAULT_ADDRESS = "localhost";
  
  public static final String SSL_PROTOCOL = "TLS";
  
  public static final int DEFAULT_PORT = 9443;
  

  private final boolean sslEnabled;
  
  private final String sslAddress;
  
  private final int sslPort;
  
  private final String keystore;
  
  private final String keystorePass;
  
  
  private SSLConfig() {
    this.sslAddress = DEFAULT_ADDRESS;
    this.sslPort = DEFAULT_PORT;
    this.sslEnabled = false;
    this.keystore = null;
    this.keystorePass = null;
  }
  
  
  public SSLConfig(
      boolean enabled,
      String address,
      int port,
      String keystore,
      String keystorePass
  ) {
    if(keystore == null) {
      throw new IllegalArgumentException("Bad Null Keystore File");
    }
    if(keystorePass == null || keystorePass.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Null Keystore Password");
    }
    this.sslEnabled = enabled;
    this.sslAddress = (address == null ? DEFAULT_ADDRESS : address);
    this.sslPort = (port <= 0 || port > 65535 ? DEFAULT_PORT : port);
    this.keystore = keystore;
    this.keystorePass = keystorePass;
  }


  public boolean isEnabled() {
    return sslEnabled;
  }


  public String getAddress() {
    return sslAddress;
  }


  public int getPort() {
    return sslPort;
  }


  public String getKeystore() {
    return keystore;
  }


  public char[] getKeystorePass() {
    return keystorePass.toCharArray();
  }
  
  
  public KeyStore loadKeystore() throws RuntimeException {
    try (InputStream in = ServerSetup.instance()
        .loader().loadStream(this.getKeystore())) {
      KeyStore ks = KeyStore.getInstance("JKS");
      ks.load(in, getKeystorePass());
      return ks;
    } catch (IOException 
        | KeyStoreException 
        | NoSuchAlgorithmException 
        | CertificateException ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }
  
  
  public SSLContext createSSLContext() throws RuntimeException {
    try {
      KeyManager[] keyManagers;
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
          KeyManagerFactory.getDefaultAlgorithm()
      );
      keyManagerFactory.init(loadKeystore(), getKeystorePass());
      keyManagers = keyManagerFactory.getKeyManagers();

      SSLContext ctx = SSLContext.getInstance(SSL_PROTOCOL);
      ctx.init(keyManagers, null, null);
      return ctx;
    }
    catch(KeyManagementException 
        | KeyStoreException 
        | NoSuchAlgorithmException 
        | UnrecoverableKeyException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }


  @Override
  public String toString() {
    return "SSLConfig{" 
        + "\n    enabled: " + sslEnabled 
        + "\n    address: " + sslAddress 
        + "\n    port: " + sslPort 
        + "\n    keystore: " + keystore 
        + "\n}";
  }
  
}
