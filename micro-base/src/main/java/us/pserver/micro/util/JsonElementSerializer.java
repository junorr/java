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

package us.pserver.micro.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.IOException;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/03/2018
 */
public class JsonElementSerializer implements Serializer<JsonElement> {
  
  private final Gson gson;
  
  public JsonElementSerializer(Gson gson) {
    this.gson = Match.notNull(gson).getOrFail("Bad null Gson");
  }

  @Override
  public void serialize(DataOutput2 out, JsonElement value) throws IOException {
    out.writeUTF(value.toString());
  }


  @Override
  public JsonElement deserialize(DataInput2 input, int available) throws IOException {
    return gson.fromJson(input.readUTF(), JsonElement.class);
  }

}
