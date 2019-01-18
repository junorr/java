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

package us.pserver.binbox.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.BitArray;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitInstant;
import us.pserver.bitbox.BitPrimitive;
import us.pserver.bitbox.BitPrimitiveArray;
import us.pserver.bitbox.BitRegion;
import us.pserver.bitbox.BitString;
import us.pserver.tools.StringPad;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitBoxIDs {

  @Test
  public void testBitBoxIDs() {
    List<Integer> ids = new ArrayList<>();
    ids.add(BitRegion.ID);
    Assertions.assertFalse(ids.contains(BitString.ID));
    ids.add(BitString.ID);
    Assertions.assertFalse(ids.contains(BitArray.ID));
    ids.add(BitArray.ID);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_BOOLEAN));
    ids.add(BitPrimitive.ID_BOOLEAN);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_CHAR));
    ids.add(BitPrimitive.ID_CHAR);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_DOUBLE));
    ids.add(BitPrimitive.ID_DOUBLE);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_FLOAT));
    ids.add(BitPrimitive.ID_FLOAT);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_INT));
    ids.add(BitPrimitive.ID_INT);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_LONG));
    ids.add(BitPrimitive.ID_LONG);
    Assertions.assertFalse(ids.contains(BitPrimitive.ID_SHORT));
    ids.add(BitPrimitive.ID_SHORT);
    ids.stream().map(i -> StringPad.of(Objects.toString(i)).lpad(".", 15)).forEach(System.out::println);
  }
  
}
