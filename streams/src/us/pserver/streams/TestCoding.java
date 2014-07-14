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

package us.pserver.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/07/2014
 */
public class TestCoding {

  
  public static void main(String[] args) throws IOException {
    InputStream is = Files.newInputStream(Paths.get("d:/base.csv"), StandardOpenOption.READ);
    OutputStream os = Files.newOutputStream(Paths.get("d:/base.csv.gz"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    GZIPOutputStream gos = new GZIPOutputStream(os);
    StreamUtils.transfer(is, gos);
    gos.close();
    is.close();
    os.close();
    
    is = Files.newInputStream(Paths.get("d:/base.csv.gz"), StandardOpenOption.READ);
    os = Files.newOutputStream(Paths.get("d:/base.csv.cy"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    CipherOutputStream cos = CryptUtils.createCipherOutputStream(os, key);
    StreamUtils.transfer(is, cos);
    cos.close();
    is.close();
    os.close();
    
    is = Files.newInputStream(Paths.get("d:/base.csv.cy"), StandardOpenOption.READ);
    os = Files.newOutputStream(Paths.get("d:/base2.csv.gz"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    CipherInputStream cis = CryptUtils.createCipherInputStream(is, key);
    StreamUtils.transfer(cis, os);
    os.close();
    cis.close();
    is.close();
    
    is = Files.newInputStream(Paths.get("d:/base2.csv.gz"), StandardOpenOption.READ);
    os = Files.newOutputStream(Paths.get("d:/base2.csv"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    GZIPInputStream gis = new GZIPInputStream(is);
    StreamUtils.transfer(gis, os);
    os.close();
    gis.close();
    is.close();
  }
  
}
