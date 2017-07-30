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
  
  public static final int DEFATUL_CACHE_SIZE = 64;
  
  private final DirectoryWatcher watcher;
  
  private final Map<String,CachedObject> cache;
  
  
  public DefaultObjectBox(Path classpath) {
    watcher = new DirectoryWatcherImpl(classpath).start();
    cache = Collections.synchronizedMap(new TreeMap<>());
  }
  

  @Override
  public List<String> listClasses() throws IOException {
    List<String> ls = new ArrayList<>();
    List<Path> jars = watcher.getDynaLoader().listJars();
    for(Path p : jars) {
      JarFile jf = new JarFile(p.toFile());
      jf.stream().map(e->e.getName()).forEach(ls::add);
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
  public Map<String, CachedObject> cache() {
    return cache;
  }
  
  
  private void cache(String name, Object obj) {
    if(cache.size() < DEFATUL_CACHE_SIZE) {
      cache.put(name, new DefaultCachedObject(obj));
    }
    else {
      Optional<CachedObject> opt = cache.values().stream().sorted().findFirst();
      Optional<Entry<String,CachedObject>> entry = cache.entrySet()
          .stream()
          .filter(e->e.getValue().equals(opt.get()))
          .findFirst();
      if(entry.isPresent()) {
        cache.remove(entry.get().getKey());
      }
      cache(name, obj);
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
    if(cache.containsKey(op.getName())) {
      return op.execute(cache.get(op.getName()).getObject());
    }
    else {
      return execute(op.getName(), op);
    }
  }
  
  
  private OpResult execute(String target, Operation op) {
    Optional opt = tryCreate(target);
    if(opt.isPresent()) {
      cache(target, opt.get());
      return op.execute(opt.get());
    }
    else {
      try {
        OpResult res = op.execute(watcher.getDynaLoader().load(target));
        if(res.getReturnValue().isPresent()) {
          cache(
              res.getReturnValue().get().getClass().getName(), 
              res.getReturnValue().get()
          );
        }
        return res;
      } catch(Exception e) {
        return OpResult.of(e);
      }
    }
  }

}
