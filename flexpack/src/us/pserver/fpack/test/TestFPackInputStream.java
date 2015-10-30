/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack.test;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.fpack.FPackEntry;
import us.pserver.fpack.FPackInputStream;
import us.pserver.streams.StreamConnector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class TestFPackInputStream {

  
  public static void main(String[] args) throws IOException {
    Path pi = Paths.get("/storage/fpack.plain.test");
    FPackInputStream fin = new FPackInputStream(
        Files.newInputStream(pi, 
            StandardOpenOption.READ)
    );
    StreamConnector sc = new StreamConnector(fin, System.out);
    while(true) {
      FPackEntry ent = fin.getNextEntry();
      if(ent == null) break;
        System.out.println("* entry = "+ JsonWriter.objectToJson(ent));
      System.out.println("----------------------");
      sc.connect(ent.getSize());
    }
    fin.close();
    System.out.flush();
  }
  
}
