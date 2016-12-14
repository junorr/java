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

package oodb.tests.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class FPath implements IFPath {

  private final String path;
  
  private final String owner;
  
  private final String group;
  
  private final IFTime time;
  
  private final int perms;
  
  private final IFSize size;
  
  private final boolean isdir;


  public FPath(String path, String owner, String group, IFTime time, int perms, IFSize size, boolean isdir) {
    this.path = path;
    this.owner = owner;
    this.group = group;
    this.time = time;
    this.perms = perms;
    this.size = size;
    this.isdir = isdir;
  }
  
  
  @Override
  public String getName() {
    return toPath().getFileName().toString();
  }


  @Override
  public String getPath() {
    return path;
  }


  @Override
  public String[] split() {
    return path.replace("\\", "/").split("/");
  }


  @Override
  public Path toPath() {
    return Paths.get(path);
  }


  @Override
  public File toFile() {
    return toPath().toFile();
  }


  @Override
  public IFPath cd() throws IOException {
    return IFPath.from(toPath().getParent());
  }


  @Override
  public IFPath cd(String spath) throws IOException {
    return IFPath.from(toPath().resolve(spath));
  }
  
  @Override
  public IFPath cd(IFPath path) throws IOException {
    return IFPath.from(toPath().resolve(getName()));
  }


  @Override
  public List<IFPath> ls() throws IOException {
    List<IFPath> ls = new ArrayList<>();
    Files.list(toPath()).forEach(p->ls.add(IFPath.from(p)));
    return ls;
  }


  @Override
  public boolean isDirectory() {
    return isdir;
  }


  @Override
  public boolean isRoot() {
    return toPath().getParent() == null;
  }


  @Override
  public IFSize size() {
    return size;
  }


  @Override
  public IFPermissions getPermissions() {
    return IFPermissions.fromPosixBin(perms);
  }


  @Override
  public IFTime getTime() {
    return time;
  }


  @Override
  public String getOwner() {
    return owner;
  }


  @Override
  public String getGroup() {
    return group;
  }
  
  
  @Override
  public String toString() {
    return new StringBuilder()
        .append((isDirectory() ? 'd' : '-'))
        .append(getPermissions())
        .append(StringPad.of(getOwner()).lpad(" ", 10))
        .append(StringPad.of(getGroup()).lpad(" ", 10))
        .append(StringPad.of(String.valueOf(size())).lpad(" ", 12))
        .append(StringPad.of(getTime().getLastModifiedTime().toString()).lpad(" ", 26))
        .append("   ")
        .append(getName())
        .toString();
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  public static Builder builder(Path path) {
    return new Builder(path);
  }
  
  
  
  
  public static class Builder {
  
    private String spath;

    private String owner;

    private String group;

    private IFTime time;

    private IFPermissions perms;

    private IFSize size;
    
    private boolean isdir;
    
    private final Path path;
    
    
    public Builder() {
      spath = "";
      owner = "";
      group = "";
      Instant now = Instant.now();
      time = new FTime(now, now, now);
      perms = new FPermissions(Collections.EMPTY_SET);
      size = IFSize.ZERO;
      path = null;
    }
    
    
    public Builder(Path p) {
      Objects.requireNonNull(p, "Bad Null Path");
      this.path = p;
    }
    
    
    public String getPath() {
      return spath;
    }


    public Builder setPath(String path) {
      this.spath = path;
      return this;
    }


    public String getOwner() {
      return owner;
    }


    public Builder setOwner(String owner) {
      this.owner = owner;
      return this;
    }


    public String getGroup() {
      return group;
    }


    public Builder setGroup(String group) {
      this.group = group;
      return this;
    }


    public IFTime getTime() {
      return time;
    }


    public Builder setTime(IFTime time) {
      this.time = time;
      return this;
    }


    public IFPermissions getPerms() {
      return perms;
    }


    public Builder setPerms(IFPermissions perms) {
      this.perms = perms;
      return this;
    }


    public IFSize getSize() {
      return size;
    }


    public Builder setSize(IFSize size) {
      this.size = size;
      return this;
    }


    public boolean isDirectory() {
      return isdir;
    }


    public Builder setIsDirectory(boolean isdir) {
      this.isdir = isdir;
      return this;
    }
    
    
    public IFPath create(PosixFileAttributes atts) {
      return new FPath(
          path.toAbsolutePath().toString(), 
          atts.owner().toString(), 
          atts.group().toString(), 
          IFTime.from(atts), 
          IFPermissions.fromPosixAttrs(atts).toPosixBin(), 
          IFSize.from(atts), 
          atts.isDirectory()
      );
    }
    
    
    public IFPath create(DosFileAttributes atts) {
      return new FPath(
          path.toAbsolutePath().toString(), 
          System.getProperty("user.name"),
          "-", 
          IFTime.from(atts), 
          IFPermissions.fromDosAttrs(atts).toPosixBin(), 
          IFSize.from(atts), 
          atts.isDirectory()
      );
    }
    
    
    public IFPath create() throws IOException {
      IFPath fpath;
      if(this.path != null) {
        try {
          fpath = this.create(Files.readAttributes(path, PosixFileAttributes.class));
        }
        catch(UnsupportedOperationException e) {
          fpath = this.create(Files.readAttributes(path, DosFileAttributes.class));
        }
      }
      else {
        fpath = new FPath(spath, owner, group, time, perms.toPosixBin(), size, isdir);
      }
      return fpath;
    }
    
  }

}
