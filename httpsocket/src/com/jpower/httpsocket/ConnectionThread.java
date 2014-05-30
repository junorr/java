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
package com.jpower.httpsocket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class ConnectionThread implements Runnable {

  private ConnectionHandler handler;
  
  
  public ConnectionThread() {
    handler = null;
  }
  
  
  public ConnectionThread(ConnectionHandler con) {
    handler = con;
    this.testProperties();
  }
  
  
  private void testProperties() {
    if(handler == null)
      throw new IllegalStateException(
          "Invalid ConnectionHandler: ["+ handler+ "]");
    else if(handler.getSocket() == null)
      throw new IllegalStateException(
          "No Socket found: ConnectionHandler.getSocket() = "
          + handler.getSocket());
  }
  
  
  public ConnectionHandler getConnectionHandler() {
    return handler;
  }
  
  
  public ConnectionThread setConnectionHandler(ConnectionHandler con) {
    handler = con;
    return this;
  }
  
  
  public void start() {
    Thread t = new Thread(this);
    t.setDaemon(true);
    t.start();
  }
  
  
  public void start(ConnectionHandler con) {
    handler = con;
    this.testProperties();
    this.start();
  }
  

  @Override
  public void run() {
    this.testProperties();
    handler.handle();
  }
  
}
