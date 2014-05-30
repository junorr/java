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


/**
 * <p style="font-size: medium;">
 * Ouvinte de eventos de erros no envio/recebimento de email, 
 * a ser registrado na classe <code>SMail</code>.
 * </p>
 * 
 * @see com.jpower.simplemail.SMail
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "ErrorMailListener",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Ouvinte de eventos erro de email"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public interface ErrorMailListener extends Listener {
  
  
  /**
   * Método invocado quando ocorrer erro 
   * ao enviar um email.
   * @param event evento de email.
   * @param t Erro lançado.
   */
  public void errorSending(MailEvent event, Throwable t);
  
  
  /**
   * Método invocado quando ocorrer erro ao 
   * receber um email.
   * @param event evento de email.
   * @param t Erro lançado.
   */
  public void errorReceiving(MailEvent event, Throwable t);
  
  
  /**
   * Metodo invocado quando ocorrer um erro
   * de conexão.
   * @param event Evento de conexão.
   * @param cause Erro lançado.
   */
  public void errorConnecting(ConnectionEvent event, Throwable cause);
  
}
