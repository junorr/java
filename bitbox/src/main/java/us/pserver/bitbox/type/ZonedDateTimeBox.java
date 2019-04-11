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
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Objects;
import us.pserver.bitbox.DataBox;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de abr de 2019
 */
public class ZonedDateTimeBox implements DataBox<ZonedDateTime> {
  
  private final ByteBuffer buffer;
  
  public ZonedDateTimeBox(ByteBuffer buffer) {
    this.buffer = Objects.requireNonNull(buffer);
  }

  @Override
  public ByteBuffer getData() {
    return buffer;
  }
  
  
  @Override
  public ZonedDateTime getValue() {
    long time = buffer.position(0).getLong();
    int zofs = buffer.getShort();
    ZonedDateTime.
    int len = length();
    int lim = buffer.limit();
    buffer.limit(buffer.position() + len);
    String str = StandardCharsets.UTF_8.decode(buffer).toString();
    buffer.limit(lim);
    return ZonedDateTime.parse(str);
  }
  
  
  @Override
  public boolean match(Class c) {
    return c == ZonedDateTime.class;
  }

}
