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

package us.pserver.dbone.store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class PathStorage implements Storage {
  
  public static final int DEFAULT_BLOCK_SIZE = 1024;
  
  private final Path path;
  
  private final FileChannel channel;
  
  private final StorageHeader header;
  
  private final LinkedBlockingDeque<Region> freebs;
  
  
  public PathStorage(Path path, FileChannel ch, StorageHeader shd, LinkedBlockingDeque<Region> freeRegions) {
    this.path = Objects.requireNonNull(path, "Bad null Path");
    this.channel = Objects.requireNonNull(ch, "Bad null FileChannel");
    this.header = Objects.requireNonNull(shd, "Bad null StorageHeader");
    this.freebs = Objects.requireNonNull(freeRegions, "Bad null free regions Deque");
  }
  
  
  @Override
  public Region put(ByteBuffer... buf) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public ByteBuffer get(Region reg) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public ByteBuffer remove(Region reg) throws IOException {
    if(reg != null && reg.isValid()) {
      ByteBuffer buf = this.get(reg);
      freebs.add(reg);
      return buf;
    }
    return null;
  }


  @Override
  public long size() throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void close() throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  private Region nextFree() throws IOException {
    Region reg;
    if(!freebs.isEmpty()) {
      reg = freebs.poll();
    }
    else {
      reg = Region.of(channel.size(), header.getBlockSize());
    }
    return reg;
  }
  
  
}
