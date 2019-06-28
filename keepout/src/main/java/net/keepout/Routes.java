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

package net.keepout;

import java.io.Closeable;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import static net.keepout.Unchecked.call;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28 de jun de 2019
 */
public class Routes implements Closeable {

  private final BlockingDeque<Packet> incoming;
  
  private final BlockingDeque<Packet> outgoing;
  
  private final List<Route> routes;
  
  public Routes() {
    incoming = new LinkedBlockingDeque<>();
    outgoing = new LinkedBlockingDeque<>();
    routes = new CopyOnWriteArrayList<>();
  }
  
  public void add(Route r) {
    if(r != null) {
      routes.add(r);
    }
  }
  
  public void addRoute(SocketChannel sch) {
    if(sch != null) {
      routes.add(Route.create(this, sch));
    }
  }
  
  public void remove(Route r) {
    if(r != null) {
      routes.remove(r);
    }
  }
  
  public Optional<Route> routeFor(Packet p) {
    if(p == null) return Optional.empty();
    return routes.stream().filter(r -> r.getId().equals(p.getId())).findAny();
  }
  
  @Override
  public void close() {
    incoming.forEach(Packet::close);
    outgoing.forEach(Packet::close);
    routes.stream().map(Route::getChannel).forEach(r -> call(()->r.close()));
    incoming.clear();
    outgoing.clear();
    routes.clear();
  }
  
}
