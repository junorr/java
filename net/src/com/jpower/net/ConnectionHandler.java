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

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Interface responsável pelo tratamento de conexãoes 
 * tanto de servidores quanto de clientes.
 * 
 * @author Juno Roesler
 * @version 1.0 - 28/05/2013
 */
public interface ConnectionHandler {
  
  /**
   * Método invocado para tratar o recebimento de bytes
   * da conexão.
   * @param buffer <code>ByteBuffer</code>.
   * @see java.nio.ByteBuffer
   */
  public void received(ByteBuffer buffer);
  
  /**
   * Método invocado na realização de uma conexão
   * bem sucedida.
   * @param channel <code>SocketChannel</code> da conexão.
   * @see java.nio.channels.SocketChannel
   */
  public void connected(SocketChannel channel);
  
  /**
   * Método invocado quando a conexão é fechada.
   * @param channel <code>SocketChannel</code> da conexão.
   * @see java.nio.channels.SocketChannel
   */
  public void disconnected(SocketChannel channel);
  
  /**
   * Método invocado na ocorrência de erro na conexão.
   * @param th Erro <code>Throwable</code>.
   */
  public void error(Throwable th);
  
  /**
   * Método invocado para obter o conteúdo
   * a ser enviado pela conexão.
   * @return <code>ByteBuffer</code> com o conteúdo a ser enviado.
   * @see com.jpower.net.ConnectionHandler#isSending() 
   */
  public ByteBuffer sending();
  
  /**
   * Método invocado para saber se esta instância de 
   * <code>ConnectionHandler</code> está interessada
   * em enviar dados pela conexão. Os dados são
   * obtidos pelo método <code>sending() : boolean</code>,
   * que é invocado após o retorno deste.
   * @return <code>true</code> caso esta instância de 
   * <code>nnectionHandler</code> deseja enviar e possui dados
   * para envio pela conexão, <code>false</code> caso contrário.
   * @see com.jpower.net.ConnectionHandler#sending() 
   */
  public boolean isSending();
  
  /**
   * Método invocado após o envio dos dados pela
   * conexão, informando a quantidade de bytes enviados
   * com sucesso.
   * @param bytes Quantidade de bytes enviados com sucesso
   * pela conexão.
   */
  public void sent(int bytes);

}
