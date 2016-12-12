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
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public interface IFSPath {

  public String getName();
  
  public String getPath();
  
  public String[] split();
  
  public Path toPath();
  
  public File toFile();
  
  public IFSPath cd();
  
  public IFSPath cd(IFSPath path);
  
  public List<IFSPath> ls();
  
  public boolean isDirectory();
  
  public boolean isRoot();
  
  public ISize size();
  
  public IFSPermissions getPermissions();
  
  public IFSTime getTime();
  
  public String getOwner();
  
  public String getGroup();
  
}
