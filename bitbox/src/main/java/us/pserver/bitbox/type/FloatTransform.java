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

package us.pserver.bitbox.type;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import us.pserver.bitbox.BitTransform;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public class FloatTransform implements BitTransform<Float> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> c == float.class || c == Float.class;
  }
  
  public DoubleBiConsumer<ByteBuffer> floatBoxing() {
    return (i,b) -> b.position(0).putFloat((float)i);
  }
  
  @Override
  public BiConsumer<Float,ByteBuffer> boxing() {
    return (i,b) -> {
      b.position(0).putFloat(i);
    };
  }
  
  @Override
  public Function<ByteBuffer,Float> unboxing() {
    return b -> b.position(0).getFloat();
  }
  
  public ToDoubleFunction<ByteBuffer> floatUnboxing() {
    return b -> b.position(0).getFloat();
  }
  
}
