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

package us.pserver.tool.om.test;

import java.net.InetAddress;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/01/2018
 */
public interface ServerConfig {

  public String getServerFullAddress();
  
  public int getServerPort();
  
  public InetAddress getServerAddress();
  
  public String getUserName();
  
  public String getUserKey();
  
  public String getUserCredentials();
  
  public ServerConfig setServerFullAddress(String full);
  
  public ServerConfig setServerPort(int port);
  
  public ServerConfig setServerPort(String port);
  
  public ServerConfig setServerAddress(InetAddress addr);
  
  public ServerConfig setServerAddress(String addr);
  
  public ServerConfig setUserName(String name);
  
  public ServerConfig setUserKey(String key);
  
  public ServerConfig setUserCredentials(String cred);
  
}
