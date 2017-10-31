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

package us.pserver.dbone.store;

import us.pserver.dbone.internal.Region;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface Block {

  public Region region();
  
  public ByteBuffer buffer();
  
  public Optional<Region> next();
  
  public Block setNext(Region r);
  
  
  public static void copy(ByteBuffer from, ByteBuffer to, int len) {
    if(from.hasArray()) {
      to.put(
          from.array(), 
          from.arrayOffset(), 
          len
      );
      from.position(from.position() + len);
    }
    else {
      byte[] bs = new byte[len];
      from.get(bs);
      to.put(bs);
    }
  }

}
