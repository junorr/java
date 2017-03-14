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

package us.pserver.jose.test;

import java.nio.ByteBuffer;
import us.pserver.jose.Region;
import us.pserver.jose.driver.ByteReader;
import us.pserver.jose.driver.StringByteReader;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2017
 */
public class TestMixByteReader {
  
  public static final String JSON = "{'hello': 'world', 'isTrue': false; 'nest': {'lucky': 52.2}}".replace("'", "\"");

  
  public static void main(String[] args) {
    System.out.println("JSON: "+ JSON);
    StringByteReader rdr = StringByteReader.of(
        ByteReader.of(ByteBuffer.wrap(
            UTF8String.from(JSON).getBytes())
        )
    );
    int idx = rdr.indexOf("isTrue");
    System.out.println("* rdr.indexOf(\"isTrue\"): "+ idx);
    ByteIterator bi = rdr.iterator();
    String fld = bi.readField();
    System.out.println("* bi.readField(): "+ fld);
    JsonType type = bi.nextValueType();
    System.out.println("* bi.nextValueType(): "+ type);
    idx = rdr.indexOf("nest");
    System.out.println("* rdr.indexOf(\"nest\"): "+ idx);
    fld = bi.readField();
    System.out.println("* bi.readField(): "+ fld);
    type = bi.nextValueType();
    System.out.println("* bi.nextValueType(): "+ type);
    type = bi.skip().nextValueType();
    System.out.println("* bi.skip().nextValueType(): "+ type);
  }
  
}
