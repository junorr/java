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

package us.pserver.zip;

import us.pserver.zip.utils.ZipConst;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.chk.Checker.nullpath;
import us.pserver.listener.ProgressContainer;
import us.pserver.listener.ProgressListener;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/08/2014
 */
public class Unzip {

  private Path zip;
  
  private Path out;
  
  private ProgressContainer cont;
  
  
  public Unzip() {
    zip = null;
    out = null;
    cont = new ProgressContainer();
  }
  
  
  public Unzip addListener(ProgressListener pl) {
    cont.add(pl);
    return this;
  }
  
  
  public ProgressContainer listeners() {
    return cont;
  }
  
  
  public Unzip clear() {
    zip = null;
    out = null;
    return this;
  }
  
  
  public Unzip input(Path p) {
    nullpath(p);
    zip = p;
    return this;
  }
  
  
  public Unzip input(String s) {
    return input(IO.p(s));
  }
  
  
  public Path input() {
    return zip;
  }
  
  
  public Unzip output(Path p) {
    nullarg(Path.class, p);
    out = p;
    return this;
  }
  
  
  public Unzip output(String s) {
    return output(IO.p(s));
  }
  
  
  public Path output() {
    return out;
  }
  
  
  public List<ZipEntry> listEntries() throws IOException {
    ZipInputStream zis = new ZipInputStream(IO.is(zip));
    List<ZipEntry> ls = new LinkedList<>();
    ZipEntry ze = zis.getNextEntry();
    while(ze != null) {
      ze.getCompressedSize();
      ze.getCrc();
      ze.getCreationTime();
      ze.getLastModifiedTime();
      ze.getSize();
      ls.add(ze);
      zis.closeEntry();
      ze = zis.getNextEntry();
    }
    zis.close();
    return ls;
  }
  
  
  public void run() throws IOException {
    List<ZipEntry> entries = listEntries();
    if(entries.size() > 1 && !Files.exists(out))
      Files.createDirectories(out);
    
    cont.setMax(ZipConst.totalUnzipSize(entries));
    ZipInputStream zis = new ZipInputStream(IO.is(zip));
    if(out == null)
      out = zip.getParent();
    ZipEntry ze = zis.getNextEntry();
    while(ze != null) {
      Path z = IO.p(ze.getName());
      Path p = out;
      if(Files.exists(out) && Files.isDirectory(out)) {
        p = out.resolve(ZipConst.excludeEqualsParts(out, z));
      }
      if(p.getParent() != null && !Files.exists(p.getParent()))
        Files.createDirectories(p.getParent());
      cont.update(p);
      OutputStream os = IO.os(p);
      ZipConst.transfer(zis, os, cont);
      os.flush();
      os.close();
      zis.closeEntry();
      ze = zis.getNextEntry();
    }
    zis.close();
  }
  
}
