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

package com.jpower.dsync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 23/04/2013
 */
public class WatchServiceTest {

  
  
  public static void main(String[] args) throws IOException, InterruptedException {
    Path pt = Paths.get("F:/");
    Files.exists(pt);
    //Files.createDirectory(pt);
    
    Path p = Paths.get("F:/");
    WatchService watcher = p.getFileSystem().newWatchService();
    p.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE);
    
    System.out.println("Watching...");
    while(true) {
      WatchKey key = watcher.take();
      List<WatchEvent<?>> events = key.pollEvents();
      for(WatchEvent e : events) {
        if(e.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
          System.out.println("ENTRY_MODIFY in "+ p+ "\\"+ e.context());
        else if(e.kind() == StandardWatchEventKinds.ENTRY_DELETE)
          System.out.println("ENTRY_DELETE in "+ p+ "\\"+ e.context());
        else if(e.kind() == StandardWatchEventKinds.ENTRY_CREATE)
          System.out.println("ENTRY_CREATE in "+ p+ "\\"+ e.context());
      }
      key.reset();
      if(!key.isValid()) break;
    }
    System.out.println("Exiting...");
  }
  
  
}
