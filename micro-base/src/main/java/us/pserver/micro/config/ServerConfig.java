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

package us.pserver.micro.config;

import java.util.Map;

/**
 * Configurações do servidor de rede, encapsula 
 * informações como endereço e porta de escuta 
 * do serviço, threads e HttpHandler's para atendimento 
 * de requisições, entre outras. Pode ser criado a partir 
 * de um arquivo no formato json.
 * @author Juno Roesler - juno.roesler@bb.com.br
 * @version 1.0.201607
 */
public interface ServerConfig {
  
  /**
   * Quantidade padrão de threads primárias para 
   * atender as requisições do servidor 
   * (DEFAULT_IO_THREADS = 4).
   */
  public static final int DEFAULT_IO_THREADS = 4;
  
  /**
   * Quantidade padrão de threads secundárias para 
   * executar trabalhos "blocantes"
   * (DEFAULT_MAX_WORKER_THREADS = 10).
   */
  public static final int DEFAULT_MAX_WORKER_THREADS = 10;
  
  
  public String getAddress();
  
  public int getPort();
  
  public int getIoThreads();
  
  public int getMaxWorkerThreads();
  
  public boolean getDispatcherEnabled();
  
  public boolean getShutdownHandlerEnabled();
  
  public boolean getCorsHandlerEnabled();
  
  public Map<String,Class> getHandlers();
  
  public DBConfig getDbConfig();
  
  public ServerConfig setDbConfig(DBConfig dbc);
  
}
