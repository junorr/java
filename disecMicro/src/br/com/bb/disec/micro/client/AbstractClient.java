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

package br.com.bb.disec.micro.client;

import org.apache.http.StatusLine;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public abstract class AbstractClient {
  
  protected final String address;
  
  protected final int port;
  
  protected final String context;
  
  protected StatusLine status;
  
  
  protected AbstractClient(String address, int port, String context) {
    if(address == null || address.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Address: "+ address);
    }
    if(port <= 0 || port > 65535) {
      throw new IllegalArgumentException("Bad Port: "+ port);
    }
    this.address = address;
    this.port = port;
    this.context = context;
  }
  
  
  public String getUriString() {
    StringBuilder sb = new StringBuilder()
        .append(address)
        .append(":")
        .append(port)
        .append("/")
        .append(context);
    return sb.toString();
  }
  
  
  public int getResponseCode() {
    return (status != null ? status.getStatusCode() : 0);
  }
  
  
  public String getResponsePhrase() {
    return (status != null ? status.getReasonPhrase() : null);
  }
  
}