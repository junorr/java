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

package us.pserver.dbone.serial.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import us.pserver.dbone.serial.Deserializer;
import us.pserver.dbone.serial.SerializationService;
import us.pserver.dbone.util.Log;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class JacksonDeserializer implements Deserializer<Object> {
  
  private final ObjectMapper omp;
  
  public JacksonDeserializer(ObjectMapper mpr) {
    this.omp = Objects.requireNonNull(mpr, "Bad null ObjectMapper");
  }

  @Override
  public Object apply(Class cls, ByteBuffer buf, SerializationService cfg) throws IOException {
      String json = StandardCharsets.UTF_8.decode(buf).toString();
      //Log.on("cls=%s, json=%s", cls, json);
      return omp.readerFor(cls).readValue(json);
  }

}
