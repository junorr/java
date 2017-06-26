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

package us.pserver.dyna.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import us.pserver.dyna.ResourceLoader;


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
public class ResourceLoaderImpl implements ResourceLoader {
  
  private final Class loader;
  
  
  /**
   * Construtor padrão que recebe a classe a partir da qual 
   * os recursos serão carregados.
   * @param cls Classe a partir da qual 
   * os recursos serão carregados.
   */
  public ResourceLoaderImpl(Class cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Bad Null Class");
    }
    this.loader = cls;
  }
  
  
  /**
   * Pega a classe utilizada para carregar recursos.
   * @return classe utilizada para carregar recursos.
   */
  @Override
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
   * Procura por um recurso de nome específico.
   * @param resource Nome do recurso a ser carregado
   * @return URI do recurso procurado
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  private URI getURI(String resource) throws ResourceLoadException {
    try {
      return findResource(resource).toURI();
    } catch(URISyntaxException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  /**
   * Procura todos os recursos com o mesmo nome e seleciona
   * um de acordo com a classe utilizada para carregar.
   * @param resource Nome do recurso a ser carregado.
   * @return URL do recurso procurado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  private URL findResource(String resource) throws ResourceLoadException {
    testResource(resource);
    String cpath = "/"+ loader.getName().replace(".", "/") + ".class";
    String def = loader.getResource(cpath).toString().replace(cpath, "");
    try {
      ClassLoader cl = (loader.getClassLoader() != null 
          ? loader.getClassLoader() 
          : ClassLoader.getSystemClassLoader());
      Enumeration<URL> en = cl.getResources(
          (resource.startsWith("/") ? resource.substring(1) : resource)
      );
      URL url = null;
      while(en.hasMoreElements()) {
        url = en.nextElement();
        String surl = url.toString();
        if(surl.contains(def)) break;
      }
      if(url == null) {
        throw new ResourceLoadException("Resource Not Found: "+ resource);
      }
      return url;
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  /**
   * Carrega a URL do recurso a partir do informado.
   * @param resource Nome do recurso procurado.
   * @return URL do recurso.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  @Override
  public URL loadURL(String resource) throws ResourceLoadException {
    return findResource(resource);
  }
  
  
  /**
   * Carrega um InputStream a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return InputStream a partir do nome do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  @Override
  public InputStream loadStream(String resource) throws ResourceLoadException {
    this.testResource(resource);
    try {
      return findResource(resource).openStream();
    } catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  /**
   * Carrega um BufferedReader a partir do nome do recurso informado.
   * @param resource Nome do recurso procurado.
   * @return BufferedReader a partir do nome do recurso informado.
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  @Override
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
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  @Override
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
   * @throws ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  @Override
  public String loadStringContent(String resource) throws ResourceLoadException {
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
  
}
