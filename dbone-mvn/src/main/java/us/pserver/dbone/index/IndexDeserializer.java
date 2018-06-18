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
import us.pserver.dbone.index.Index;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.serial.Deserializer;
import us.pserver.dbone.serial.SerializationService;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/06/2018
 */
public class IndexDeserializer implements Deserializer<Index> {

  @Override
  public Index apply(Class<Index> cls, ByteBuffer buf, SerializationService cfg) throws IOException {
    Region reg = Region.of(buf);
    String name = readSizedUTF8String(buf);
    Class vcls = getClass(buf);
    Comparable value = (Comparable) cfg.deserialize(vcls, buf);
    return Index.of(name, value, reg);
  }
  
  
  private String readSizedUTF8String(ByteBuffer buf) {
    int lim = buf.limit();
    int size = buf.getInt();
    buf.limit(buf.position() + size);
    String str = StandardCharsets.UTF_8.decode(buf).toString();
    buf.limit(lim);
    return str;
  }
  
  
  private <T> Class<T> getClass(ByteBuffer buf) throws IOException {
    try {
      String cname = readSizedUTF8String(buf);
      return (Class<T>) Class.forName(cname);
    }
    catch(ClassNotFoundException e) {
      throw new IOException(e.toString(), e);
    }
  }

}
