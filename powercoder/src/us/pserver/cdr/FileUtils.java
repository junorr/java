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

package us.pserver.cdr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.tools.Valid;

/**
 * Classe utilitária com funções para manipulação de
 * arquivos e streams.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/06/2014
 */
public class FileUtils {
  
  /**
   * <code>BUFFER = 4096</code><br>
   * Tamanho padrão do buffer de dados processados.
   */
  public static final int BUFFER = 4096;

  
  /**
   * Cria um caminho <code>java.nio.file.Path</code>
   * a partir da <code>String str</code> informada.
   * @param str com o caminho a ser criado.
   * @return Caminho <code>Path</code>.
   */
  public static Path path(String str) {
    return Paths.get(str);
  }
  
  
  /**
   * Retorna um array de objetos <code>StandardOpenOption</code>
   * para leitura de arquivos.
   * @return array de objetos <code>StandardOpenOption</code>.
   */
  public static StandardOpenOption[] optionsRead() {
    StandardOpenOption[] opts = new StandardOpenOption[1];
    opts[0] = StandardOpenOption.READ;
    return opts;
  }
  
  
  /**
   * Retorna um array de objetos <code>StandardOpenOption</code>
   * para escrita de arquivos.
   * @return array de objetos <code>StandardOpenOption</code>.
   */
  public static StandardOpenOption[] optionsWrite() {
    StandardOpenOption[] opts = new StandardOpenOption[1];
    opts[0] = StandardOpenOption.WRITE;
    return opts;
  }
  
  
  /**
   * Retorna um array de objetos <code>StandardOpenOption</code>
   * para escrita e criação (caso não exista) de arquivos.
   * @return array de objetos <code>StandardOpenOption</code>.
   */
  public static StandardOpenOption[] optionsWriteCreate() {
    StandardOpenOption[] opts = new StandardOpenOption[2];
    opts[0] = StandardOpenOption.WRITE;
    opts[1] = StandardOpenOption.CREATE;
    return opts;
  }
  
  
  /**
   * Cria um stream de entrada para leitura do arquivo 
   * informado.
   * @param path Caminho do arquivo a ser lido.
   * @return <code>InputStream</code>.
   * @throws IOException Caso ocorra erro na criação 
   * do <code>InputStream</code>.
   */
  public static InputStream inputStream(Path path) throws IOException {
    return Files.newInputStream(path, optionsRead());
  }
  
  
  /**
   * Cria um stream de saída para escrita no arquivo 
   * informado, criando o arquivo se necessário.
   * @param path Caminho do arquivo a ser escrito.
   * @return <code>OutputStream</code>.
   * @throws IOException Caso ocorra erro na criação 
   * do <code>OutputStream</code>.
   */
  public static OutputStream outputStream(Path path) throws IOException {
    return Files.newOutputStream(path, optionsWriteCreate());
  }
  
  
  /**
   * Transfere todos os dados do stream de entrada 
   * para o stream de saída.
   * @param in Stream de entrada <code>InputStream</code>.
   * @param out Stream de saída <code>OutputStream</code>.
   * @return Número de bytes transferidos.
   * @throws IOException Caso ocorra erro na transferência dos dados.
   */
  public static long transfer(InputStream in, OutputStream out) throws IOException {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    
    long total = 0;
    byte[] buf = new byte[BUFFER];
    int read = 0;
    while((read = in.read(buf)) > 0) {
      total += read;
      out.write(buf, 0, read);
    }
    out.flush();
    return total;
  }
  
}
