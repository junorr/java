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

package us.pserver.tar;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Objects;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/10/2015
 */
public class TarPathData implements TarData, Iterator<TarPathData> {
  
  
  private final Path path;
  
  private Iterator<Path> paths;
  
  private TarArchiveEntry entry;
  
  
  public TarPathData(Path p) {
    Valid.off(p).forNull().fail(Path.class);
    path = p;
    entry = null;
  }
  
  
  public TarPathData(String path) {
    this(Paths.get(
        Valid.off(path).forEmpty()
            .getOrFail("Invalid Path name: "))
    );
  }


  @Override
  public InputStream getData() throws IOException {
    if(Files.isDirectory(path)) {
      return null;
    }
    return Files.newInputStream(path, StandardOpenOption.READ);
  }
  
  
  public boolean isDirectory() {
    return Files.isDirectory(path);
  }
  
  
  public boolean exists() {
    return Files.exists(path);
  }


  @Override
  public TarArchiveEntry getEntry() {
    if(entry == null) {
      entry  = new TarArchiveEntry(path.toFile());
    }
    return entry;
  }


  @Override
  public boolean hasNext() {
    if(!Files.isDirectory(path)) {
      return false;
    }
    if(paths == null) {
      try {
        paths = Files.walk(path).iterator();
        paths.next();
      } catch(IOException e) {
        throw new RuntimeException(e);
      }
    }
    return paths.hasNext();
  }


  @Override
  public TarPathData next() {
    if(!isDirectory() || !hasNext()) {
      return null;
    }
    return new TarPathData(paths.next());
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 19 * hash + Objects.hashCode(this.path);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TarPathData other = (TarPathData) obj;
    if (!Objects.equals(this.path, other.path)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "TGZPathData{" + "path=" + path + ", isDir=" + isDirectory() + '}';
  }

  
  public static void main(String[] args) {
    String spath = "D:/videos";
    TarPathData data = new TarPathData(spath);
    System.out.println("* "+ data);
    while(data.hasNext()) {
      TarPathData t1 = data.next();
      System.out.println("  -[w1] "+ t1);
    }
  }
  
}
