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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import us.pserver.dyna.DirectoryWatcher;
import us.pserver.dyna.DynaLoader;
import us.pserver.dyna.DynaLoaderInstance;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public class DirectoryWatcherImpl implements DirectoryWatcher, Runnable {
  
  private final Path dir;
  
  private final AtomicBoolean running;
  
  private final DynaLoader dyna;
  
  private final List<Consumer<DynaLoader>> listeners;
  
  
  public DirectoryWatcherImpl(Path path) {
    if(path == null || !Files.exists(path) || !Files.isDirectory(path)) {
      throw new IllegalArgumentException("Invalid directory: "+ path);
    }
    this.dir = path;
    this.running = new AtomicBoolean(false);
    this.dyna = new DynaLoaderInstance().register(path);
    this.listeners = Collections.synchronizedList(new ArrayList<>());
  }
  

  @Override
  public DirectoryWatcher start() {
    this.running.compareAndSet(false, true);
    ForkJoinPool.commonPool().submit(this);
    return this;
  }
  
  
  @Override
  public void run() {
    try {
      WatchService ws = dir.getFileSystem().newWatchService();
      dir.register(ws, 
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY
      );
      while(this.running.get()) {
        WatchKey key = ws.take();
        dyna.reset().register(dir);
        this.listeners.forEach(l->l.accept(dyna));
      }
    } 
    catch(IOException | InterruptedException ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }


  @Override
  public synchronized DirectoryWatcher stop() {
    this.running.compareAndSet(true, false);
    return this;
  }


  @Override
  public DynaLoader getDynaLoader() {
    return dyna;
  }


  @Override
  public Path getDirectory() {
    return dir;
  }
  
  
  @Override
  public DirectoryWatcher addChangeListener(Consumer<DynaLoader> lst) {
    if(lst != null) {
      this.listeners.add(lst);
    }
    return this;
  }
  
  
  @Override
  public DirectoryWatcher rmChangeListener(Consumer<DynaLoader> lst) {
    if(lst != null) {
      this.listeners.remove(lst);
    }
    return this;
  }
  
  
  @Override
  public DirectoryWatcher clearChangeListeners() {
    this.listeners.clear();
    return this;
  }
  
}
