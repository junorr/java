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

/**
 * Filtro de dados para o tráfego de rede recebido por um socket.
 * É usado para controlar "janelas" de dados transmitidos na rede,
 * quando determinadas informações necessitam ser lidas apenas
 * quando estiverem completas. A tecnologia Java 7 NIO não
 * garante a entrega completa de determinada informação na outra ponta
 * da rede, ou seja, uma informação transmitida pode ser recebida 
 * incompleta no primeiro buffer recebido, sendo necessário aguardar
 * o restante da informação. <code>Receivefilter</code> é utilizado por 
 * <code>NioClient</code> e <code>NioServer</code> para filtrar os dados
 * recebidos, verificando se determinada porção de bytes está ou não 
 * presente em um buffer e então removendo esses dados, para realizar 
 * uma entrega "limpa", sem bytes de controle à classe que 
 * trata as conexões.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 10/12/2012
 */
public interface ReceiveFilter {
  
  /**
   * Verifica se o buffer informado contém os dados 
   * esperados.
   * @param buffer <code>ByteBuffer</code> a ser verificado.
   * @return <code>true</code> se o buffer contiver os
   * dados esperados pelo filtro, <code>false</code>
   * caso contrário.
   */
  public boolean match(ByteBuffer buffer);
  
  /**
   * Remove os dados de controle utilizado pelo filtro.
   * @param buffer <code>ByteBuffer</code>.
   * @return Novo buffer sem os dados de controle 
   * utilizados pelo filtro.
   */
  public ByteBuffer filter(ByteBuffer buffer);

}
