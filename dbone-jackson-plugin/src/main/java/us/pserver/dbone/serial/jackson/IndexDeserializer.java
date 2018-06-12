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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import us.pserver.dbone.index.Index;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.serial.JsonIndex;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/06/2018
 */
public class IndexDeserializer extends JsonDeserializer<Index> implements JsonIndex {
  
  @Override
  public Index<?> deserialize(JsonParser psr, DeserializationContext ctx) throws IOException, JsonProcessingException {
    String name = null;
    Region region = null;
    Class cls = null;
    Object val = null;
    JsonToken tk = null;
    while((tk = psr.nextToken()) != null) {
      switch(tk) {
        case FIELD_NAME:
          if(FNAME.equals(psr.currentName())) {
            name = psr.nextTextValue();
          }
          else if(FREGION.equals(psr.currentName())) {
            psr.nextValue();
            region = psr.readValueAs(Region.class);
          }
          else if(FCLASS.equals(psr.currentName())) {
            psr.nextValue();
            cls = getClass(psr);
          }
          else if(FCVALUE.equals(psr.currentName())) {
            psr.nextValue();
            val = psr.readValueAs(cls);
          }
          break;
      }
    }
    return Index.of(name, (Comparable) val, region);
  }
  
  private Class<?> getClass(JsonParser psr) throws IOException {
    try {
      return Class.forName(psr.getValueAsString());
    }
    catch(ClassNotFoundException e) {
      throw new IOException(e.toString(), e);
    }
  }
  
}
