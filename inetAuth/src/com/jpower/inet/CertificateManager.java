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

package com.jpower.inet;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/07/2013
 */
public class CertificateManager extends X509ExtendedTrustManager {
  
  private X509Certificate[] certs;


  @Override
  public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {
    certs = xcs;
  }

  @Override
  public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {
    certs = xcs;
  }

  @Override
  public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {
    certs = xcs;
  }

  @Override
  public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {
    certs = xcs;
  }

  @Override
  public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    certs = xcs;
  }

  @Override
  public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    certs = xcs;
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }

}
