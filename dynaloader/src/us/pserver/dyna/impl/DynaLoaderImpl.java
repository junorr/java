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
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import us.pserver.dyna.DynaLoader;
import us.pserver.dyna.ResourceLoader;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public class DynaLoaderImpl implements DynaLoader {
  
  private final List<Path> jars;
  
  private URLClassLoader loader;
  
  private final ReentrantLock lock;
  
  
  public DynaLoaderImpl() {
    this.jars = Collections.synchronizedList(new ArrayList<>());
    this.loader = this.create();
    this.lock = new ReentrantLock();
  }
  
  
  private URLClassLoader create() {
    ClassLoader cl = DynaLoader.class.getClassLoader();
    cl = (cl != null ? cl : ClassLoader.getSystemClassLoader());
    if(jars.isEmpty()) {
      return (URLClassLoader) cl;
    }
    else {
      return new URLClassLoader(this.listURLs(), cl);
    }
  }
  
  
  @Override
  public List<Path> listJars() {
    return Collections.unmodifiableList(jars);
  }
  
  
  @Override
  public URL[] listURLs() {
    Function<Path,URL> fn = p->{
      try { return p.toUri().toURL(); }
      catch(MalformedURLException e) {
        return null;
      }
    };
    List<URL> urls = new ArrayList<>(jars.size());
    jars.stream().map(fn).forEach(urls::add);
    return urls.toArray(new URL[urls.size()]);
  }


  @Override
  public Class<?> load(String cls) {
    if(cls == null || cls.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid class name: "+ cls);
    }
    lock.lock();
    try {
      return this.loader.loadClass(cls);
    }
    catch(ClassNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    }
    finally {
      lock.unlock();
    }
  }


  public Class<?> load(String cls, Path jar) {
    if(cls == null || cls.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid class name: "+ cls);
    }
    if(jar == null || !Files.isRegularFile(jar) 
        || !jar.toString().endsWith(".jar")) {
      throw new IllegalArgumentException("Invalid jar: "+ jar);
    }
    lock.lock();
    try {
      URLClassLoader ucl = new URLClassLoader(
          new URL[]{jar.toUri().toURL()}, this.loader
      );
      return ucl.loadClass(cls);
    }
    catch(ClassNotFoundException | MalformedURLException e) {
      throw new RuntimeException(e.toString(), e);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  @Override
  public Object loadAndCreate(String cls) {
    return new Reflector(this.load(cls)).create();
  }


  @Override
  public ResourceLoader getResourceLoader(String cls) {
    return ResourceLoader.of(load(cls));
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
  public boolean isRegistered(Path path) {
    return jars.contains(path);
  }


  @Override
  public DynaLoader register(Path path) {
    if(path != null) {
      lock.lock();
      try {
        if(Files.isDirectory(path)) {
          this.registerDir(path);
        }
        else if(path.toString().endsWith(".jar") 
            && !isRegistered(path)) {
          this.registerJar(path);
        }
        this.loader = create();
        //System.out.println("* DynaLoader.register finished!");
      }
      finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  private DynaLoader registerDir(Path path) {
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


  private DynaLoader registerJar(Path path) {
    if(!Files.isRegularFile(path)) {
      throw new IllegalArgumentException("Invalid file: "+ path);
    }
    jars.add(path);
    return this;
  }
  
  
  @Override
  public DynaLoader unregister(Path path) {
    if(path != null) {
      lock.lock();
      try {
        jars.remove(path);
        this.close();
        this.loader = create();
      }
      finally {
        lock.unlock();
      }
    }
    return this;
  }

  
  @Override
  public void close() {
    if(this.loader != null) {
      lock.lock();
      try { this.loader.close(); } 
      catch(IOException e) {}
      finally { lock.unlock(); }
    }
  }
  
  
  @Override
  public DynaLoader reset() {
    lock.lock();
    try {
      jars.clear();
      this.close();
      this.loader = null;
      System.gc();
    }
    finally {
      lock.unlock();
    }
    return this;
  }
  
}
