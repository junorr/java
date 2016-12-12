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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public class FSPath implements IFSPath {

  private final String path;
  
  private final String owner;
  
  private final String group;
  
  private final IFSTime time;
  
  private final IFSPermissions perms;
  
  private final ISize size;


  public FSPath(String path, String owner, String group, IFSTime time, IFSPermissions perms, ISize size) {
    this.path = path;
    this.owner = owner;
    this.group = group;
    this.time = time;
    this.perms = perms;
    this.size = size;
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
  public IFSPath cd() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public IFSPath cd(IFSPath path) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public List<IFSPath> ls() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public boolean isDirectory() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public boolean isRoot() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public ISize size() {
    return size;
  }


  @Override
  public IFSPermissions getPermissions() {
    return perms;
  }


  @Override
  public IFSTime getTime() {
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
  
  
  
  
  public static class Builder {
  
    private String path;

    private String owner;

    private String group;

    private IFSTime time;

    private IFSPermissions perms;

    private ISize size;
    
    
    public Builder() {
      path = "";
      owner = "";
      group = "";
      Instant now = Instant.now();
      time = new FSTime(now, now, now);
      perms = new FSPermissions(Collections.EMPTY_SET);
      size = ISize.ZERO;
    }
    
    
    public Builder(Path p) {
      Objects.requireNonNull(p, "Bad Null Path");
      if(!Files.exists(p)) {
        throw new IllegalArgumentException("Path does not exists: "+ p);
      }
      Posix
    }
    
    
    public String getPath() {
      return path;
    }


    public Builder setPath(String path) {
      this.path = path;
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


    public IFSTime getTime() {
      return time;
    }


    public Builder setTime(IFSTime time) {
      this.time = time;
      return this;
    }


    public IFSPermissions getPerms() {
      return perms;
    }


    public Builder setPerms(IFSPermissions perms) {
      this.perms = perms;
      return this;
    }


    public ISize getSize() {
      return size;
    }


    public Builder setSize(ISize size) {
      this.size = size;
      return this;
    }
    
    
    public IFSPath create() {
      return new FSPath(path, owner, group, time, perms, size);
    }
    
  }

}
