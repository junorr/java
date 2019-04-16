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

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public class LocalDateTimeTransform implements BitTransform<LocalDateTime> {
  
  @Override
  public Predicate<Class> matching() {
    return LocalDateTime.class::isAssignableFrom;
  }
  
  @Override
  public BiConsumer<LocalDateTime, BitBuffer> boxing() {
    return (l,b) -> {
      ByteBuffer lb = StandardCharsets.UTF_8.encode(l.toString());
      b.putInt(lb.remaining()).put(lb);
    };
  }
  
  @Override
  public Function<BitBuffer,LocalDateTime> unboxing() {
    return b -> {
      int len = b.getInt();
      int lim = b.limit();
      b.limit(b.position() + len);
      LocalDateTime l = LocalDateTime.parse(
          StandardCharsets.UTF_8.decode(b.toByteBuffer()).toString()
      );
      b.limit(lim);
      return l;
    };
  }
  
}
