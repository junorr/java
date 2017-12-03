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

package us.pserver.dyna;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Directory change watching engine. DirectoryWatcher
 * listen for jar files changes in some directory structure,
 * automaticaly updating DynaLoader.
 * @see us.pserver.dyna.DynaLoader
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public interface DirectoryWatcher {
  
  /**
   * Starts the directory watching service.
   * @return this instance of DirectoryWatcher;
   */
  public DirectoryWatcher start();

  /**
   * Stops the directory watching service.
   * @return this instance of DirectoryWatcher;
   */
  public DirectoryWatcher stop();
  
  /**
   * Return the directory path under watch.
   * @return The directory path under watch.
   */
  public Path getDirectory();

  /**
   * Return the DynaLoader binded to the directory.
   * @return The DynaLoader binded to the directory.
   */
  public DynaLoader getDynaLoader();
  
  /**
   * Adds a listener for classpath changes.
   * @param lst The listener.
   * @return This DirectoryWatcher instance.
   */
  public DirectoryWatcher addChangeListener(Consumer<DynaLoader> lst);
  
  /**
   * Removes a classpath change listener.
   * @param lst The listener to be removed.
   * @return This DirectoryWatcher instance.
   */
  public DirectoryWatcher rmChangeListener(Consumer<DynaLoader> lst);
  
  /**
   * Clear all classpath change listeners.
   * @return This DirectoryWatcher instance.
   */
  public DirectoryWatcher clearChangeListeners();
  
}
