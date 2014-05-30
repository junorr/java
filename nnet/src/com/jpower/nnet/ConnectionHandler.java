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
package com.jpower.nnet;


/**
 * Handler para tratar a conexão de cliente/servidor
 * através da invocação seus métodos para 
 * cada evento da conexão.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-22
 */
public interface ConnectionHandler {
  
  /**
   * Invocado quando a conexão é estabelecida.
   * @param ch Canal de comunicação da conexão.
   */
  public void connected(TcpChannel ch);
  
  /**
   * Invocado na desconexão.
   * @param ch Canal de comunicação da conexão.
   */
  public void disconnected(TcpChannel ch);
  
  /**
   * Invocado quando a conexão é fechada.
   * @param ch Canal de comunicação da conexão.
   */
  public void closed(TcpChannel ch);
  
  /**
   * Invocado na ocorrência de exceções na conexão.
   * @param th Exceção lançada.
   * @param ch Canal de comunicação da conexão.
   */
  public void error(Throwable th, TcpChannel ch);
  
  /**
   * Invocado quando dados são recebidos pelo canal de comunicação.
   * @param buffer Dados recebidos pelo canal de comunicação.
   * @param ch Canal de comunicação da conexão.
   */
  public void received(DynamicBuffer buffer, TcpChannel ch);
  
}
