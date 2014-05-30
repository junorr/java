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
package com.jpower.nettyserver;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/06/2012
 */
public class ForwardConfig {
  
  private String targetAddress;
  
  private int targetPort;
  
  private String remoteAddress;
  
  private int remotePort;
  
  private String cipherKey;
  
  private String proxyAuth;
  
  private boolean sendServer;
  
  
  public ForwardConfig() {
    targetAddress = null;
    remoteAddress = null;
    cipherKey = null;
    proxyAuth = null;
    targetPort = 0;
    remotePort = 0;
  }


  public String getCipherKey() {
    return cipherKey;
  }


  public ForwardConfig setCipherKey(String cipherKey) {
    this.cipherKey = cipherKey;
    return this;
  }


  public String getRemoteAddress() {
    return remoteAddress;
  }


  public ForwardConfig setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
    return this;
  }


  public int getRemotePort() {
    return remotePort;
  }


  public ForwardConfig setRemotePort(int remotePort) {
    this.remotePort = remotePort;
    return this;
  }


  public String getTargetAddress() {
    if(targetAddress == null || targetAddress.trim().isEmpty()) 
      return remoteAddress;
    else return targetAddress;
  }


  public ForwardConfig setTargetAddress(String targetAddress) {
    this.targetAddress = targetAddress;
    return this;
  }


  public int getTargetPort() {
    if(targetPort == 0) return remotePort;
    else return targetPort;
  }


  public ForwardConfig setTargetPort(int targetPort) {
    this.targetPort = targetPort;
    return this;
  }


  public boolean isSendServer() {
    return sendServer;
  }


  public ForwardConfig setSendServer(boolean sendServer) {
    this.sendServer = sendServer;
    return this;
  }


  public String getProxyAuth() {
    return proxyAuth;
  }


  public ForwardConfig setProxyAuth(String proxyAuth) {
    this.proxyAuth = proxyAuth;
    return this;
  }
  
}
