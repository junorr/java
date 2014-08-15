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
import us.pserver.zip.utils.DirScan;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.listener.ProgressContainer;
import us.pserver.listener.ProgressListener;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/08/2014
 */
public class Zip {
    
  private final List<Path> srcs;
    
  private Path out;
    
  private boolean includeDir;
   
  private ProgressContainer cont;
    
    
  public Zip() {
    super();
    srcs = new LinkedList<>();
    out = null;
    includeDir = false;
    cont = new ProgressContainer();
  }
    
    
  public Zip addListener(ProgressListener pl) {
    cont.add(pl);
    return this;
  }
  
    
  public ProgressContainer listeners() {
    return cont;
  }
    
    
  public Zip add(Path p) {
    if(p != null && Files.exists(p)) {
      if(!Files.isDirectory(p))
        srcs.add(p);
      else {
        DirScan ds = new DirScan(p);
        srcs.addAll(ds.scan(false));
        includeDir = true;
      }
    }
    return this;
  }
    
    
  public Zip add(String s) {
    return this.add(IO.p(s));
  }
  
  
  public Zip clear() {
    srcs.clear();
    out = null;
    includeDir = false;
    return this;
  }
    
    
  public List<Path> sources() {
    return srcs;
  }
    
    
  public Zip output(Path p) {
    nullarg(Path.class, p);
    out = p;
    return this;
  }
    
    
  public Zip output(String s) {
    return this.output(IO.p(s));
  }
    
    
  public Path output() {
    return out;
  }
    
    
  private boolean contains(Path path, Path part) {
    if(path == null || part == null) 
      return false;
    if(path.equals(part)) return true;
    Path parent = path.getParent();
    while(parent != null && parent.getFileName() != null) {
      if(parent.getFileName().equals(part.getFileName()))
        return true;
      parent = parent.getParent();
    }
    return false;
  }
    
    
  private Path findCommonBase(Path base) {
    if(srcs.size() <= 1) return null;
    if(base == null) return base;
    boolean common = true;
    for(Path p : srcs) {
      common = common 
          && p.getParent() != null
          && contains(p, base);
    }
    if(common)
      return base;
    else
      return findCommonBase(base.getParent());
  }
    
    
  private ZipEntry createZipEntry(Path base, Path p) throws IOException {
    if(p == null) return null;
    if(base == null) base = out.getParent();
    String se = (base == null ? p.toString() : base.relativize(p).toString());
    if(includeDir && base != null) {
      se = base.getFileName().toString() + "/" + se;
    }
    System.out.println("* se="+ se);
    ZipEntry ze = new ZipEntry(se);
      
    ze.setCrc(ZipConst.getCRC32(p));
    ze.setCreationTime(FileTime.fromMillis(
        System.currentTimeMillis()));
    Object att = Files.getAttribute(p, "lastModifiedTime");
    if(att != null && att instanceof FileTime) {
      ze.setLastModifiedTime((FileTime) att);
    }
    ze.setSize(Files.size(p));
    System.out.println("* ZipEntry="+ ze);
    return ze;
  }
    
    
  public void run() throws IOException {
    if(out == null)
      throw new IllegalStateException("Zip output not configured");
    if(srcs.isEmpty())
      throw new IllegalStateException("Zip source not configured");
      
    ZipOutputStream zos = new ZipOutputStream(IO.os(out));
    cont.setMax(ZipConst.totalSize(srcs));
    Path base = findCommonBase(srcs.get(0).getParent());
    System.out.println("* base="+ base);
    Iterator<Path> it = srcs.iterator();
    
    while(it.hasNext()) {
      Path p = it.next();
      cont.update(p);
      zos.putNextEntry(createZipEntry(base, p));
      InputStream is = IO.is(p);
      ZipConst.transfer(is, zos, cont);
      zos.closeEntry();
      is.close();
    }
    zos.flush();
    zos.close();
    this.clear();
  }
  
}
