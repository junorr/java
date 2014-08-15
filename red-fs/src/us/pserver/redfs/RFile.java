/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.redfs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.swing.Icon;
import us.pserver.streams.IO;


/**
 *
 * @author juno
 */
public class RFile {
  
  private String name;
  
  private String path;
  
  private Icon icon;
  
  private Size size;
  
  private long lastModifiedTime;
  
  private long creationTime;
  
  private boolean directory;
  
  private boolean hidden;
  
  private boolean writable;
  
  private boolean readable;
  
  private Set<PosixFilePermission> permissions;
  
  private String owner;
  
  private String hostAddr;
  
  private String hostName;
  
  
  public RFile() {}
  
  
  public RFile(String fullPath) {
    if(fullPath != null && !fullPath.trim().isEmpty()) {
      Path p = Paths.get(fullPath);
      if(!Files.exists(p)) {
        this.setName(p.getFileName().toString())
            .setPath(p.toString());
      } 
      else {
        LocalFileFactory.define(this, p);
      }
    }
  }
  
  
  public String getName() {
    return name;
  }


  public RFile setName(String name) {
    this.name = name;
    return this;
  }


  public String getPath() {
    return path;
  }


  public RFile setPath(String path) {
    this.path = path;
    return this;
  }
  
  
  public Path toPath() {
    return IO.p(getPath());
  }


  public Icon getIcon() {
    return icon;
  }


  public RFile setIcon(Icon icon) {
    this.icon = icon;
    return this;
  }


  public Size getSize() {
    return size;
  }


  public RFile setSize(Size size) {
    this.size = size;
    return this;
  }


  public long getLastModifiedTime() {
    return lastModifiedTime;
  }


  public RFile setLastModifiedTime(long lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
    return this;
  }
  
  
  public Date getLastModifiedDate() {
    return new Date(lastModifiedTime);
  }


  public long getCreationTime() {
    return creationTime;
  }


  public RFile setCreationTime(long creationTime) {
    this.creationTime = creationTime;
    return this;
  }
  
  
  public Date getCreationDate() {
    return new Date(creationTime);
  }


  public boolean isDirectory() {
    return directory;
  }


  public RFile setDirectory(boolean directory) {
    this.directory = directory;
    return this;
  }


  public boolean isHidden() {
    return hidden;
  }


  public RFile setHidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }


  public boolean isWritable() {
    return writable;
  }


  public RFile setWritable(boolean writable) {
    this.writable = writable;
    return this;
  }


  public boolean isReadable() {
    return readable;
  }


  public RFile setReadable(boolean readable) {
    this.readable = readable;
    return this;
  }


  public Set<PosixFilePermission> getPermissions() {
    return permissions;
  }


  public RFile setPermissions(Set<PosixFilePermission> permissions) {
    this.permissions = permissions;
    return this;
  }


  public String getOwner() {
    return owner;
  }


  public RFile setOwner(String owner) {
    this.owner = owner;
    return this;
  }


  public String getHostAddress() {
    return hostAddr;
  }


  public RFile setHostAddress(String host) {
    this.hostAddr = host;
    return this;
  }


  public String getHostName() {
    return hostName;
  }


  public RFile setHostName(String hostName) {
    this.hostName = hostName;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.path);
    hash = 67 * hash + Objects.hashCode(this.size);
    hash = 67 * hash + (int) (this.lastModifiedTime ^ (this.lastModifiedTime >>> 32));
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
    final RFile other = (RFile) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.path, other.path)) {
      return false;
    }
    if (!Objects.equals(this.size, other.size)) {
      return false;
    }
    if (this.lastModifiedTime != other.lastModifiedTime) {
      return false;
    }
    return true;
  } 


  @Override
  public String toString() {
    return "RemoteFile{ " + "name=" + name + ", path=" + path + ", size=" + size + " }";
  }
  
}
