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
import java.nio.file.Path;
import us.pserver.cdr.hex.HexCoder;
import us.pserver.tools.Valid;


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
    Valid.off(key).forNull().fail(CryptKey.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
    bw.write(key.getAlgorithm().toString());
    bw.newLine();
    bw.write(HexCoder.toHexString(key.getHash()));
    bw.newLine();
    bw.write(HexCoder.toHexString(key.getIV().getBytes()));
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
    Valid.off(in).forNull().fail(InputStream.class);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String algo = Valid.off(br.readLine()).forEmpty()
        .getOrFail("Invalid Algorithm String: ");
    String xhash = Valid.off(br.readLine()).forEmpty()
        .getOrFail("Invalid Hash Hex String: ");
    String xiv = Valid.off(br.readLine()).forEmpty()
        .getOrFail("Invalid IV Hex String: ");
    CryptAlgorithm ca = Valid.off(CryptAlgorithm.fromString(algo))
        .forNull().getOrFail(CryptAlgorithm.class);
    byte[] hash = Valid.off(HexCoder.fromHexString(xhash))
        .forEmpty().getOrFail("Invalid Hash Array");
    SecureIV iv = new SecureIV(Valid.off(HexCoder.decode(xiv))
        .forEmpty().getOrFail("Invalid IV Array"), ca
    );
    CryptKey key = new CryptKey();
    key.setKey(hash, iv, ca);
    return key;
  }
  
  
  public static void main(String[] args) throws IOException {
    KeyFile.save(CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_256_PKCS5), System.out);
  }
  
}
