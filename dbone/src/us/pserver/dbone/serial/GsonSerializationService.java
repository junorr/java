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

package us.pserver.dbone.serial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.ByteBuffer;
import us.pserver.tools.NotNull;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/09/2017
 */
public class GsonSerializationService implements SerializationService {
  
  private final Gson gson;
  
  public GsonSerializationService() {
    this(new Gson());
  }
  
  public GsonSerializationService(Gson gson) {
    this.gson = NotNull.of(gson).getOrFail("Bad null Gson");
  }

  @Override
  public byte[] serialize(Object obj) {
    byte[] bob = UTF8String.from(gson.toJson(obj)).getBytes();
    byte[] cls = UTF8String.from(obj.getClass().getName()).getBytes();
    ByteBuffer buf = ByteBuffer.allocate(cls.length + bob.length + Short.BYTES);
    buf.putShort((short) cls.length);
    buf.put(cls);
    buf.put(bob);
    return buf.array();
  }

  @Override
  public Object deserialize(byte[] bs) {
    ByteBuffer buf = ByteBuffer.wrap(bs);
    short clen = buf.getShort();
    byte[] cls = new byte[clen];
    buf.get(cls);
    byte[] bob = new byte[buf.remaining()];
    buf.get(bob);
    try {
      return gson.fromJson(
          UTF8String.from(bob).toString().trim(), 
          Class.forName(UTF8String.from(cls).toString())
      );
    }
    catch(ClassNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }

}
