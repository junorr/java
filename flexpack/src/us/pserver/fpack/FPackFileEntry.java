/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import us.pserver.tools.FileSizeFormatter;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class FPackFileEntry extends FPackEntry {
  
  public static final String LAST_MOD_TIME = "lastModifiedTime";
  
  public static final String CREATION_TIME = "creationTime";
  
  public static final String DIRECTORY = "directory";
  
  public static final String OWNER = "owner";
  
  public static final String POSIX_GROUP = "group";
  
  public static final String POSIX_PERMISSIONS = "permissions";
  
  public static final String DOS_READ_ONLY = "readonly";
  
  public static final String DOS_HIDDEN = "hidden";
  
  public static final String PATH = "path";
  
  
  private transient Path path;
  
  
  protected FPackFileEntry() {
    super();
    path = null;
  }
  
  
  public FPackFileEntry(Path p) {
    super(Valid.off(p).forNull()
        .getOrFail(Path.class)
        .getFileName().toString()
    );
    path = p;
    this.put(PATH, path.toAbsolutePath().toString());
  }
  
  
  public Path getPath() {
    if(path != null) return path;
    if(this.getValues().containsKey(PATH))
      return Paths.get(this.get(PATH).toString());
    return null;
  }
  
  
  public boolean isDosHidden() {
    return this.getValues().containsKey(DOS_HIDDEN)
        && (boolean) this.get(DOS_HIDDEN);
  }
  
  
  public boolean isDosReadOnly() {
    return this.getValues().containsKey(DOS_READ_ONLY)
        && (boolean) this.get(DOS_READ_ONLY);
  }
  
  
  public boolean isDirectory() {
    return this.getValues().containsKey(DIRECTORY)
        && (boolean) this.get(DIRECTORY);
  }
  
  
  public boolean isFile() {
    return !isDirectory();
  }
  
  
  public UserPrincipal getOwner() throws IOException {
    return FileSystems.getDefault()
        .getUserPrincipalLookupService()
        .lookupPrincipalByName(getOwnerName());
  }
  
  
  public String getOwnerName() {
    if(!this.getValues().containsKey(OWNER))
      return null;
    return this.get(OWNER).toString();
  }
  
  
  public GroupPrincipal getGroup() throws IOException {
    return FileSystems.getDefault()
        .getUserPrincipalLookupService()
        .lookupPrincipalByGroupName(getGroupName());
  }
  
  
  public String getGroupName() {
    if(!this.getValues().containsKey(POSIX_GROUP))
      return null;
    return this.get(POSIX_GROUP).toString();
  }
  
  
  public long getLastModTime() {
    if(!this.getValues().containsKey(LAST_MOD_TIME))
      return -1;
    return (long) this.get(LAST_MOD_TIME);
  }
  
  
  public long getCreationTime() {
    if(!this.getValues().containsKey(CREATION_TIME))
      return -1;
    return (long) this.get(CREATION_TIME);
  }
  
  
  public int getPosixPermissions() {
    if(!this.getValues().containsKey(POSIX_PERMISSIONS))
      return -1;
    return (int) this.get(POSIX_PERMISSIONS);
  }
  
  
  public Set<PosixFilePermission> getPosixPermissions(int perms) {
    String sperm = String.valueOf(perms);
    byte owner = Byte.parseByte(
        String.valueOf(sperm.charAt(0))
    );
    byte group = Byte.parseByte(
        String.valueOf(sperm.charAt(1))
    );
    byte others = Byte.parseByte(
        String.valueOf(sperm.charAt(2))
    );
    List<PosixFilePermission> lperm = new LinkedList<>();
    parseOwnerPermission(owner, lperm);
    parseGroupPermission(group, lperm);
    parseOthersPermission(others, lperm);
    return new HashSet<PosixFilePermission>(lperm);
  }
  
  
  public void create() throws IOException {
    if(Files.exists(path)) return;
    if(isDirectory()) {
      Files.createDirectory(path);
    } else {
      Files.createFile(path);
    }
    this.setAttributes();
  }
  
  
  public void setAttributes() throws IOException {
    if(Files.exists(path)) return;
    if(this.getValues().containsKey(CREATION_TIME)) {
      Files.setAttribute(path, CREATION_TIME, 
          FileTime.fromMillis(this.getCreationTime())
      );
    }
    if(this.getValues().containsKey(LAST_MOD_TIME)) {
      Files.setAttribute(path, LAST_MOD_TIME, 
          FileTime.fromMillis(this.getLastModTime())
      );
    }
    if(this.getValues().containsKey(OWNER)) {
      Files.setAttribute(path, OWNER, this.getOwner());
    }
    if(OS.isUnix() || OS.isMacOS()) {
      if(this.getValues().containsKey(POSIX_GROUP)) {
        Files.setAttribute(path, OWNER, this.getGroup());
      }
      if(this.getValues().containsKey(POSIX_PERMISSIONS)) {
        Files.setPosixFilePermissions(path, 
            this.getPosixPermissions(
                this.getPosixPermissions())
        );
      }
    }
    else if(OS.isWindows()) {
      if(this.getValues().containsKey(DOS_HIDDEN)) {
        Files.setAttribute(path, 
            DOS_HIDDEN, this.isDosHidden()
        );
      }
      if(this.getValues().containsKey(DOS_READ_ONLY)) {
        Files.setAttribute(path, 
            DOS_READ_ONLY, this.isDosReadOnly()
        );
      }
    }
  }
  
  
  public void readPathAttributes() throws IOException {
    BasicFileAttributes bat = Files.getFileAttributeView(
        path, BasicFileAttributeView.class
    ).readAttributes();
    this.put(LAST_MOD_TIME, bat.lastModifiedTime().toMillis());
    this.put(CREATION_TIME, bat.creationTime().toMillis());
    this.put(DIRECTORY, bat.isDirectory());
    this.put(OWNER, Files.getOwner(path).getName());
    this.setSize(Files.size(path));
    if(OS.isUnix() || OS.isMacOS()) {
      PosixFileAttributes pat = Files.readAttributes(
          path, PosixFileAttributes.class
      );
      this.put(POSIX_GROUP, pat.group().getName());
      StringBuilder sperm = new StringBuilder();
      sperm.append(readOwnerPermissions(pat))
          .append(readGroupPermissions(pat))
          .append(readOthersPermissions(pat));
      this.put(POSIX_PERMISSIONS, Integer.parseInt(sperm.toString()));
    }
    if(OS.isWindows()) {
      DosFileAttributes dat = Files.readAttributes(
          path, DosFileAttributes.class
      );
      this.put(DOS_HIDDEN, dat.isHidden());
      this.put(DOS_READ_ONLY, dat.isReadOnly());
    }
  }
  
  
  public void printInfo(PrintStream ps) {
    final PrintStream out = (ps != null ? ps : System.out);
    out.print("* Path.........: ");
    out.println(getPath());
    out.print("* isDirectory..: ");
    out.println(isDirectory());
    out.print("* Size.........: ");
    out.println(new FileSizeFormatter().format(getSize()));
    out.print("* Creation Time: ");
    out.println(new Date(getCreationTime()));
    out.print("* Last Mod Time: ");
    out.println(new Date(getLastModTime()));
    out.print("* Owner........: ");
    out.println(getOwnerName());
    out.print("* Group........: ");
    out.println(getGroupName());
    out.print("* isDosHidden..: ");
    out.println(isDosHidden());
    out.print("* isDosReadOnly: ");
    out.println(isDosReadOnly());
    out.print("* Posix Perms..: ");
    out.println(getPosixPermissions());
    Set<PosixFilePermission> perms = getPosixPermissions(getPosixPermissions());
    perms.stream().sorted().forEach(
        p->out.println("  - "
            + p.name().toLowerCase())
        );
  }
  
  
  private byte readOwnerPermissions(PosixFileAttributes pat) throws IOException {
    if(pat != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : pat.permissions()) {
        switch(p) {
          case OWNER_READ:
            pmod += pcodes[0];
            break;
          case OWNER_WRITE:
            pmod += pcodes[1];
            break;
          case OWNER_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private byte readGroupPermissions(PosixFileAttributes pat) throws IOException {
    if(pat != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : pat.permissions()) {
        switch(p) {
          case GROUP_READ:
            pmod += pcodes[0];
            break;
          case GROUP_WRITE:
            pmod += pcodes[1];
            break;
          case GROUP_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private byte readOthersPermissions(PosixFileAttributes pat) throws IOException {
    if(pat != null) {
      byte pmod = 0;
      byte[] pcodes = {1,2,4};
      for(PosixFilePermission p : pat.permissions()) {
        switch(p) {
          case OTHERS_READ:
            pmod += pcodes[0];
            break;
          case OTHERS_WRITE:
            pmod += pcodes[1];
            break;
          case OTHERS_EXECUTE:
            pmod += pcodes[2];
            break;
        }
      }
      return pmod;
    }
    return 0;
  }
  
  
  private void parseOwnerPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.OWNER_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.OWNER_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.OWNER_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.OWNER_READ);
        lperm.add(PosixFilePermission.OWNER_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
    }
  }
  
  
  private void parseGroupPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.GROUP_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.GROUP_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.GROUP_WRITE);
        lperm.add(PosixFilePermission.OWNER_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.GROUP_READ);
        lperm.add(PosixFilePermission.GROUP_WRITE);
        lperm.add(PosixFilePermission.GROUP_EXECUTE);
        break;
    }
  }
  
  
  private void parseOthersPermission(byte perm, List<PosixFilePermission> lperm) {
    switch(perm) {
      case 1:
        lperm.add(PosixFilePermission.OTHERS_READ);
        break;
      case 2:
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        break;
      case 3:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        break;
      case 4:
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 5:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 6:
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
      case 7:
        lperm.add(PosixFilePermission.OTHERS_READ);
        lperm.add(PosixFilePermission.OTHERS_WRITE);
        lperm.add(PosixFilePermission.OTHERS_EXECUTE);
        break;
    }
  }
  
}
