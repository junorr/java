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

package br.com.bb.disec.micro;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


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
public class ResourceLoader {
  
  private final Class loader;
  
  
  /**
   * Construtor padrão que recebe a classe a partir da qual 
   * os recursos serão carregados.
   * @param cls Classe a partir da qual 
   * os recursos serão carregados.
   */
  public ResourceLoader(Class cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Bad Null Class");
    }
    this.loader = cls;
  }
  
  
  /**
   * Constrói um ResourceLoader utilizando a própria
   * classe para carregar recursos.
   * @return Nova instância de ResourceLoader.
   */
  public static ResourceLoader self() {
    return new ResourceLoader(ResourceLoader.class);
  }
  
  
  /**
   * Constrói um ResourceLoader utilizando a 
   * classe informada para carregar recursos.
   * @param cls Classe a partir da qual 
   * os recursos serão carregados.
   * @return Nova instância de ResourceLoader.
   */
  public static ResourceLoader of(Class cls) {
    return new ResourceLoader(cls);
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
   * Pega a classe utilizada para carregar recursos.
   * @return classe utilizada para carregar recursos.
   */
  public Class loader() {
    return loader;
  }
  
  
  /**
   * Valida um recurso para não ser vazio ou nulo.
   * @param resource Nome do resource a ser validado
   */
  private void testResource(String resource) {
    if(resource == null || resource.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Empty Resource");
    }
  }
  
  
  /**
   * Carrega um InputStream a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return InputStream a partir do nome do recurso informado.
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public InputStream loadStream(String resource) throws ResourceLoadException {
    this.testResource(resource);
    try {
      return loader.getResourceAsStream(resource);
      //return findResource(resource).openStream();
    } catch(Exception e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  /**
   * Carrega um BufferedReader a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return BufferedReader a partir do nome do recurso informado.
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public BufferedReader loadReader(String resource) throws ResourceLoadException {
    this.testResource(resource);
    return new BufferedReader(
        new InputStreamReader(
            loadStream(resource))
    );
  }
  
  
  /**
   * Carrega um ReadableByteChannel a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return ReadableByteChannel a partir do nome do recurso informado.
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public ReadableByteChannel loadChannel(String resource) {
    this.testResource(resource);
    return Channels.newChannel(
        loadStream(resource)
    );
  }
  
  
  /**
   * Carrega o conteúdo de texto a partir do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return String com o conteúdo de texto a partir do recurso informado.
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public String loadContentString(String resource) throws ResourceLoadException {
    ReadableByteChannel channel = this.loadChannel(resource);
    ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
    StringBuilder builder = new StringBuilder();
    try {
      while(channel.read(buffer) > 0) {
        buffer.flip();
        builder.append(StandardCharsets.UTF_8
            .newDecoder().decode(buffer).toString()
        );
        buffer.clear();
      }
      return builder.toString();
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
    finally {
      try { channel.close(); }
      catch(IOException e) {}
    }
  }
  
  
  /**
   * Carrega um JSON a partir do recurso informado.
   * @param resource Nome do recurso procurado
   * @return JSON carregado
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public JsonElement loadJson(String resource) throws ResourceLoadException {
    Reader rd = this.loadReader(resource);
    try {
      return new JsonParser().parse(rd);
    }
    finally {
      try { rd.close(); }
      catch(IOException e) {}
    }
  }
  
  
  
  
  
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
