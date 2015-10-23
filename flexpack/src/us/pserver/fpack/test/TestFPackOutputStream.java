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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.fpack.FPackEncoding;
import us.pserver.fpack.FPackEntry;
import us.pserver.fpack.FlexPackOutputStream;
import us.pserver.io.OutputConnector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class TestFPackOutputStream {

  
  public static void main(String[] args) throws IOException {
    FPackEntry e = new FPackEntry("log.xml");
    Path pi = Paths.get("/storage/log.xml");
    e.setSize(Files.size(pi));
    e.put("path", pi.toAbsolutePath().toString());
    Path po = Paths.get("/storage/fpack.plain.test");
    if(Files.exists(po)) Files.delete(po);
    InputStream input = Files.newInputStream(
        pi, StandardOpenOption.READ
    );
    OutputStream output = Files.newOutputStream(
        po, StandardOpenOption.WRITE, 
        StandardOpenOption.CREATE
    );
    //System.out.println("* entry.size="+ e.getWriteSize());
    e.setCryptKey(CryptKey
        .createWithUnsecurePasswordIV(
            "123456", CryptAlgorithm.AES_CBC_PKCS5)
    ).addEncoding(FPackEncoding.LZMA);
    FlexPackOutputStream fox = new FlexPackOutputStream(output);
    OutputConnector con = new OutputConnector(fox);
    fox.putEntry(e);
    con.connectAndClose(input);
  }
  
}
