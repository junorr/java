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

import java.io.Closeable;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

/**
 * DynaLoader is a jar files registry for
 * loading classes and resources dinamically.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/06/2017
 */
public interface DynaLoader extends Closeable {

  /**
   * Return a list with jar files registered.
   * @return 
   */
  public List<Path> listJars();
  
  /**
   * Return an array with all jar files URLs.
   * @return 
   */
  public URL[] listURLs();
  
  /**
   * Load a class from registered jar files.
   * @param cls Class name to load.
   * @return The loaded Class.
   */
  public Class<?> load(String cls);
  
  /**
   * Load a class from the given jar file path.
   * @param cls Class name to load.
   * @param jar The jar file path from the class will be loaded.
   * @return The loaded Class.
   */
  public Class<?> load(String cls, Path jar);
  
  /**
   * Load a class from registered jar files 
   * and instantiates it with no args constructor.
   * @param cls Class name to load.
   * @return The object created from loaded class.
   */
  public Object loadAndCreate(String cls);
  
  /**
   * Returns a ResourceLoader for the class name informed.
   * @param cls Class name for ResourceLoader.
   * @return The ResourceLoader created for the class name.
   * @see us.pserver.dyna.ResourceLoader
   */
  public ResourceLoader getResourceLoader(String cls);
  
  /**
   * Returns a ResourceLoader for the class informed.
   * @param cls Class for ResourceLoader.
   * @return The ResourceLoader created for the given class.
   * @see us.pserver.dyna.ResourceLoader
   */
  public ResourceLoader getResourceLoader(Class cls);
  
  /**
   * Return the ClassLoader used by DynaLoader.
   * @return 
   */
  public ClassLoader getClassLoader();
  
  /**
   * Check if a jar file path is already registered in DynaLoader.
   * @param path The jar file path to be checked.
   * @return <code>true</code> if the jar file path is
   * already registered in DynaLoader, <code>false</code> otherwise.
   */
  public boolean isRegistered(Path path);
  
  /**
   * Register a path in DynaLoader. The path may be 
   * from file or directory. Directories will be scanned
   * recursively for jar files.
   * @param path The path to be registered.
   * @return This instance of DynaLoader.
   */
  public DynaLoader register(Path path);
  
  /**
   * Unregister a path previously registered in DynaLoader. 
   * The path must be from a unique jar file.
   * @param path The jar file path to be unregistered.
   * @return This instance of DynaLoader.
   */
  public DynaLoader unregister(Path path);
  
  /**
   * Close the ClassLoader and clear all paths registered.
   * @return This instance of DynaLoader.
   */
  public DynaLoader reset();
  
}
