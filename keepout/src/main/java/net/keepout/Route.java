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
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import static net.keepout.Unchecked.call;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28 de jun de 2019
 */
public class Route implements Closeable {
  
  private final Routes routes;

  private final String id;
  
  private final String address;
  
  private final SocketChannel channel;
  
  public Route(Routes r, String id, String address, SocketChannel sch) {
    this.routes = Objects.requireNonNull(r);
    this.id = Objects.requireNonNull(id);
    this.address = Objects.requireNonNull(address);
    this.channel = Objects.requireNonNull(sch);
  }
  
  public static Route create(Routes r, SocketChannel sch) {
    Objects.requireNonNull(sch);
    String laddr = call(()->sch.getLocalAddress().toString());
    String raddr = call(()->sch.getRemoteAddress().toString());
    String id = DigestUtils.sha1Hex(String.format("%s-%s-%d", raddr, laddr, System.currentTimeMillis()));
    return new Route(r, id, raddr, sch);
  }
  
  public String getId() {
    return id;
  }
  
  public String getAddress() {
    return address;
  }
  
  public SocketChannel getChannel() {
    return channel;
  }
  
  @Override
  public void close() throws IOException {
    channel.close();
    routes.remove(this);
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + Objects.hashCode(this.id);
    hash = 89 * hash + Objects.hashCode(this.address);
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
    final Route other = (Route) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.address, other.address);
  }


  @Override
  public String toString() {
    return String.format("Route{id=%s, address=%s}", id, address);
  }
  
}
