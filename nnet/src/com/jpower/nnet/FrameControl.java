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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Objeto de configuração de controle de envio de frames de comunicação.
 * O controle de frames de comunicação evita o recebimento 
 * incompleto de informações pela outra ponta do canal de comunicação,
 * porém só funciona se as duas pontas de do canal de 
 * comunicação utilizarem a biblioteca <code>com.jpower.nnet</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-25
 */
public class FrameControl {
  
  /**
   * Frame de controle.
   */
  public static final String FRAME = "EOF\r\n\r\n";
  
  
  private boolean send;
  
  private boolean receive;
  
  
  /**
   * Construtor padrão sem argumentos, inicia
   * habilitado o controle de envio e recebimento.
   */
  public FrameControl() {
    send = true;
    receive = true;
  }
  
  
  /**
   * Define o controle de frames no envio de dados.
   * @param enabled <code>boolean</code> indicando se
   * o controle de envio de dados deve ser habilitado.
   * @return Esta instância modificada de <code>FrameControl</code>.
   */
  public FrameControl setEnabledOnSend(boolean enabled) {
    send = enabled;
    return this;
  } 
  
  
  /**
   * Define o controle de frames no recebimento de dados.
   * @param enabled <code>boolean</code> indicando se
   * o controle de recebimento de dados deve ser habilitado.
   * @return Esta instância modificada de <code>FrameControl</code>.
   */
  public FrameControl setEnabledOnReceive(boolean enabled) {
    receive = enabled;
    return this;
  }
  
  
  /**
   * Define o controle de frames no envio e recebimento de dados.
   * @param enabled <code>boolean</code> indicando se
   * o controle de envio e recebimento de dados deve ser habilitado.
   * @return Esta instância modificada de <code>FrameControl</code>.
   */
  public FrameControl setEnabled(boolean enabled) {
    return this.setEnabledOnReceive(enabled)
        .setEnabledOnSend(enabled);
  }
  
  
  /**
   * Verifica se o controle de frames no envio de dados está ativado.
   * @return enabled <code>boolean</code> indicando se
   * o controle de envio deve ser habilitado.
   */
  public boolean isEnabledOnReceive() {
    return receive;
  }
  
  
  /**
   * Verifica se o controle de frames no recebimento de dados está ativado.
   * @return enabled <code>boolean</code> indicando se
   * o controle de envio deve ser habilitado.
   */
  public boolean isEnabledOnSend() {
    return send;
  }
  
  
  public static byte[] getBytes() {
    try { 
      return FRAME.getBytes("UTF-8"); } 
    catch(UnsupportedEncodingException ex) {
      return null;
    }
  }
  
  
  public static String fromBytes(byte[] b) {
    if(b == null || b.length == 0) return null;
    try { return new String(b, "UTF-8"); }
    catch(UnsupportedEncodingException ex) {
      return null;
    }
  }
  
  
  public static boolean matchFrame(byte[] b) {
    String s = fromBytes(b);
    //System.out.println("matchFrame( "+ s+ ")");
    return s != null && (s.equals(FRAME) || s.contains("EOF"));
  }
  
}
