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

package us.pserver.remote;

import java.io.IOException;


/**
 * Interface que define a estrutura principal
 * do servidor de objetos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public interface Server extends Runnable {
  
  /**
   * Quantidade total de <code>Threads</code> disponíveis
   * para no pool que atende as demandas
   * <br/><code>DEFAULT_AVAILABLE_THREADS = 6</code>.
   */
  public static final int DEFAULT_AVAILABLE_THREADS = 6;
  
  
  /**
   * Define o container de objetos.
   * @param cont Container de objetos.
   * @see us.pserver.remote.ObjectContainer
   */
  public void setContainer(ObjectContainer cont);
  
  /**
   * Retorna o container de objetos.
   * @return Container de objetos.
   * @see us.pserver.remote.ObjectContainer
   */
  public ObjectContainer container();
  
  /**
   * Define a quantidade de <code>Threads</code>
   * que atenderão as demandas do servidor.
   * @param threads Quantidade de <code>Threads</code>
   * que atenderão a demanda do servidor.
   * @see us.pserver.remote.Server#DEFAULT_AVAILABLE_THREADS
   */
  public void setAvailableThreads(int threads);
  
  /**
   * Retorna a quantidade de <code>Threads</code>
   * que atenderão as demandas do servidor.
   * @return Quantidade de <code>Threads</code>
   * que atenderão a demanda do servidor.
   * @see us.pserver.remote.Server#DEFAULT_AVAILABLE_THREADS
   */
  public int getAvailableThreads();
  
  /**
   * Inicia a execução do servidor.
   * @throws IOException Caso ocorra erro
   * na execução do servidor.
   */
  public void start() throws IOException;
  
  /**
   * Para a execução do servidor.
   */
  public void stop();
  
  /**
   * Verifica se o servidor está em execução.
   * @return <code>true</code> se o servidor
   * está em execução, <code>false</code>
   * caso contrário.
   */
  public boolean isRunning();
  
}
