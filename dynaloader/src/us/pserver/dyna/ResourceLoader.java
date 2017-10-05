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

package us.pserver.dyna;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import us.pserver.dyna.impl.ResourceLoaderImpl;


/**
 * Provê funções para carregar recursos de pacotes
 * de acordo com a classe informada no construtor.
 * A capacidade de carregamento de recursos do Java
 * é limitada e não permite escolher o recurso a ser
 * carregado, quando este possui o mesmo nome, porém 
 * arquivos JAR diferentes. ResourceLoader contorna
 * essa limitação, permitindo carregar recursos que 
 * estejam no mesmo arquivo JAR da classe informada
 * no construtor.
 * 
 * @author Juno Roesler - junoroesler@bb.com.br
 * @version 1.0.201609
 */
public interface ResourceLoader {
  
  /**
   * Constrói um ResourceLoader utilizando a própria
   * classe para carregar recursos.
   * @return Nova instância de ResourceLoader.
   */
  public static ResourceLoader self() {
    return new ResourceLoaderImpl(ResourceLoader.class);
  }
  
  
  /**
   * Constrói um ResourceLoader utilizando a 
   * classe informada para carregar recursos.
   * @param cls Classe a partir da qual 
   * os recursos serão carregados.
   * @return Nova instância de ResourceLoader.
   */
  public static ResourceLoader of(Class cls) {
    return new ResourceLoaderImpl(cls);
  }
  
  
  /**
   * Constrói um ResourceLoader utilizando a classe 
   * chamadora deste método para carregar recursos.
   * @return Nova instância de ResourceLoader.
   */
  public static ResourceLoader caller() throws ResourceLoadException {
    try {
      Thread cur = Thread.currentThread();
      StackTraceElement[] els = cur.getStackTrace();
      return of(cur.getContextClassLoader()
          .loadClass(els[Math.min(2, els.length-1)].getClassName())
      );
    } catch(Exception e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  /**
   * Retorna a classe utilizada para carregar recursos.
   * @return classe utilizada para carregar recursos.
   */
  public ClassLoader loader();
  
  
  /**
   * Carrega uma classe pelo nome.
   * @param cname Nome da classe.
   * @return Class
   * @throws us.pserver.dyna.ResourceLoader.ResourceLoadException 
   */
  public Class loadClass(String cname) throws ResourceLoadException;
  
  
  /**
   * Carrega a URL do recurso a partir do informado.
   * @param resource Nome do recurso procurado.
   * @return URL do recurso.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public URL loadURL(String resource) throws ResourceLoadException;
  
  
  /**
   * Carrega um byte array com o conteúdo do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return byte array com o conteúdo do recurso informado.
   * @throws ResourceLoadException Se nenhum recurso for encontrado 
   * ou se ocorrer um erro na busca.
   */
  public byte[] loadBytes(String resource) throws ResourceLoadException;
  
  
  /**
   * Carrega um InputStream a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return InputStream a partir do nome do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public InputStream loadStream(String resource) throws ResourceLoadException;
  
  
  /**
   * Carrega um BufferedReader a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return BufferedReader a partir do nome do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public BufferedReader loadReader(String resource) throws ResourceLoadException;
  
  
  /**
   * Carrega um ReadableByteChannel a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return ReadableByteChannel a partir do nome do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public ReadableByteChannel loadChannel(String resource);
  
  
  /**
   * Carrega o conteúdo de texto a partir do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return String com o conteúdo de texto a partir do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public String loadStringContent(String resource) throws ResourceLoadException;
  
  
  
  
  
  
  
  /**
   * Exceção de runtime para erros lançados por ResourceLoader.
   */
  public static class ResourceLoadException extends RuntimeException {

    public ResourceLoadException(String message) {
      super(message);
    }


    public ResourceLoadException(String message, Throwable cause) {
      super(message, cause);
    }


    public ResourceLoadException(Throwable cause) {
      super("["+ cause.getClass().getSimpleName()
          + "] "+ cause.getMessage(), cause);
    }
    
  }
  
}
