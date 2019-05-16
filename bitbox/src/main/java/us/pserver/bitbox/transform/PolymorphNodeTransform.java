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

package us.pserver.bitbox.transform;

import java.util.Objects;
import java.util.Optional;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26 de abr de 2019
 */
public class PolymorphNodeTransform implements BitTransform<Object> {

  private final BitBoxConfiguration cfg;
  
  public PolymorphNodeTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
  @Override
  public boolean match(Class c) {
    return false;
  }
  
  @Override
  public int box(Object obj, BitBuffer buf) {
    if(obj == null) {
      buf.putInt(-1);
      return Integer.BYTES;
    }
    int pos = buf.position();
    Class c = obj.getClass();
    //Logger.debug("Node({}: {})", c.getSimpleName(), obj);
    BitTransform trans = cfg.getTransform(c);
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    int len = Integer.BYTES;
    buf.position(pos + len);
    len += ctran.box(trans.serialType().orElse(c), buf);
    int vpos = buf.position();
    len += trans.box(obj, buf);
    buf.putInt(pos, vpos);
    buf.position(pos + len);
    return len;
  }
  
  @Override
  public Object unbox(BitBuffer buf) {
    int vpos = buf.getInt();
    if(vpos < 0) return null;
    BitTransform<Class> ctran = cfg.getTransform(Class.class);
    Class c = ctran.unbox(buf);
    BitTransform trans = cfg.getTransform(c);
    Object o = trans.unbox(buf);
    //Logger.debug("Node({}: {})", c.getSimpleName(), o);
    return o;
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
}
