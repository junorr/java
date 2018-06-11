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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import us.pserver.dbone.index.Index;
import us.pserver.dbone.serial.JsonIndex;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/06/2018
 */
public class IndexSerializer extends JsonSerializer<Index> implements JsonIndex {
  
  @Override
  public void serialize(Index idx, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeStringField(FNAME, idx.name());
    gen.writeObjectField(FREGION, idx.region());
    gen.writeFieldName(FVALUE);
    gen.writeStartObject();
    gen.writeStringField(FCLASS, idx.value().getClass().getName());
    gen.writeObjectField(FCVALUE, idx.value());
    gen.writeEndObject();
    gen.writeEndObject();
  }

}
