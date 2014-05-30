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

package com.jpower.net;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 15/07/2013
 */
public class HostPool {
  
  private LinkedList<VirtualHost> hosts;
  
  
  public HostPool() {
    hosts = new LinkedList<>();
  }
  
  
  public boolean addHost(VirtualHost host) {
    return host != null && hosts.add(host);
  }
  
  
  public HostPool add(VirtualHost host) {
    this.addHost(host);
    return this;
  }
  
  
  public VirtualHost remove(int idx) {
    return hosts.remove(idx);
  }
  
  
  public boolean remove(VirtualHost host) {
    return hosts.remove(host);
  }
  
  
  public HostPool rm(VirtualHost host) {
    this.remove(host);
    return this;
  }
  
  
  public VirtualHost rm(SocketChannel channel) {
    for(int i = 0; i < hosts.size(); i++) {
      if(hosts.get(i) != null 
          && hosts.get(i).channel()
          .equals(channel)) {
        return hosts.remove(i);
      }
    }
    return null;
  }
  
  
  public VirtualHost getBy(SocketChannel channel) {
    for(int i = 0; i < hosts.size(); i++) {
      if(hosts.get(i) != null 
          && hosts.get(i).channel()
          .equals(channel)) {
        return hosts.get(i);
      }
    }
    return null;
  }
  
  
  public VirtualHost get(int idx) {
    return hosts.get(idx);
  }
  
  
  public int size() {
    return hosts.size();
  }
  
  
  public boolean isEmpty() {
    return hosts.isEmpty();
  }

}
