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
import java.nio.charset.StandardCharsets;
import java.util.function.IntFunction;
import us.pserver.dbone.index.Index;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.serial.Serializer;
import us.pserver.dbone.serial.SerializationService;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class IndexSerializer implements Serializer<Index> {
  
  @Override
  public ByteBuffer apply(Index idx, SerializationService cfg) throws IOException {
    ByteBuffer bname = StandardCharsets.UTF_8.encode(idx.name());
    ByteBuffer bcls = StandardCharsets.UTF_8.encode(idx.value().getClass().getName());
    ByteBuffer ba = cfg.getByteBufferAllocPolicy().apply(Region.BYTES 
        + Integer.BYTES * 2 
        + bname.remaining() 
        + bcls.remaining()
    );
    ba.put(idx.region().toByteBuffer());
    ba.putInt(bname.remaining());
    ba.put(bname);
    ba.putInt(bcls.remaining());
    ba.put(bcls);
    ba.flip();
    ByteBuffer bv = cfg.serialize(idx.value());
    ByteBuffer buf = cfg.getByteBufferAllocPolicy().apply(ba.remaining() + bv.remaining());
    buf.put(ba);
    buf.put(bv);
    buf.flip();
    return buf;
  }

}
