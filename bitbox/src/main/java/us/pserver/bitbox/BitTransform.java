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

package us.pserver.bitbox;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.type.SerializedType;
import us.pserver.bitbox.type.TypeMatching;

/**
 *
 * @author Juno Roesler - juno@pseimport us.pserver.bitbox.impl.BitBuffer;
rver.us
 * @version 0.0 - 12 de abr de 2019
 */
public interface BitTransform<T> extends TypeMatching, SerializedType {
  
  public boolean match(Class c);
  
  public int box(T obj, BitBuffer buf);
  
  public T unbox(BitBuffer buf);
  
  
  
  public static <U> BitTransform of(Predicate<Class> prd, ToIntBiFunction<U, BitBuffer> box, Function<BitBuffer,U> unbox) {
    return new BitTransform<U>() {
      @Override
      public boolean match(Class c) {
        return prd.test(c);
      }
      @Override
      public Optional<Class> serialType() {
        return Optional.empty();
      }
      @Override
      public int box(U obj, BitBuffer buf) {
        return box.applyAsInt(obj, buf);
      }
      @Override
      public U unbox(BitBuffer buf) { 
        return unbox.apply(buf); 
      }
    };
  }
  
  
  public static <U> BitTransform of(Predicate<Class> prd, Class serialClass, ToIntBiFunction<U, BitBuffer> box, Function<BitBuffer,U> unbox) {
    return new BitTransform<U>() {
      @Override
      public boolean match(Class c) {
        return prd.test(c);
      }
      @Override
      public Optional<Class> serialType() {
        return Optional.of(serialClass);
      }
      @Override
      public int box(U obj, BitBuffer buf) {
        return box.applyAsInt(obj, buf);
      }
      @Override
      public U unbox(BitBuffer buf) { 
        return unbox.apply(buf); 
      }
    };
  }
  
}
