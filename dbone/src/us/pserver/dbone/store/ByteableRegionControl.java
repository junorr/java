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

import us.pserver.dbone.store.Region;
import us.pserver.dbone.store.RegionControl;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import us.pserver.tools.Iterate;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/11/2017
 */
public class ByteableRegionControl implements RegionControl {
  
  public static final int BUFFER_SIZE = 4096;
  

  private final RegionControl control;
  
  
  public ByteableRegionControl(RegionControl rgc) {
    this.control = NotNull.of(rgc).getOrFail("Bad null RegionControl");
  }


  @Override
  public boolean offer(Region reg) {
    return control.offer(reg);
  }


  @Override
  public boolean discard(Region reg) {
    return control.discard(reg);
  }


  @Override
  public Region allocate() {
    return control.allocate();
  }


  @Override
  public Iterator<Region> freeRegions() {
    return control.freeRegions();
  }
  
  
  @Override
  public int size() {
    return control.size();
  }
  

  public ByteableRegionControl readFrom(Path path) throws IOException {
    if(path == null || !Files.exists(path)) {
      throw new IllegalArgumentException("Bad file path: "+ path);
    }
    return readFrom(FileChannel.open(path, StandardOpenOption.READ));
  }
  
  
  public ByteableRegionControl writeTo(Path path) throws IOException {
    if(path == null) {
      throw new IllegalArgumentException("Bad file path: "+ path);
    }
    return writeTo(FileChannel.open(path, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING
    ));
  }
  
  
  public ByteableRegionControl writeTo(WritableByteChannel channel) throws IOException {
    NotNull.of(channel).failIfNull("Bad null Channel");
    try {
      ByteBuffer bb = ByteBuffer.allocate(control.size() * Region.BYTES);
      Iterate.on(control.freeRegions()).go(r->r.writeTo(bb));
      bb.flip();
      channel.write(bb);
    }
    finally {
      channel.close();
    }
    return this;
  }
  

  public ByteableRegionControl readFrom(ReadableByteChannel channel) throws IOException {
    NotNull.of(channel).failIfNull("Bad null Channel");
    try {
      ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);
      while(channel.read(bb) != -1) {
        bb.flip();
        while(bb.remaining() >= Region.BYTES) {
          control.offer(Region.of(bb));
        }
        bb.compact();
      }
    }
    finally {
      channel.close();
    }
    return this;
  }
  
}
