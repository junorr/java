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

package us.pserver.redfs;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Iterator;
import java.util.Set;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/11/2013
 */
public class LocalFileFactory {
  
  public static final String 
      CREATION_TIME = "creationTime",
      LAST_MODIFIED_TIME = "lastModifiedTime";
      

  public static RemoteFile create(String path) {
    return create(Paths.get(path));
  }
  
  
  public static RemoteFile create(Path path) {
    if(path == null || !Files.exists(path))
      return null;
    RemoteFile rf = new RemoteFile();
    define(rf, path);
    return rf;
  }
  
  
  public static boolean define(RemoteFile rf, Path path) {
    if(path == null || !Files.exists(path) || rf == null)
      return false;
    try {
      Path name = path.getFileName();
      rf.setName((name != null ? name.toString() : path.toString()))
          .setPath(path.toString())
          .setSize(new Size(Files.size(path)))
          .setCreationTime(((FileTime) 
          Files.getAttribute(path, CREATION_TIME, 
          LinkOption.NOFOLLOW_LINKS)).toMillis())
          .setLastModifiedTime(((FileTime) 
          Files.getAttribute(path, LAST_MODIFIED_TIME, 
          LinkOption.NOFOLLOW_LINKS)).toMillis())
          .setDirectory(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
          .setReadable(Files.isReadable(path))
          .setWritable(Files.isWritable(path));
    } catch(IOException ex) {
      return false;
    }
    try {
      rf.setIcon(FileSystemView
          .getFileSystemView()
          .getSystemIcon(path.toFile()));
    } catch(Exception ex) {
      System.out.println("# Error getting icon: "+ ex);
    }
    try {
      rf.setHidden(Files.isHidden(path));
    } catch(Exception ex) {}
    try {
      rf.setPermissions(Files.getPosixFilePermissions(
          path, LinkOption.NOFOLLOW_LINKS));
    } catch(Exception ex) {}
    try {
      rf.setOwner(Files.getOwner(path, 
          LinkOption.NOFOLLOW_LINKS).getName());
    } catch(Exception ex) {}
    try {
      rf.setHostAddress(InetAddress.getLocalHost().getHostAddress());
      rf.setHostName(InetAddress.getLocalHost().getHostName());
    } catch(Exception ex) {}
    return true;
  }
  
  
  public static void main(String[] args) {
    RemoteFile rf = create("f:/Monografia.docx");
    System.out.println("* "+ rf);
    System.out.println("* host addr    : "+ rf.getHostAddress());
    System.out.println("* host name    : "+ rf.getHostName());
    System.out.println("* creation time: "+ rf.getCreationDate());
    System.out.println("* last modified: "+ rf.getLastModifiedDate());
    System.out.println("* isDirectory  : "+ rf.isDirectory());
    System.out.println("* isHidden     : "+ rf.isHidden());
    System.out.println("* isReadable   : "+ rf.isReadable());
    System.out.println("* isWritable   : "+ rf.isWritable());
    System.out.println("* owner        : "+ rf.getOwner());
    Set<PosixFilePermission> perms = rf.getPermissions();
    if(perms != null) {
      Iterator<PosixFilePermission> it = perms.iterator();
      while(it.hasNext())
        System.out.println("* permission: "+ it.next().name());
    }
  }
  
}
