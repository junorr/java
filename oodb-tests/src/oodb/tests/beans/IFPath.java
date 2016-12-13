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
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public interface IFPath {

  public String getName();
  
  public String getPath();
  
  public String[] split();
  
  public Path toPath();
  
  public File toFile();
  
  public IFPath cd() throws IOException;
  
  public IFPath cd(String spath) throws IOException;
  
  public IFPath cd(IFPath path) throws IOException;
  
  public List<IFPath> ls() throws IOException;
  
  public boolean isDirectory();
  
  public boolean isRoot();
  
  public IFSize size();
  
  public IFPermissions getPermissions();
  
  public IFTime getTime();
  
  public String getOwner();
  
  public String getGroup();
  
  
  public static IFPath from(Path path) {
    try {
      return FPath.builder(path).create();
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public static IFPath from(PosixFileAttributes atts) {
    return FPath.builder().create(atts);
  }
  
  
  public static IFPath from(DosFileAttributes atts) {
    return FPath.builder().create(atts);
  }
  
}
