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

package us.pserver.rob.channel;

import java.io.IOException;


/**
 * Interface que define um canal de transmissão 
 * e codificação de objetos.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public interface Channel {
  
  /**
   * Escreve um objeto <code>Transport</code> no canal
   * de transmissão.
   * @param trp Objeto <code>Transport</code>.
   * @throws IOException Caso ocorra erro na
   * transmissão.
   * @see us.pserver.remote.Transport
   */
  public void write(Transport trp) throws IOException;
  
  /**
   * Lê um objeto <code>Transport</code> do
   * canal de transmissão.
   * @return Objeto <code>Transport</code>
   * @throws IOException Caso ocorra erro 
   * na leitura da transmissão.
   * @see us.pserver.remote.Transport
   */
  public Transport read() throws IOException;
  
  /**
   * Fecha o canal de transmissão.
   */
  public void close();
  
  /**
   * Verifica se o canal continua válido
   * para executar transmissões.
   * @return <code>true</code> se o canal continua
   * válido para executar transmissões, <code>false</code>
   * caso contrário.
   */
  public boolean isValid();
  
}
