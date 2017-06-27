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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarFile;
import us.pserver.dyna.LoaderRegistry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public class LoaderRegistryImpl implements LoaderRegistry {
  
  private final Map<String,Path> registry;
  
  
  public LoaderRegistryImpl() {
    this.registry = Collections.synchronizedMap(new TreeMap<>());
  }
  

  @Override
  public boolean isRegistered(Path path) {
    return registry.containsValue(path);
  }


  @Override
  public boolean isRegistered(String cls) {
    return registry.containsKey(cls);
  }


  @Override
  public boolean isRegistered(Class cls) {
    return registry.containsKey(cls.toString());
  }


  @Override
  public LoaderRegistry unregister(Path path) {
    if(path != null && isRegistered(path)) {
      this.listContent(path)
          .forEach(registry::remove);
    }
    return this;
  }
  
  
  @Override
  public LoaderRegistry register(Path path) {
    if(path != null) {
      if(Files.isDirectory(path)) {
        this.registerDir(path);
      }
      else if(path.toString().endsWith(".jar") && !isRegistered(path)) {
        this.registerJar(path);
      }
    }
    return this;
  }
  
  
  private LoaderRegistry registerDir(Path path) {
    if(!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Invalid directory: "+ path);
    }
    try {
      Files.list(path).sorted().forEach(this::register);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
    return this;
  }


  private LoaderRegistry registerJar(Path path) {
    if(!Files.isRegularFile(path)) {
      throw new IllegalArgumentException("Invalid file: "+ path);
    }
    System.out.println("* registerJar( '"+ path+ "' ) | isRegistered: "+ this.isRegistered(path));
    try {
      JarFile jf = new JarFile(path.toFile());
      jf.stream().filter(e->!e.isDirectory())
          .forEach(e->registry.put(e.toString(), path));
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
    return this;
  }


  @Override
  public List<Path> listJars() {
    List<Path> ls = new ArrayList<>();
    ls.addAll(registry.values());
    return ls;
  }


  @Override
  public URL[] listURLs() {
    List<URL> ls = new ArrayList<>();
    registry.values().stream()
        .map(this::path2url)
        .forEach(ls::add);
    URL[] urls = new URL[ls.size()];
    return ls.toArray(urls);
  }
  
  
  private URL path2url(Path p) {
    try {
      return p.toUri().toURL();
    }
    catch(MalformedURLException e) {
      return null;
    }
  }


  @Override
  public List<String> listContent(Path path) {
    if(path == null || !registry.containsValue(path)) {
      return Collections.EMPTY_LIST;
    }
    List<String> ls = new ArrayList<>();
    registry.entrySet().stream()
        .filter(e->path.equals(e.getValue()))
        .map(e->e.getKey())
        .forEach(ls::add);
    return ls;
  }


  @Override
  public Path getJarPath(String cls) {
    return registry.get(cls);
  }


  @Override
  public Path getJarPath(Class cls) {
    return registry.get(cls.toString());
  }
  
}
