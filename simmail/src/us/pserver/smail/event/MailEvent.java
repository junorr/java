/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package us.pserver.smail.event;

import us.pserver.smail.Message;
import us.pserver.smail.SMail;


/**
 * <p style="font-size: medium;">
 * Evento gerado por SMail para conexão, envio e
 * recebimento de emails.
 * </p>
 * 
 * @see us.pserver.smail.SMail
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class MailEvent extends AbstractEvent<SMail> {
  
  private Message message;
  
  
  /**
   * Construtor padrão que recebe o SMail 
   * gerador do evento.
   * @param src Gerador do evento.
   */
  public MailEvent(SMail src) {
    super(src);
  }
  
  
  /**
   * Construtor que recebe o gerador do evento e 
   * a mensagem relacionada ao evento.
   * @param src Gerador do evento.
   * @param msg Mensagem relacionada ao evento.
   */
  public MailEvent(SMail src, Message msg) {
    super(src);
    this.setMessage(msg);
  }
  
  
  /**
   * Retorna a mensagem relacionada ao evento.
   * @return Mensagem.
   */
  public Message getMessage() {
    return message;
  }


  /**
   * Define a mensagem relacionada ao evento.
   * @param message Mensagem.
   */
  public void setMessage(Message message) {
    this.message = message;
  }

}
