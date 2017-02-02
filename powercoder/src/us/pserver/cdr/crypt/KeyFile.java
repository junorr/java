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
 *
*/

package us.pserver.cdr.crypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import us.pserver.cdr.crypt.iv.BasicIV;
import us.pserver.cdr.hex.HexCoder;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;


/**
 * Arquivo para armazenamento de Chave de 
 * criptografia <code>CryptKey</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class KeyFile {

  
  /**
   * Salva a chave na saida especificada.
   * @param key Chave de criptografia a ser salva.
   * @param out Stream de saída onde a chave será salva.
   * @throws IOException em caso de erro no stream de saída.
   */
  public static void save(CryptKey key, OutputStream out) throws IOException {
    Sane.of(key).check(Checkup.isNotNull());
    Sane.of(out).check(Checkup.isNotNull());
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
    bw.write(key.getAlgorithm().toString());
    bw.newLine();
    bw.write(HexCoder.toHexString(key.getHash()));
    bw.newLine();
    bw.write(HexCoder.toHexString(key.getIV().getVector()));
    bw.newLine();
    bw.flush();
  }
  
  
  /**
   * Carrega a chave do stream de entrada especificado.
   * @param in Stream de entrada de onde será lida a chave.
   * @return A chave de criptografia lida.
   * @throws IOException em caso de erro ao ler do stream.
   */
  public static CryptKey load(InputStream in) throws IOException {
    Sane.of(in).check(Checkup.isNotNull());
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String algo = Sane.of(br.readLine())
        .with("Invalid Algorithm String")
        .get(Checkup.isNotEmpty());
    String xhash = Sane.of(br.readLine())
        .with("Invalid Hash Hex String")
        .get(Checkup.isNotEmpty());
    String xiv = Sane.of(br.readLine())
        .with("Invalid IV Hex String")
        .get(Checkup.isNotEmpty());
    CryptAlgorithm ca = Sane.of(CryptAlgorithm.fromString(algo))
        .get(Checkup.isNotNull());
    byte[] hash = Sane.of(HexCoder.fromHexString(xhash))
        .with("Invalid Hash Array")
        .get(Checkup.isNotEmptyArray());
    BasicIV iv = new BasicIV(
        Sane.of(HexCoder.decode(xiv))
            .with("Invalid IV Array")
            .get(Checkup.isNotEmptyArray())
    );
    CryptKey key = new CryptKey();
    key.setKey(hash, iv, ca);
    return key;
  }
  
  
  public static void main(String[] args) throws IOException {
    KeyFile.save(CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_256_PKCS5), System.out);
  }
  
}
