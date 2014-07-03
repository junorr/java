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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.cdr.hex.HexCoder;


/**
 * Arquivo para armazenamento de Chave de 
 * criptografia <code>CryptKey</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class KeyFile {
  
  private CryptKey key;
  
  private String file;
  
  
  /**
   * Construtor que recebe o nome do arquivo a ser 
   * carregado/gerado.
   * @param file arquivo a ser carregado/gerado.
   */
  public KeyFile(String file) {
    if(file == null || file.isEmpty()
        || !Files.exists(Paths.get(file)))
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    key = null;
  }
  
  
  /**
   * Construtor que recebe o nome do arquivo a ser 
   * gerado e a chave de criptografia.
   * @param k Chave <code>CryptKey</code> a ser 
   * armazenada em arquivo.
   * @param file arquivo a ser carregado/gerado.
   */
  public KeyFile(CryptKey k, String file) {
    if(file == null || file.isEmpty())
      throw new IllegalArgumentException("Invalid file: "+ file);
    this.file = file;
    key = k;
  }
  
  
  /**
   * Salva a chave em arquivo.
   * @return <code>true</code> se salvo
   * com sucesso, <code>false</code> caso
   * contrário.
   */
  public boolean save() {
    if(file == null || key == null)
      return false;
    
    try {
      BufferedWriter bw = Files.newBufferedWriter(
          Paths.get(file), Charset.forName("UTF-8"), 
          StandardOpenOption.WRITE);
      bw.write(key.getAlgorithm().toString());
      bw.newLine();
      bw.write(HexCoder.toHexString(key.getHash()));
      bw.newLine();
      bw.flush();
      bw.close();
      return true;
    } catch(IOException e) {
      return false;
    }
  }
  
  
  /**
   * Carrega a chave do arquivo.
   * @return Esta instância modificada de <code>KeyFile</code>.
   */
  public KeyFile load() {
    if(file == null || !Files.exists(Paths.get(file)))
      return this;
    
    try {
      BufferedReader br = Files.newBufferedReader(
          Paths.get(file), Charset.forName("UTF-8"));
      String algo = br.readLine();
      String hex = br.readLine();
      CryptAlgorithm ca = CryptAlgorithm.fromString(algo);
      byte[] hash = HexCoder.fromHexString(hex);
      if(ca != null && hash != null) {
        key = new CryptKey();
        key.setKey(hash, ca);
      }
      return this;
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  /**
   * Retorna a chave de criptografia <code>CryptKey</code>.
   * @return chave de criptografia <code>CryptKey</code>.
   */
  public CryptKey getKey() {
    return key;
  }
  
  
  /**
   * Retorna o arquivo de armazenamento.
   * @return arquivo de armazenamento.
   */
  public String getFile() {
    return file;
  }
  
}
