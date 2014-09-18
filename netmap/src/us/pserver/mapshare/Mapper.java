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

package us.pserver.mapshare;

import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 27/01/2014
 */
public abstract class Mapper {
  
  public static final String
      
      NET_USE = "NET USE ",
      
      USER = "/USER:",
      
      PERSIST = "/PERSISTENT:NO",
      
      DELETE = "/DELETE";
  
  
  public static void map(NetShare share, Credentials cred) {
    try {
      Process p;
      if(cred == null) {
        p = Runtime.getRuntime().exec(NET_USE 
            + share.getDrive()
            + " " + share.getPath()
            + PERSIST);
      }
      else {
        p = Runtime.getRuntime().exec(NET_USE 
            + share.getDrive()
            + " " + share.getPath()
            + " " + new String(cred.getPasswd())
            + " " + USER
            + cred.getUser() + " "
            + PERSIST);
      }
      p.waitFor();
    } catch(IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public static void unmap(String drive) {
    try {
      Process p = Runtime.getRuntime().exec(NET_USE 
          + drive + " " + DELETE);
      p.waitFor();
    } catch(IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public static boolean isMapped(String drive) {
    return Files.exists(Paths.get(drive));
  }
  
  
  public static boolean persist(Object obj, String file) {
    if(obj == null|| file == null)
      return false;
    
    XStream xst = new XStream();
    try (FileOutputStream fos = new FileOutputStream(file);) {
      xst.toXML(obj, fos);
      return true;
    } catch(Exception e) {
      return false;
    }
  }
  
  
  public static <T> T load(String file) {
    if(file == null || !Files.exists(Paths.get(file)))
      return null;
    
    XStream xst = new XStream();
    try (FileInputStream fis = new FileInputStream(file);) {
      return (T) xst.fromXML(fis);
    } catch(Exception e) {
      return null;
    }
  }
  
}
