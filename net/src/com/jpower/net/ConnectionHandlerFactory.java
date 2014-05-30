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


package com.jpower.net;

/**
 * Interface modelo para fábrica de 
 * instâncias <code>ConnectionHandler</code>.
 * @see com.jpower.net.ConnectionHandler
 * @see com.jpower.net.NioServer
 * 
 * @author Juno Roesler
 * @version 1.0 - 28/05/2013
 */
public interface ConnectionHandlerFactory {
  
  /**
   * Método invocado para criar uma instância de
   * <code>ConnectionHandler</code>.
   * @return Instância de <code>ConnectionHandler</code>.
   * @see com.jpower.net.ConnectionHandler
   * @see com.jpower.net.NioServer
   */
  public ConnectionHandler createConnectionHandler();

}
