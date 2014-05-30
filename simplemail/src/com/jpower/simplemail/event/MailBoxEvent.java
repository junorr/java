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

package com.jpower.simplemail.event;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.simplemail.MailBox;


/**
 * <p style="font-size: medium;">
 * Evento gerado por <code>MailBox</code>
 * quando ocorrerem mudanças nas mensagens 
 * da caixa.
 * </p>
 *
 * @see com.jpower.simplemail.MailBox
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "MailBoxEvent",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Evento de ações de MailBox"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class MailBoxEvent extends AbstractEvent<MailBox> {
  
  private int msgcount;
  
  private int newcount;
  
  
  /**
   * Construtor padrão sem argumentos
   */
  public MailBoxEvent() {
    super();
    msgcount = -1;
    newcount = -1;
  }
  
  
  /**
   * Construtor que recebe o MailBox gerador do evento.
   * @param src MailBox gerador do evento.
   */
  public MailBoxEvent(MailBox src) {
    super(src);
    msgcount = -1;
    newcount = -1;
  }
  
  
  /**
   * Construtor que recebe o MailBox gerador do evento,
   * o número total de mensagens e o número de novas
   * mensagens da caixa de email.
   * @param src MailBox gerador do evento.
   * @param messages Número de mensagens na caixa de email.
   * @param newMessages Numero de novas mensagens 
   * na caixa de email.
   */
  public MailBoxEvent(MailBox src, int messages, int newMessages) {
    this(src);
    msgcount = messages;
    newcount = newMessages;
  }


  /**
   * Retorna o número de mensagens existentes
   * na caixa de email.
   * @return Número de mensagens.
   */
  public int getMessagesCount() {
    return msgcount;
  }


  /**
   * Define o número de mensagens existentes
   * na caixa de email.
   * @param msgcount Número de mensagens.
   */
  public void setMessagesCount(int msgcount) {
    this.msgcount = msgcount;
  }


  /**
   * Retorna o número de novas mensagens existentes
   * na caixa de email.
   * @return Número de novas mensagens.
   */
  public int getNewMessagesCount() {
    return newcount;
  }


  /**
   * Define o número de novas mensagens existentes
   * na caixa de email.
   * @param newcount Número de novas mensagens.
   */
  public void setNewMessagesCount(int newcount) {
    this.newcount = newcount;
  }
  
}
