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

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import us.pserver.dyna.DynaLoader;
import us.pserver.dyna.LoaderRegistry;
import us.pserver.dyna.ResourceLoader;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public class DynaLoaderImpl implements DynaLoader {
  
  private final LoaderRegistry registry;
  
  private final List<Class> loaded;
  
  private URLClassLoader loader;
  
  
  public DynaLoaderImpl() {
    this.registry = new LoaderRegistryImpl();
    this.loaded = Collections.synchronizedList(new ArrayList<>());
    this.loader = this.create();
  }
  
  
  private URLClassLoader create() {
    ClassLoader cl = DynaLoader.class.getClassLoader();
    cl = (cl != null ? cl : ClassLoader.getSystemClassLoader());
    if(registry.listJars().isEmpty()) {
      return (URLClassLoader) cl;
    }
    else {
      return new URLClassLoader(registry.listURLs(), cl);
    }
  }
  
  
  @Override
  public boolean isLoaded(String cls) {
    return loaded.stream()
        .map(c->c.toString())
        .anyMatch(s->s.equals(cls));
  }


  @Override
  public boolean isLoaded(Class cls) {
    return loaded.contains(cls);
  }
  
  
  @Override
  public synchronized Class<?> getLoaded(String cls) {
    Class cl = null;
    if(isLoaded(cls)) {
      cl = loaded.stream()
        .filter(c->c.toString().equals(cls))
        .findAny().orElse(null);
    }
    return cl;
  }
  
  
  @Override
  public List<Class> listLoaded() {
    return Collections.unmodifiableList(loaded);
  }
  
  
  @Override
  public List<Path> listJars() {
    return registry.listJars();
  }


  @Override
  public synchronized Class<?> load(String cls) {
    if(cls == null || cls.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid class name: "+ cls);
    }
    if(isLoaded(cls)) {
      return getLoaded(cls);
    }
    try {
      Class c = this.loader.loadClass(cls);
      this.loaded.add(c);
      return c;
    }
    catch(ClassNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }


  @Override
  public synchronized Object loadAndCreate(String cls) {
    return new Reflector(this.load(cls)).create();
  }


  @Override
  public ResourceLoader getResourceLoader(String cls) {
    Class c = isLoaded(cls) ? getLoaded(cls) : load(cls);
    return ResourceLoader.of(c);
  }


  @Override
  public ResourceLoader getResourceLoader(Class cls) {
    return ResourceLoader.of(cls);
  }


  @Override
  public ClassLoader getClassLoader() {
    return this.loader;
  }
  
  
  @Override
  public synchronized boolean isRegistered(Path path) {
    return registry.isRegistered(path);
  }


  @Override
  public synchronized DynaLoader register(Path path) {
    if(path != null) {
      registry.register(path);
      this.loader = create();
    }
    return this;
  }


  @Override
  public synchronized DynaLoader unregister(Path path) {
    if(path != null) {
      registry.unregister(path);
      this.loader = create();
    }
    return this;
  }

}
