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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/10/2015
 */
public class TarReader implements Iterator<TarData> {

  
  private final InputStream input;
  
  private TarArchiveInputStream tar;
  
  private TarArchiveEntry currentry;
  
  
  public TarReader(InputStream in) {
    input = Valid.off(in).forNull()
        .getOrFail(InputStream.class);
    currentry = null;
  }


  @Override
  public boolean hasNext() {
    if(tar == null) {
      tar = new TarArchiveInputStream(input);
    }
    try {
      currentry = tar.getNextTarEntry();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return currentry != null;
  }


  @Override
  public TarData next() {
    if(currentry == null) return null;
    TarData data = null;
    if(currentry.isDirectory()) {
      data = new TarEmptyData(currentry.getName()).setEntry(currentry);
    }
    else {
      LimitedSizeInputStream lsin = 
          new LimitedSizeInputStream(tar, currentry.getSize());
      return new TarStreamData(lsin, currentry.getName())
          .setEntry(currentry);
    }
    return data;
  }
  
  
  public void close() throws IOException {
    tar.close();
  }
  
  
  public TarReader untar(String path) throws IOException {
    return this.untar(
        Paths.get(Valid.off(path).forEmpty()
            .getOrFail("Invalid path: "))
    );
  }
  
  
  public TarReader untar(Path path) throws IOException {
    Valid.off(path).forNull().fail("Invalid path: ");
    Valid.off(path).forTest(
        Files.exists(path) && !Files.isDirectory(path)
    ).fail("Path must be a directory");
    if(!Files.exists(path)) {
      Files.createDirectories(path);
    }
    while(hasNext()) {
      TarData data = next();
      if(data.getEntry().isDirectory()) {
        mkdir(path, data.getEntry().getName());
      }
      else {
        Path file = path.resolve(data.getEntry().getName());
        mkdir(file.getParent(), null);
        OutputStream out = Files.newOutputStream(file, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.WRITE
        );
        OutputConnector con = new OutputConnector(out);
        con.connect(data.getData(), data.getEntry().getSize());
        out.close();
      }
    }
    return this;
  }
  
  
  private void mkdir(Path base, String name) throws IOException {
    if(base == null) return;
    Path p = base;
    if(name != null) {
      p = base.resolve(name);
    }
    if(!Files.exists(p)) {
      Files.createDirectories(p);
    }
  }

  
  public static void main(String[] args) throws IOException {
    String src = "D:/test2.tar";
    TarReader tr = new TarReader(new FileInputStream(src));
    while(tr.hasNext()) {
      System.out.println("- "+ tr.next().getEntry().getName());
    }
    tr.close();
    System.out.println("--------------------");
    tr = new TarReader(new FileInputStream(src));
    tr.untar("D:/tarReader").close();
  }
  
  
}
