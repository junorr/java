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

package br.com.bb.disec.micro.box.def;

import br.com.bb.disec.micro.box.ObjectBox;
import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.box.Operation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import us.pserver.dyna.DirectoryWatcher;
import us.pserver.dyna.impl.DirectoryWatcherImpl;
import us.pserver.tools.rfl.ReflectorException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class DefaultObjectBox implements ObjectBox {
  
  public static final int DEFATUL_CACHE_SIZE = 128;
  
  private final DirectoryWatcher watcher;
  
  private final Map<String,CachedObject> cache;
  
  private final ReentrantLock lock;
  
  
  public DefaultObjectBox(Path classpath) {
    watcher = new DirectoryWatcherImpl(classpath).start();
    cache = Collections.synchronizedMap(new TreeMap<>());
    lock = new ReentrantLock();
  }
  

  @Override
  public List<String> listClasses(String jar) throws IOException {
    List<String> ls = new ArrayList<>();
    List<Path> jars = watcher.getDynaLoader().listJars();
    for(Path p : jars) {
      if(p.getFileName().toString().equals(jar)) {
        JarFile jf = new JarFile(p.toFile());
        jf.stream().filter(e->!e.isDirectory()).map(e->e.getName()).forEach(ls::add);
      }
    }
    return ls;
  }


  @Override
  public List<String> listJars() {
    return watcher.getDynaLoader()
        .listJars().stream()
        .map(p->p.toAbsolutePath().toString())
        .collect(Collectors.toList());
  }
  
  
  @Override
  public Class load(String className) {
    return watcher.getDynaLoader().load(className);
  }


  @Override
  public Map<String, CachedObject> cache() {
    return Collections.unmodifiableMap(cache);
  }
  
  
  private void cache(String name, Object obj) {
    lock.lock();
    try {
      if(cache.size() >= DEFATUL_CACHE_SIZE) {
        Optional<Entry<String,CachedObject>> entry = cache.entrySet()
            .stream()
            .sorted((a,b)->a.getValue().compareTo(b.getValue()))
            .findFirst();
        if(entry.isPresent()) {
          cache.remove(entry.get().getKey());
        }
      }
      cache.put(name, new DefaultCachedObject(obj));
    }
    finally {
      lock.unlock();
    }
  }
  
  
  private Optional tryCreate(String name) {
    try {
      return Optional.ofNullable(watcher.getDynaLoader().loadAndCreate(name));
    }
    catch(ReflectorException ex) {
      return Optional.empty();
    }
  }


  @Override
  public OpResult execute(Operation op) {
    lock.lock();
    try {
     if(cache.containsKey(op.getName())) {
        return op.execute(cache.get(op.getName()).getObject());
      }
      else {
        return execute(op.getName(), op);
      } 
    } finally {
      lock.unlock();
    }
  }
  
  
  private OpResult execute(String target, Operation op) {
    lock.lock();
    try {
      Optional opt = tryCreate(target);
      if(opt.isPresent()) {
        cache(target, opt.get());
        return op.execute(opt.get());
      }
      else {
        OpResult res = op.execute(watcher.getDynaLoader().load(target));
        if(res.getValue().isPresent()) {
          cache(
              res.getValue().get().getClass().getName(), 
              res.getValue().get()
          );
        }
        return res;
      }
    } 
    catch(Exception e) {
      return OpResult.of(e);
    }
    finally {
      lock.unlock();
    }
  }

}
