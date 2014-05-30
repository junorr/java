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

package com.jpower.dsync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/05/2013
 */
public class FileAttributes {
  
  public static final String
      
      ATTR_DOS_HIDDEN = "dos:hidden",
      
      ATTR_DOS_READONLY = "dos:readonly",
      
      ATTR_DOS_SYSTEM = "dos:system",
      
      ATTR_DOS_ARCHIVE = "dos:archive",
      
      ATTR_LAST_MODIFIED_TIME = "lastModifiedTime",
      
      ATTR_LAST_ACCESS_TIME = "lastAccessTime",
      
      ATTR_CREATION_TIME = "creationTime",
      
      ATTR_POSIX_GROUP = "posix:group";
      

  public static void copyAll(Path src, Path dst) throws IOException {
    if(src == null || !Files.exists(src))
      throw new IllegalArgumentException(
          "Invalid src path: "+ src);
    if(dst == null || !Files.exists(dst))
      throw new IllegalArgumentException(
          "Invalid dst path: "+ dst);
    
    setCreationTime(dst, getCreationTime(src));
    setDosArchive(dst, getDosArchive(src));
    setDosHidden(dst, getDosHidden(src));
    setDosReadOnly(dst, getDosReadOnly(src));
    setDosSystem(dst, getDosSystem(src));
    setLastAccessTime(dst, getLastAccessTime(src));
    setLastModifiedTime(dst, getLastModifiedTime(src));
    setPosixGroup(dst, getPosixGroup(src));
    setPosixPermissions(dst, getPosixPermissions(src));
    setOwner(dst, getOwner(src));
  }
  
  
  public static boolean getDosReadOnly(Path p) throws IOException {
    if(p == null) return false;
    return (Boolean) Files.getAttribute(p, ATTR_DOS_READONLY);
  }
  
  
  public static boolean getDosHidden(Path p) throws IOException {
    if(p == null) return false;
    return (Boolean) Files.getAttribute(p, ATTR_DOS_HIDDEN);
  }
  
  
  public static boolean getDosArchive(Path p) throws IOException {
    if(p == null) return false;
    return (Boolean) Files.getAttribute(p, ATTR_DOS_ARCHIVE);
  }
  
  
  public static boolean getDosSystem(Path p) throws IOException {
    if(p == null) return false;
    return (Boolean) Files.getAttribute(p, ATTR_DOS_SYSTEM);
  }
  
  
  public static long getLastModifiedTime(Path p) throws IOException {
    if(p == null) return -1;
    FileTime ft = (FileTime) Files.getAttribute(p, ATTR_LAST_MODIFIED_TIME);
    if(ft == null) return -1;
    return ft.toMillis();
  }
  
  
  public static long getLastAccessTime(Path p) throws IOException {
    if(p == null) return -1;
    FileTime ft = (FileTime) Files.getAttribute(p, ATTR_LAST_ACCESS_TIME);
    if(ft == null) return -1;
    return ft.toMillis();
  }
  
  
  public static long getCreationTime(Path p) throws IOException {
    if(p == null) return -1;
    FileTime ft = (FileTime) Files.getAttribute(p, ATTR_CREATION_TIME);
    if(ft == null) return -1;
    return ft.toMillis();
  }
  
  
  public static String getOwner(Path p) throws IOException {
    if(p == null) return null;
    UserPrincipal up = (UserPrincipal) Files.getOwner(p);
    if(up == null) return null;
    return up.getName();
  }
  
  
  public static Set<PosixFilePermission> getPosixPermissions(Path p) throws IOException {
    if(p == null) return null;
    return Files.getPosixFilePermissions(p);
  }
  
  
  public static void printSet(Set s) {
    for(Object o : s) {
      System.out.print(" - "+ o.getClass().toString()+ " : ");
      System.out.println(o);
    }
  }
  
  
  public static String getPosixGroup(Path p) throws IOException {
    if(p == null) return null;
    GroupPrincipal gp = (GroupPrincipal) Files.getAttribute(p, ATTR_POSIX_GROUP);
    if(gp == null) return null;
    return gp.getName();
  }
  
  
  public static void setDosReadOnly(Path p, boolean readonly) throws IOException {
    if(p == null) return;
    Files.setAttribute(p, ATTR_DOS_READONLY, readonly);
  }
  
  
  public static void setDosHidden(Path p, boolean hidden) throws IOException {
    if(p == null) return;
    Files.setAttribute(p, ATTR_DOS_HIDDEN, hidden);
  }
  
  
  public static void setDosArchive(Path p, boolean archive) throws IOException {
    if(p == null) return;
    Files.setAttribute(p, ATTR_DOS_ARCHIVE, archive);
  }
  
  
  public static void setDosSystem(Path p, boolean system) throws IOException {
    if(p == null) return;
    Files.setAttribute(p, ATTR_DOS_SYSTEM, system);
  }
  
  
  public static void setLastModifiedTime(Path p, long time) throws IOException {
    if(p == null || time <= 0) return;
    Files.setAttribute(p, ATTR_LAST_MODIFIED_TIME, 
        FileTime.fromMillis(time));
  }
  
  
  public static void setLastAccessTime(Path p, long time) throws IOException {
    if(p == null || time <= 0) return;
    Files.setAttribute(p, ATTR_LAST_ACCESS_TIME, 
        FileTime.fromMillis(time));
  }
  
  
  public static void setCreationTime(Path p, long time) throws IOException {
    if(p == null || time <= 0) return;
    Files.setAttribute(p, ATTR_CREATION_TIME, 
        FileTime.fromMillis(time));
  }
  
  
  public static void setOwner(Path p, final String owner) throws IOException {
    if(p == null || owner == null || owner.trim().isEmpty()) 
      return;
    UserPrincipal up = new UserPrincipal() {
      @Override public String getName() {
        return owner;
      }
    };
    Files.setOwner(p, up);
  }
  
  
  public static void setPosixPermissions(Path p, Set<PosixFilePermission> perm) throws IOException {
    if(p == null || perm == null || perm.isEmpty()) 
      return;
    Files.setPosixFilePermissions(p, perm); 
  }
  
  
  public static void setPosixGroup(Path p, final String group) throws IOException {
    if(p == null || group == null || group.trim().isEmpty()) 
      return;
    GroupPrincipal gp = new GroupPrincipal() {
      @Override public String getName() {
        return group;
      }
    };
    Files.setAttribute(p, ATTR_POSIX_GROUP, gp);
  }
  
  
  public static void main(String[] args) throws IOException {
    Path p = Paths.get("/home/juno/zzz/src/file-1.msi");
    System.out.println("* path: "+ p);
    System.out.println("* exists(): "+ Files.exists(p));
    System.out.println("* getDosReadOnly(): "+ getDosReadOnly(p));
    System.out.println("* getDosHidden(): "+ getDosHidden(p));
    System.out.println("* getDosArchive(): "+ getDosArchive(p));
    System.out.println("* getDosSystem(): "+ getDosSystem(p));
    System.out.println("* getLastModifiedTime(): "+ new Date(getLastModifiedTime(p)));
    System.out.println("* getLastAccessTime(): "+ new Date(getLastAccessTime(p)));
    System.out.println("* getCreationTime(): "+ new Date(getCreationTime(p)));
    System.out.println("* getOwner(): "+ getOwner(p));
    System.out.println("* getPosixGroup(): "+ getPosixGroup(p));
    System.out.println("* getPosixPermissions(): ");
    printSet(getPosixPermissions(p));
    
    Path q = Paths.get("/home/juno/zzz/src/file-3.txt");
    System.out.println("* path: "+ q);
    System.out.println("* exists(): "+ Files.exists(q));
    System.out.println("* getDosReadOnly(): "+ getDosReadOnly(q));
    System.out.println("* getDosHidden(): "+ getDosHidden(q));
    System.out.println("* getDosArchive(): "+ getDosArchive(q));
    System.out.println("* getDosSystem(): "+ getDosSystem(q));
    System.out.println("* getLastModifiedTime(): "+ new Date(getLastModifiedTime(q)));
    System.out.println("* getLastAccessTime(): "+ new Date(getLastAccessTime(q)));
    System.out.println("* getCreationTime(): "+ new Date(getCreationTime(q)));
    System.out.println("* getOwner(): "+ getOwner(q));
    System.out.println("* getPosixGroup(): "+ getPosixGroup(q));
    System.out.println("* getPosixPermissions(): ");
    printSet(getPosixPermissions(q));
    
    copyAll(p, q);
    System.out.println("* path: "+ p);
    System.out.println("* exists(): "+ Files.exists(p));
    System.out.println("* getDosReadOnly(): "+ getDosReadOnly(p));
    System.out.println("* getDosHidden(): "+ getDosHidden(p));
    System.out.println("* getDosArchive(): "+ getDosArchive(p));
    System.out.println("* getDosSystem(): "+ getDosSystem(p));
    System.out.println("* getLastModifiedTime(): "+ new Date(getLastModifiedTime(p)));
    System.out.println("* getLastAccessTime(): "+ new Date(getLastAccessTime(p)));
    System.out.println("* getCreationTime(): "+ new Date(getCreationTime(p)));
    System.out.println("* getOwner(): "+ getOwner(p));
    System.out.println("* getPosixGroup(): "+ getPosixGroup(p));
    System.out.println("* getPosixPermissions(): ");
    printSet(getPosixPermissions(p));
    
    System.out.println("* path: "+ q);
    System.out.println("* exists(): "+ Files.exists(q));
    System.out.println("* getDosReadOnly(): "+ getDosReadOnly(q));
    System.out.println("* getDosHidden(): "+ getDosHidden(q));
    System.out.println("* getDosArchive(): "+ getDosArchive(q));
    System.out.println("* getDosSystem(): "+ getDosSystem(q));
    System.out.println("* getLastModifiedTime(): "+ new Date(getLastModifiedTime(q)));
    System.out.println("* getLastAccessTime(): "+ new Date(getLastAccessTime(q)));
    System.out.println("* getCreationTime(): "+ new Date(getCreationTime(q)));
    System.out.println("* getOwner(): "+ getOwner(q));
    System.out.println("* getPosixGroup(): "+ getPosixGroup(q));
    System.out.println("* getPosixPermissions(): ");
    printSet(getPosixPermissions(q));
  }
  
}
