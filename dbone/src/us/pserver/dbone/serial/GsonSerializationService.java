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
import java.util.Arrays;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/09/2017
 */
public class GsonSerializationService implements SerializationService {
  
  private final Gson gson;
  
  public GsonSerializationService() {
    gson = new GsonBuilder().create();
  }

  @Override
  public ByteBuffer serialize(Object obj) {
    //System.out.println("* GsonSerializationService.serialize *");
    byte[] bcls = UTF8String.from(obj.getClass().getName()).getBytes();
    byte[] bobj = UTF8String.from(gson.toJson(obj)).getBytes();
    ByteBuffer buf = ByteBuffer.allocate(bcls.length + bobj.length + Short.BYTES);
    buf.putShort((short) bcls.length);
    buf.put(bcls);
    buf.put(bobj);
    //System.out.print("=>>   serialize: ");
    //print15bytes(buf);
    buf.flip();
    return buf;
  }

  public static void print15bytes(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    byte[] bs = new byte[15];
    buf.position(0);
    buf.limit(15);
    buf.get(bs);
    System.out.println(Arrays.toString(bs));
    buf.limit(lim);
    buf.position(pos);
  }
  
  @Override
  public Object deserialize(ByteBuffer buf) {
    //System.out.print("<<= deserialize: ");
    //print15bytes(buf);
    short icls = buf.getShort();
    //System.out.println("* GsonSerializationService.deserialize: buf.getShort()="+ icls+ ", buf.position()="+ buf.position());
    byte[] bcls = new byte[icls];
    buf.get(bcls);
    byte[] bobj = new byte[buf.remaining()];
    buf.get(bobj);
    try {
      //System.out.println("* GsonSerializationService.deserialize: json: '"+ UTF8String.from(bobj).toString().trim()+ "'");
      Class cls = Class.forName(UTF8String.from(bcls).toString());
      return gson.fromJson(UTF8String.from(bobj).toString().trim(), cls);
    }
    catch(ClassNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }

}
