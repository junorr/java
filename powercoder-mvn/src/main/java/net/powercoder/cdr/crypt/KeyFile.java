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

package net.powercoder.cdr.crypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import net.powercoder.cdr.crypt.iv.BasicIV;
import net.powercoder.cdr.hex.HexCoder;


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
    Objects.requireNonNull(key);
    Objects.requireNonNull(out);
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
    Objects.requireNonNull(in);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String algo = Objects.requireNonNull(br.readLine());
    String xhash = Objects.requireNonNull(br.readLine());
    String xiv = Objects.requireNonNull(br.readLine());
    CryptAlgorithm ca = Objects.requireNonNull(CryptAlgorithm.fromString(algo));
    byte[] hash = Objects.requireNonNull(HexCoder.fromHexString(xhash));
    BasicIV iv = new BasicIV(HexCoder.decode(xiv));
    CryptKey key = new CryptKey();
    key.setKey(hash, iv, ca);
    return key;
  }
  
  
  public static void main(String[] args) throws IOException {
    KeyFile.save(CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_256_PKCS5), System.out);
  }
  
}
