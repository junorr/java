/*
 *  Direitos Autorais Reservados (c) 2009 Juno Roesler
 *  Contato com o autor: juno.rr@gmail.com
 *
 *  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 *  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 *  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
 *  versão posterior.
 *
 *  Este software é distribuído na expectativa de que seja útil, porém, SEM
 *  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 *  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 *  Geral Menor do GNU para mais detalhes.
 *
 *  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 *  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
 *  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 * */
package com.jpower.utils;

import java.net.InetAddress;

/**
 * Interface para classe utilitária
 * para execução do comando ping.
 * @author Juno Roesler
 */
public interface PingTool extends Runnable {

  /**
   * Seta o host alvo.
   * @param host Nome ou endereço ip.
   */
  public void setHost(String host);

  /**
   * Retorna o host alvo.
   * @return Nome ou endereço ip.
   */
  public String getHost();

  /**
   * Retorna o endereço do host alvo.
   * @return Endereço InetAddress.
   */
  public InetAddress getHostAddress();

  /**
   * Seta a quantidade de ping's
   * a executar.
   * @param count Quantidade de comandos
   * ping sucessivos.
   */
  public void setCount(int count);

  /**
   * Retorna a quantidade de ping's
   * a executar.
   * @return Quantidade de comandos
   * ping sucessivos.
   */
  public int getCount();

  /**
   * Seta o tamanho do pacote.
   * @param size tamanho do pacote em bytes.
   */
  public void setPacketSize(int size);

  /**
   * Retorna o tamanho do pacote.
   * @return tamanho do pacote em bytes.
   */
  public int getPacketSize();

  /**
   * Seta o "tempo de vida" do pacote, ou seja,
   * quantos "saltos" pode passar até tornar-se
   * inválido e seja descartado.
   * @param ttl Time To Live.
   */
  public void setTTL(int ttl);

  /**
   * Retorna o "tempo de vida" do pacote, ou seja,
   * quantos "saltos" pode passar até tornar-se
   * inválido e seja descartado.
   * @return Time To Live.
   */
  public int getTTL();

  /**
   * Seta o tempo de espera até o
   * retorno do ECHO_REPLY.
   * @param timeout Em segundos
   */
  public void setTimeout(int timeout);

  /**
   * Retorna o tempo de espera até o
   * retorno do ECHO_REPLY.
   * @return Em segundos
   */
  public int getTimeout();

  /**
   * Executa o comando ping.
   */
  public void run();

  /**
   * Representação em String.
   * @return String
   */
  @Override
  public String toString();

  /**
   * Retorna a saída do comando.
   * @return
   */
  public String getOutput();

  /**
   * Retorna true se o host foi
   * atingido com sucesso, false
   * caso contrário.
   * @return boolean
   */
  public boolean isSuccessful();

  /**
   * Retorna a linha principal de retorno do
   * comando.
   * @return String
   */
  public String getReturnLine();

  /**
   * Retorna a quantidade de disparos
   * que atingiram o alvo com sucesso.
   * @return Disparos atingidos.
   */
  public int getHits();

}
