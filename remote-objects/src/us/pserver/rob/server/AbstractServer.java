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

package us.pserver.rob.server;

import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.server.Server;


/**
 * Classe abstrata que implementa funcionalidades
 * básicas de um servidor de objetos, como getters e setters.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 * @see us.pserver.remote.Server
 */
public abstract class AbstractServer implements Server {
  
  public static final String ABSTRACT_SERVER = "AbstractServer";
  
  
  int availableThreads;
  
  boolean running;
  
  ObjectContainer container;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  AbstractServer() {
    availableThreads = DEFAULT_AVAILABLE_THREADS;
    running = false;
    container = null;
  }
  
  
  /**
   * Construtor que recebe o container de objetos.
   * @param container Container de objetos.
   */
  AbstractServer(ObjectContainer container) {
    if(container == null)
      throw new IllegalArgumentException(
          "Invalid ObjectContainer ["+ container+ "]");
    container.put(ABSTRACT_SERVER, this);
    availableThreads = DEFAULT_AVAILABLE_THREADS;
    running = false;
    this.container = container;
  }


  @Override
  public void setContainer(ObjectContainer cont) {
    this.container = cont;
    if(cont != null)
      container.put(ABSTRACT_SERVER, this);
  }


  @Override
  public ObjectContainer container() {
    return container;
  }


  @Override
  public void setAvailableThreads(int threads) {
    if(threads < 1) throw new IllegalArgumentException(
        "Invalid available Threads ["+ threads+ "]");
    availableThreads = threads;
  }


  @Override
  public int getAvailableThreads() {
    return availableThreads;
  }


  @Override
  public void stop() {
    running = false;
  }


  @Override
  public boolean isRunning() {
    return running;
  }

}
