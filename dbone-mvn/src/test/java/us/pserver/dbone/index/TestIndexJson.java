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

package us.pserver.dbone.index;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;
import us.pserver.dbone.obj.Record;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.util.Log;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/06/2018
 */
public class TestIndexJson {

  @Test
  public void indexToJson() throws ClassNotFoundException, IOException {
    try {
      Index<Integer> idx = Index.of("magic", 43, Region.of(32, 1024));
      Log.on("idx = %s", idx);
      Record<Index<Integer>> rec = Record.of(idx.region(), idx);
      Log.on("rec = %s", rec);
      ByteBuffer buf = rec.toByteBuffer(ByteBuffer::allocate);
      Log.on("rec.toByteBuffer() = %s", buf);
      rec = Record.of(rec.getRegion(), buf);
      Log.on("rec = %s", rec);
      Log.on("rec.getValue() = %s", rec.getValue());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
