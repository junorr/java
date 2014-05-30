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


/**
 *
 * @author juno
 */
public class RemoteFile {
  
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
  
  
  public RemoteFile() {}
  
  
  public RemoteFile(String fullPath) {
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


  public RemoteFile setName(String name) {
    this.name = name;
    return this;
  }


  public String getPath() {
    return path;
  }


  public RemoteFile setPath(String path) {
    this.path = path;
    return this;
  }


  public Icon getIcon() {
    return icon;
  }


  public RemoteFile setIcon(Icon icon) {
    this.icon = icon;
    return this;
  }


  public Size getSize() {
    return size;
  }


  public RemoteFile setSize(Size size) {
    this.size = size;
    return this;
  }


  public long getLastModifiedTime() {
    return lastModifiedTime;
  }


  public RemoteFile setLastModifiedTime(long lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
    return this;
  }
  
  
  public Date getLastModifiedDate() {
    return new Date(lastModifiedTime);
  }


  public long getCreationTime() {
    return creationTime;
  }


  public RemoteFile setCreationTime(long creationTime) {
    this.creationTime = creationTime;
    return this;
  }
  
  
  public Date getCreationDate() {
    return new Date(creationTime);
  }


  public boolean isDirectory() {
    return directory;
  }


  public RemoteFile setDirectory(boolean directory) {
    this.directory = directory;
    return this;
  }


  public boolean isHidden() {
    return hidden;
  }


  public RemoteFile setHidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }


  public boolean isWritable() {
    return writable;
  }


  public RemoteFile setWritable(boolean writable) {
    this.writable = writable;
    return this;
  }


  public boolean isReadable() {
    return readable;
  }


  public RemoteFile setReadable(boolean readable) {
    this.readable = readable;
    return this;
  }


  public Set<PosixFilePermission> getPermissions() {
    return permissions;
  }


  public RemoteFile setPermissions(Set<PosixFilePermission> permissions) {
    this.permissions = permissions;
    return this;
  }


  public String getOwner() {
    return owner;
  }


  public RemoteFile setOwner(String owner) {
    this.owner = owner;
    return this;
  }


  public String getHostAddress() {
    return hostAddr;
  }


  public RemoteFile setHostAddress(String host) {
    this.hostAddr = host;
    return this;
  }


  public String getHostName() {
    return hostName;
  }


  public RemoteFile setHostName(String hostName) {
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
    final RemoteFile other = (RemoteFile) obj;
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
