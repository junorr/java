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

package us.pserver.jackson.test.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2016
 */
public interface IFPermissions {

  public Set<FPermission> owner();
  
  public Set<FPermission> group();
  
  public Set<FPermission> others();
  
  @JsonValue
  public String toPosixCode();
  
  public int toPosixBin();
  
  public String toPosixString();
  
  
  public static IFPermissions fromPosixBin(int cd) {
    int o = cd >> 6;
    int g = (cd >> 3) & 0b111;
    int a = cd & 0b111;
    if(o == 0) {
      throw new IllegalArgumentException("Bad Owner Permission: "
          + o+ " (PermCode: "+ cd+ ")"
      );
    }
    return new FPermissions(
        FPermission.fromPosixBin(o), 
        FPermission.fromPosixBin(g), 
        FPermission.fromPosixBin(a)
    );
  }
  
  
  @JsonCreator
  public static IFPermissions fromPosixCode(String code) {
    if(code == null || code.length() != 3) {
      throw new IllegalArgumentException("Bad Posix String: "+ code);
    }
    int o = Integer.parseInt(code.substring(0, 1));
    int g = Integer.parseInt(code.substring(1, 2));
    int a = Integer.parseInt(code.substring(2));
    return new FPermissions(
        FPermission.fromPosixBin(o),
        FPermission.fromPosixBin(g),
        FPermission.fromPosixBin(a)
    );
  }
  
  
  public static IFPermissions fromPosixString(String str) {
    if(str == null || str.length() != 9) {
      throw new IllegalArgumentException("Bad Posix String: "+ str);
    }
    String o = str.substring(0, 3);
    String g = str.substring(3, 6);
    String a = str.substring(6);
    return new FPermissions(
        FPermission.fromPosixString(o), 
        FPermission.fromPosixString(g), 
        FPermission.fromPosixString(a)
    );
  }
  
  
  public static IFPermissions fromDosAttrs(DosFileAttributes attrs) {
    Set<FPermission> owner = new HashSet<>();
    Set<FPermission> group = new HashSet<>();
    Set<FPermission> others = new HashSet<>();
    owner.add(FPermission.READ);
    if(!attrs.isReadOnly()) owner.add(FPermission.WRITE);
    group.add(FPermission.READ);
    others.add(FPermission.READ);
    return new FPermissions(owner, group, others);
  }
  
  
  public static IFPermissions fromPosixAttrs(PosixFileAttributes attrs) {
    Set<FPermission> owner = new HashSet<>();
    Set<FPermission> group = new HashSet<>();
    Set<FPermission> others = new HashSet<>();
    Iterator<PosixFilePermission> it = attrs.permissions().iterator();
    while(it.hasNext()) {
      switch(it.next()) {
        case OWNER_READ:
          owner.add(FPermission.READ);
          break;
        case OWNER_WRITE:
          owner.add(FPermission.WRITE);
          break;
        case OWNER_EXECUTE:
          owner.add(FPermission.EXEC);
          break;
        case GROUP_READ:
          group.add(FPermission.READ);
          break;
        case GROUP_WRITE:
          group.add(FPermission.WRITE);
          break;
        case GROUP_EXECUTE:
          group.add(FPermission.EXEC);
          break;
        case OTHERS_READ:
          others.add(FPermission.READ);
          break;
        case OTHERS_WRITE:
          others.add(FPermission.WRITE);
          break;
        case OTHERS_EXECUTE:
          others.add(FPermission.EXEC);
          break;
      }
    }
    return new FPermissions(owner, group, others);
  }
  
  
  public static IFPermissions from(Path path) throws IOException {
    try {
      return fromPosixAttrs(Files.readAttributes(path, PosixFileAttributes.class));
    } 
    catch(UnsupportedOperationException e) {
      return fromDosAttrs(Files.readAttributes(path, DosFileAttributes.class));
    } 
  }
  
}
